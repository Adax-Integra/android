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

/**
 * Implementation of AuthRepository managing Supabase Auth and remote backend synchronization.
 * Handles login validation, user registration, OTP verification, and session management.
 */
@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi,
    private val supabase: SupabaseClient,
) : AuthRepository {
    // // G-09-Register: Active user session StateFlow held in memory
    private val _session = MutableStateFlow<UserSession?>(null)
    override val session = _session.asStateFlow()

    // // G-09-Register: Authenticates user credentials with Supabase Auth and enforces email confirmation
    override fun login(
        email: String,
        password: String,
    ): Flow<Result<UserSession>> = flow {
        emit(Result.Loading)

        try {
            // // G-09-Register: Attempt direct Supabase authentication
            try {
                supabase.auth.signInWith(Email) {
                    this.email = email
                    this.password = password
                }
                // // G-09-Register: Ensure the user's email has been confirmed before granting session access
                val currentUser = supabase.auth.currentUserOrNull()
                if (currentUser != null && currentUser.emailConfirmedAt == null) {
                    supabase.auth.signOut()
                    throw IllegalStateException("email_not_confirmed")
                }
            } catch (e: Exception) {
                val errorText = "${e.message} ${e.cause} ${e.javaClass.simpleName}".lowercase()
                if (errorText.contains("email_not_confirmed") ||
                    errorText.contains("email not confirmed") ||
                    e.message == "email_not_confirmed"
                ) {
                    throw IllegalStateException("email_not_confirmed")
                } else if (e is IllegalStateException && e.message == "email_not_confirmed") {
                    throw e
                } else {
                    // // G-09-Register: Re-throw Supabase authentication errors
                    throw e
                }
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
            if (exception.message == "email_not_confirmed" ||
                exception.message?.contains("email_not_confirmed", ignoreCase = true) == true
            ) {
                emit(Result.Error(IllegalStateException("email_not_confirmed")))
            } else {
                emit(Result.Error(exception))
            }
        }
    }

    override fun logout() {
        _session.value = null
    }

    // // G-09-Register: Creates a new user account with Supabase Auth and remote backend
    override fun register(
        name: String,
        lastname: String,
        phone: String,
        email: String,
        password: String,
    ): Flow<Result<UserSession>> = flow {
        emit(Result.Loading)
        try {
            // // G-09-Register: Register account in Supabase Auth
            supabase.auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }

            // Synchronize user account registration with Node backend
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

    // // G-09-VerifyOTP: Requests Supabase to resend the 6-digit email confirmation OTP
    override fun resendVerificationEmail(email: String): Flow<Result<Unit>> = flow {
        emit(Result.Loading)
        try {
            supabase.auth.resendEmail(OtpType.Email.SIGNUP, email)
            emit(Result.Success(Unit))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    // // G-09-VerifyOTP: Verifies the 6-digit OTP code entered by the user with Supabase Auth
    override fun verifyEmailCode(
        email: String,
        code: String,
    ): Flow<Result<UserSession>> = flow {
        emit(Result.Loading)
        try {
            // // G-09-VerifyOTP: Call Supabase Auth verifyEmailOtp API for SIGNUP type
            supabase.auth.verifyEmailOtp(
                type = OtpType.Email.SIGNUP,
                email = email,
                token = code,
            )

            // // G-09-VerifyOTP: Retrieve current user upon successful OTP verification
            val currentUser = supabase.auth.currentUserOrNull()
            val accessToken = supabase.auth.currentAccessTokenOrNull() ?: ""

            val userSession = UserSession(
                token = accessToken,
                userId = currentUser?.id,
                role = "external",
            )

            // // G-09-VerifyOTP: Publish new active session and emit success
            _session.value = userSession
            emit(Result.Success(userSession))
        } catch (e: Exception) {
            emit(Result.Error(Exception("Código de verificación incorrecto o expirado")))
        }
    }
}
