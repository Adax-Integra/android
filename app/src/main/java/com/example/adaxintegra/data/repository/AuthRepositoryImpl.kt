package com.example.adaxintegra.data.repository

import com.example.adaxintegra.data.remote.api.AuthApi
import com.example.adaxintegra.data.remote.dto.ChangePasswordRequestDto
import com.example.adaxintegra.data.remote.dto.LoginRequestDto
import com.example.adaxintegra.data.remote.dto.RegisterRequestDto
import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.model.UserSession
import com.example.adaxintegra.domain.repository.AuthRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

// File that manages the network's logic, catches errors and allows the operation state
@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi,
) : AuthRepository {
    // Keeps the session in memory until logout or termination process
    private val _session = MutableStateFlow<UserSession?>(null)

    // Other componnents can observe the session without modifying it directly
    override val session = _session.asStateFlow()

    override fun login(
        email: String,
        password: String,
    ): Flow<Result<UserSession>> = flow {
        emit(Result.Loading)

        try {
            val response = api.login(LoginRequestDto(email, password))

            if (!response.success) {
                throw IllegalStateException("No se pudo iniciar sesión")
            }

            val data = response.data

            // Return null if there is not a rol
            val role = data.roles?.singleOrNull()

            // Rejects missing or unsupported roles before creating a session
            if (
                role == null || role !in listOf("external", "internal", "admin")
            ) {
                throw IllegalStateException(
                    "La cuenta no tiene un rol válido. Contacta al administrador. ",
                )
            }

            // Values required for authenticated requests.
            if (data.token.isBlank() || data.userId.isNullOrBlank()) {
                throw IllegalStateException("La respuesta de inicio de sesión está incompleta")
            }

            val userSession = UserSession(
                token = data.token,
                userId = data.userId,
                role = role,
            )

            // Makes the session available before reporting login success
            _session.value = userSession
            emit(Result.Success(userSession))
        } catch (exception: CancellationException) {
            // Preserves coroutine cancellation insted of reporting a login error
            throw exception
        } catch (exception: Exception) {
            emit(Result.Error(exception))
        }
    }

    override fun logout() {
        // Clears local state
        _session.value = null
    }

    override fun register(
        name: String,
        lastname: String,
        phone: String,
        email: String,
        password: String,
    ): Flow<Result<UserSession>> = flow {
        emit(Result.Loading)
        try {
            val response = api.register(
                RegisterRequestDto(
                    name = name,
                    lastName = lastname,
                    phone = phone,
                    email = email,
                    password = password,
                    confirmPassword = password,
                ),
            )
            val session = UserSession(
                token = response.data?.token ?: "",
                userId = response.data?.userId,
                role = response.data?.roles?.firstOrNull() ?: "external",
            )
            emit(Result.Success(session))
        } catch (e: Exception) {
            emit(Result.Error(e))
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

            // Always display Spanish message
            val message = "Contraseña actualizada exitosamente."
            emit(Result.Success(message))
        } catch (e: Exception) {
            val translated = when (e) {
                is java.io.IOException -> "Sin conexión a internet. Revisa tu red e inténtalo de nuevo."
                is IllegalStateException -> translateErrorMessage(e.message)
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
