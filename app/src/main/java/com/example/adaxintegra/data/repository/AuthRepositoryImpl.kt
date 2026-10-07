package com.example.adaxintegra.data.repository

import com.example.adaxintegra.data.remote.api.AuthApi
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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

// File that manages the network's logic, catches errors and allows the operation state
@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi,
    private val supabase: SupabaseClient,
) : AuthRepository {
    // Keeps the session in memory until logout or termination process
    private val _session = MutableStateFlow<UserSession?>(null)

    // Other components can observe the session without modifying it directly
    override val session = _session.asStateFlow()

    override fun login(
        email: String,
        password: String,
    ): Flow<Result<UserSession>> = flow {
        emit(Result.Loading)

        try {
            // Check email confirmation via Supabase if direct auth is used
            try {
                supabase.auth.signInWith(Email) {
                    this.email = email
                    this.password = password
                }
            } catch (e: Exception) {
                if (e.message?.contains("email_not_confirmed", ignoreCase = true) == true ||
                    e.message?.contains("Email not confirmed", ignoreCase = true) == true
                ) {
                    throw IllegalStateException("email_not_confirmed")
                }
            }

            val response = api.login(LoginRequestDto(email, password))

            if (!response.success) {
                throw IllegalStateException("No se pudo iniciar sesión")
            }

            val data = response.data

            // Return null if there is not a role
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
            // Preserves coroutine cancellation instead of reporting a login error
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
            // Attempt registration in Supabase Auth
            try {
                supabase.auth.signUpWith(Email) {
                    this.email = email
                    this.password = password
                }
            } catch (_: Exception) {
                // Backend or Supabase handling
            }

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

    override fun resendVerificationEmail(email: String): Flow<Result<Unit>> = flow {
        emit(Result.Loading)
        try {
            supabase.auth.resendEmail(OtpType.Email.SIGNUP, email)
            emit(Result.Success(Unit))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }
}
