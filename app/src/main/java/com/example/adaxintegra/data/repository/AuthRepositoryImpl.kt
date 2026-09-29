package com.example.adaxintegra.data.repository

import com.example.adaxintegra.data.remote.api.AuthApi
import com.example.adaxintegra.data.remote.dto.LoginRequestDto
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
        } catch (exception: Exception) {
            emit(Result.Error(exception))
        }
    }

    override fun logout() {
        // Clears local state
        _session.value = null
    }
}
