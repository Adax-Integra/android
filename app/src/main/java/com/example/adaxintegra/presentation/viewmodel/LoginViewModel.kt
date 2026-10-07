package com.example.adaxintegra.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.usecases.LoginUseCase
import com.example.adaxintegra.domain.usecases.ResendVerificationEmailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

// login's screen brain...
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val resendVerificationEmailUseCase: ResendVerificationEmailUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun onEmailChanger(email: String) {
        _uiState.update { it.copy(email = email, isEmailNotConfirmed = false, error = null) }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { it.copy(password = password, isEmailNotConfirmed = false, error = null) }
    }

    fun login() {
        viewModelScope.launch {
            loginUseCase(_uiState.value.email, _uiState.value.password).collect { result ->
                _uiState.update { state ->
                    when (result) {
                        is Result.Loading -> state.copy(
                            isLoading = true,
                            error = null,
                            isEmailNotConfirmed = false,
                            resendSuccessMessage = null,
                        )

                        is Result.Success -> state.copy(
                            isLoading = false,
                            isLoginSuccess = true,
                            userId = result.data.userId,
                            userRole = result.data.role,
                        )

                        is Result.Error -> {
                            val rawMsg = result.exception.message ?: result.exception.localizedMessage ?: ""
                            val isUnconfirmed = rawMsg.contains("email_not_confirmed", ignoreCase = true) ||
                                rawMsg.contains("Email not confirmed", ignoreCase = true) ||
                                rawMsg == "email_not_confirmed"

                            val isInvalidCredentials = rawMsg.contains("invalid login credentials", ignoreCase = true) ||
                                rawMsg.contains("invalid_credentials", ignoreCase = true)

                            val errorMessage = when {
                                isUnconfirmed -> "Debes confirmar tu correo electrónico antes de iniciar sesión."
                                isInvalidCredentials -> "Correo o contraseña incorrectos"
                                result.exception is HttpException -> when (result.exception.code()) {
                                    400 -> "Credenciales incorrectas"
                                    401 -> "Credenciales incorrectas"
                                    404 -> "Endpoint no encontrado (404)"
                                    else -> "Error en la solicitud (${result.exception.code()})"
                                }

                                result.exception is IOException -> "Error de conexión a internet"
                                else -> rawMsg.ifBlank { "Error desconocido al iniciar sesión" }
                            }

                            state.copy(
                                isLoading = false,
                                error = errorMessage,
                                isEmailNotConfirmed = isUnconfirmed || isInvalidCredentials,
                            )
                        }
                    }
                }
            }
        }
    }

    fun resendVerificationEmail() {
        val targetEmail = _uiState.value.email
        if (targetEmail.isBlank()) return

        viewModelScope.launch {
            resendVerificationEmailUseCase(targetEmail).collect { result ->
                _uiState.update { state ->
                    when (result) {
                        is Result.Loading -> state.copy(isResendingEmail = true)
                        is Result.Success -> state.copy(
                            isResendingEmail = false,
                            resendSuccessMessage = "Correo de verificación reenviado exitosamente.",
                        )

                        is Result.Error -> state.copy(
                            isResendingEmail = false,
                            error = "Error al reenviar el correo. Inténtalo de nuevo.",
                        )
                    }
                }
            }
        }
    }
}
