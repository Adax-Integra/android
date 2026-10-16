package com.example.adaxintegra.data.repository

import android.util.Base64
import com.example.adaxintegra.data.local.SessionPreferences
import com.example.adaxintegra.data.remote.api.AuthApi
import com.example.adaxintegra.data.remote.dto.ChangePasswordRequestDto
import com.example.adaxintegra.data.remote.dto.ForgotPasswordRequestDto
import com.example.adaxintegra.data.remote.dto.LoginDataDto
import com.example.adaxintegra.data.remote.dto.LoginRequestDto
import com.example.adaxintegra.data.remote.dto.RegisterRequestDto
import com.example.adaxintegra.data.remote.dto.ResendVerificationRequestDto
import com.example.adaxintegra.data.remote.dto.ResetPasswordRequestDto
import com.example.adaxintegra.data.remote.dto.VerifyEmailRequestDto
import com.example.adaxintegra.data.remote.dto.ValidateCodeRequestDto
import com.example.adaxintegra.data.remote.toAuthError
import com.example.adaxintegra.domain.common.AuthError
import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.model.UserSession
import com.example.adaxintegra.domain.repository.AuthRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import org.json.JSONException
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

/*
 * Auth against the Node backend (G-01, G-02, G-09, G-12) with the session saved
 * on the device (G-15).
 */
@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi,
    private val sessionPreferences: SessionPreferences,
) : AuthRepository {
    // Active user session held in memory
    private val _session = MutableStateFlow<UserSession?>(null)
    override val session: StateFlow<UserSession?> = _session.asStateFlow()

    // G-02: one request to the backend; it also rejects unverified emails (403)
    override fun login(
        email: String,
        password: String,
    ): Flow<Result<UserSession>> = flow {
        emit(Result.Loading)
        try {
            val response = api.login(LoginRequestDto(email.trim(), password))
            emit(Result.Success(startSession(response.data)))
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            emit(Result.Error(exception.toAuthError()))
        }
    }

    override suspend fun restoreSession() {
        val storedSession = sessionPreferences.load()

        if (storedSession == null) {
            _session.value = null
            return
        }

        if (!hasUnexpiredToken(storedSession.token)) {
            sessionPreferences.clear()
            _session.value = null
            return
        }

        _session.value = storedSession
    }

    override suspend fun logout() {
        // Clear storage first so the session cannot return after reopening.
        sessionPreferences.clear()
        _session.value = null
    }

    //g-05 password recovery flow
    override suspend fun forgotPassword(
        email: String,
    ): Result<Unit> {
        return try {
            val response = api.forgotPassword(ForgotPasswordRequestDto(email.trim()))

            if (!response.success) {
                throw AuthError.Unknown("forgot-password returned success=false")
            }

            Result.Success(Unit)
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            Result.Error(exception.toAuthError())
        }
    }

    override suspend fun validateRecoveryCode(
        email: String,
        code: String,
    ): Result<Unit> {
        return try {
            val response = api.validateRecoveryCode(ValidateCodeRequestDto(
                email = email.trim(),
                code = code.trim()))

            if (!response.success) {
                throw AuthError.Unknown("validate-code returned success=false")
            }

            Result.Success(Unit)
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            Result.Error(exception.toAuthError())
        }
    }

    override suspend fun resetPassword(
        email: String,
        code: String,
        newPassword: String,
    ): Result<Unit> {
        return try {
            val response = api.resetPassword(ResetPasswordRequestDto(
                email = email.trim(),
                code = code.trim(),
                newPassword = newPassword))

            if (!response.success) {
                throw AuthError.Unknown("reset-password returned success=false")
            }

            Result.Success(Unit)
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            Result.Error(exception.toAuthError())
        }
    }

    // G-01: errors are no longer swallowed; the screen shows what the backend answered
    override fun register(
        name: String,
        lastname: String,
        phone: String,
        email: String,
        password: String,
    ): Flow<Result<Unit>> = flow {
        emit(Result.Loading)
        try {
            api.register(
                RegisterRequestDto(
                    name = name,
                    lastName = lastname,
                    phone = phone,
                    email = email,
                    password = password,
                    confirmPassword = password,
                ),
            )
            emit(Result.Success(Unit))
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            emit(Result.Error(exception.toAuthError()))
        }
    }

    // G-09: the backend answers the same for unknown emails, so success does not
    // mean the email exists
    override fun resendVerificationEmail(email: String): Flow<Result<Unit>> = flow {
        emit(Result.Loading)
        try {
            api.resendVerification(ResendVerificationRequestDto(email.trim()))
            emit(Result.Success(Unit))
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            emit(Result.Error(exception.toAuthError()))
        }
    }

    // G-09: a valid code returns the same session as the login, so the user
    // is signed in right away and the session survives restarts (G-15)
    override fun verifyEmailCode(
        email: String,
        code: String,
    ): Flow<Result<UserSession>> = flow {
        emit(Result.Loading)
        try {
            val response = api.verifyEmail(VerifyEmailRequestDto(email.trim(), code))
            emit(Result.Success(startSession(response.data)))
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            emit(Result.Error(exception.toAuthError()))
        }
    }

    // G-12 (unchanged)
    override fun changePassword(
        currentPassword: String,
        newPassword: String,
        confirmPassword: String,
    ): Flow<Result<String>> = flow {
        emit(Result.Loading)
        try {
            val token = _session.value?.token
            if (token.isNullOrBlank()) {
                throw IllegalStateException("Inicia sesión para cambiar la contraseña.")
            }

            val response = api.changePassword(
                authorization = "Bearer $token",
                request = ChangePasswordRequestDto(
                    currentPassword = currentPassword,
                    newPassword = newPassword,
                    confirmPassword = confirmPassword,
                ),
            )

            if (!response.success) {
                val errorMsg = translateErrorMessage(response.error)
                throw IllegalStateException(errorMsg)
            }

            val message = "Contraseña actualizada exitosamente."
            emit(Result.Success(message))
        } catch (e: Exception) {
            val translated = when (e) {
                is java.io.IOException -> "Sin conexión a internet. Revisa tu red e inténtalo de nuevo."
                else -> translateErrorMessage(e.message)
            }
            emit(Result.Error(Exception(translated)))
        }
    }

    // Shared by login and verification: validates the backend answer, then saves
    // the session before exposing it (same order G-15 used for the login)
    private suspend fun startSession(data: LoginDataDto): UserSession {
        val role = data.roles?.singleOrNull()

        if (role !in VALID_ROLES || data.token.isBlank() || data.userId.isNullOrBlank()) {
            throw AuthError.Unknown("Invalid session data from the server")
        }

        val userSession = UserSession(
            token = data.token,
            userId = data.userId,
            role = role,
        )

        sessionPreferences.save(userSession)
        _session.value = userSession
        return userSession
    }

    // Reads expiration locally; the backend still verifies the JWT signature.
    private fun hasUnexpiredToken(token: String): Boolean {
        val parts = token.split(".")
        if (parts.size != 3) return false

        return try {
            val decodedPayload = Base64.decode(
                parts[1],
                Base64.URL_SAFE or Base64.NO_WRAP,
            )

            val payload = JSONObject(
                String(decodedPayload, Charsets.UTF_8),
            )

            val expiresAt = payload.getLong("exp")
            val currentTime = System.currentTimeMillis() / 1000

            expiresAt > currentTime
        } catch (_: IllegalArgumentException) {
            false
        } catch (_: JSONException) {
            false
        }
    }

    // G-12 (unchanged)
    private fun translateErrorMessage(rawError: String?): String {
        if (rawError.isNullOrBlank()) return "Ocurrió un error al cambiar la contraseña."
        val lower = rawError.lowercase()
        return when {
            "current password" in lower || "incorrect" in lower ->
                "La contraseña actual es incorrecta."
            "unauthorized" in lower || "token" in lower || "401" in lower ->
                "Tu sesión ha caducado. Vuelve a iniciar sesión."
            "same" in lower || "different" in lower ->
                "La nueva contraseña debe ser diferente a la actual."
            "match" in lower ->
                "Las contraseñas no coinciden."
            "network" in lower || "connect" in lower ->
                "Sin conexión a internet. Revisa tu red e inténtalo de nuevo."
            else ->
                "No se pudo cambiar la contraseña. Revisa que tu contraseña actual sea correcta."
        }
    }

    private companion object {
        val VALID_ROLES = setOf("external", "internal", "admin")
    }
}
