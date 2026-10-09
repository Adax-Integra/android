package com.example.adaxintegra.data.repository

import com.example.adaxintegra.data.remote.api.AuthApi
import com.example.adaxintegra.data.remote.dto.ChangePasswordRequestDto
import com.example.adaxintegra.data.remote.dto.LoginRequestDto
import com.example.adaxintegra.data.remote.dto.RegisterRequestDto
import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.model.UserSession
import com.example.adaxintegra.domain.repository.AuthRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.OtpType
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton
import android.util.Base64
import com.example.adaxintegra.data.local.SessionPreferences
import org.json.JSONException
import org.json.JSONObject

/**
 * Implementation of AuthRepository managing Supabase Auth and remote backend synchronization.
 * Handles login validation, user registration, OTP verification, password change, and session management.
 */
@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi,
    private val supabase: SupabaseClient,
    private val sessionPreferences: SessionPreferences,
) : AuthRepository {
    // G-09-Register: Active user session StateFlow held in memory
    private val _session = MutableStateFlow<UserSession?>(null)
    override val session: StateFlow<UserSession?> = _session.asStateFlow()

    // G-09-Register: Authenticates user credentials with Supabase Auth and enforces email confirmation
    override fun login(
        email: String,
        password: String,
    ): Flow<Result<UserSession>> = flow {
        emit(Result.Loading)

        try {
            // G-09-Register: Attempt direct Supabase authentication
            supabase.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            // G-09-Register: Ensure the user's email has been confirmed before granting session access
            val currentUser = supabase.auth.currentUserOrNull()
            if (currentUser != null && (currentUser.emailConfirmedAt == null)) {
                supabase.auth.signOut()
                throw IllegalStateException("email_not_confirmed")
            }

            // Remote backend authentication fallback/synchronization
            val response = api.login(LoginRequestDto(email, password))

            if (!response.success) {
                throw IllegalStateException("No se pudo iniciar sesión")
            }

            val data = response.data
            val role = data.roles?.singleOrNull()

            if (role == null || role !in listOf("external", "internal", "admin")) {
                throw IllegalStateException("La cuenta no tiene un rol válido. Contacta al administrador.")
            }

            if (data.token.isBlank() || data.userId.isNullOrBlank()) {
                throw IllegalStateException("La respuesta de inicio de sesión está incompleta")
            }

            val userSession = UserSession(
                token = data.token,
                userId = data.userId,
                role = role,
            )

            // Persist the session before making it available to the interface.
            sessionPreferences.save(userSession)
            _session.value = userSession
            emit(Result.Success(userSession))
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: HttpException) {
            val errorBody = exception.response()?.errorBody()?.string() ?: ""
            if (errorBody.contains("email_not_confirmed", ignoreCase = true) ||
                errorBody.contains("Email not confirmed", ignoreCase = true)
            ) {
                emit(Result.Error(IllegalStateException("email_not_confirmed")))
            } else {
                emit(Result.Error(exception))
            }
        } catch (exception: Exception) {
            val errorText = "${exception.message} ${exception.cause} ${exception.javaClass.simpleName}".lowercase()
            if (errorText.contains("email_not_confirmed") ||
                errorText.contains("email not confirmed") ||
                exception.message == "email_not_confirmed"
            ) {
                emit(Result.Error(IllegalStateException("email_not_confirmed")))
            } else {
                emit(Result.Error(exception))
            }
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

    override suspend fun forgotPassword(
        email: String,
    ): Result<Unit> {
        return try {
            supabase.auth.resetPasswordForEmail(
                email = email,
                redirectUrl = "adax://auth/callback",
            )

            Result.Success(Unit)
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            Result.Error(exception)
        }
    }

    override suspend fun resetPassword(
        newPassword: String,
    ): Result<Unit> {
        return try {
            supabase.auth.updateUser {
                password = newPassword
            }

            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
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

    // G-09-Register: Creates a new user account with Supabase Auth and remote backend
    override fun register(
        name: String,
        lastname: String,
        phone: String,
        email: String,
        password: String,
    ): Flow<Result<UserSession>> = flow {
        emit(Result.Loading)
        try {
            // G-09-Register: Register account in Supabase Auth
            supabase.auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }

            // Synchronize user account registration with Node backend
            @Suppress("SwallowedException")
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
            } catch (_: Exception) {
                // Secondary backend synchronization catch
            }

            val session = UserSession(
                token = "",
                userId = null,
                role = "external",
            )
            emit(Result.Success(session))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    // G-09-VerifyOTP: Requests Supabase to resend the 6-digit email confirmation OTP
    override fun resendVerificationEmail(email: String): Flow<Result<Unit>> = flow {
        emit(Result.Loading)
        try {
            supabase.auth.resendEmail(OtpType.Email.SIGNUP, email)
            emit(Result.Success(Unit))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    // G-09-VerifyOTP: Verifies the 6-digit OTP code entered by the user with Supabase Auth
    override fun verifyEmailCode(
        email: String,
        code: String,
    ): Flow<Result<UserSession>> = flow {
        emit(Result.Loading)
        try {
            // G-09-VerifyOTP: Call Supabase Auth verifyEmailOtp API for SIGNUP type
            supabase.auth.verifyEmailOtp(
                type = OtpType.Email.SIGNUP,
                email = email,
                token = code,
            )

            val resultSession = UserSession(
                token = "",
                userId = null,
                role = "external",
            )
            emit(Result.Success(resultSession))
        } catch (_: Exception) {
            emit(Result.Error(Exception("Código de verificación incorrecto o expirado")))
        }
    }

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
}
