package com.example.adaxintegra.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adaxintegra.domain.common.AuthError
import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.usecases.ResetPasswordUseCase
import com.example.adaxintegra.presentation.mapper.toAuthMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel
@Inject constructor(
    private val resetPasswordUseCase: ResetPasswordUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ResetPasswordUiState())
    val uiState: StateFlow<ResetPasswordUiState> = _uiState.asStateFlow()

    private val _events = Channel<ResetPasswordEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    //receive data from previous step
    fun setData(email: String, code: String) {
        _uiState.update {
            it.copy(
                email = email,
                code = code
            )
        }
    }

    fun onPasswordChange(password: String) {
        _uiState.update {
            it.copy(
                password = password,
                errorMessage = null,
            )
        }
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        _uiState.update {
            it.copy(
                confirmPassword = confirmPassword,
                errorMessage = null,
            )
        }
    }

    fun onResetPasswordClick() {
        val state = _uiState.value
        if (state.isLoading) return

        //password field validation
        val error = when {
            state.password.isBlank() -> "Favor de ingresar la nueva contraseña."
            state.password.length < 8 -> "La contraseña debe tener al menos 8 carácteres."
            state.confirmPassword.isBlank() -> "Favor de confirmar tu nueva contraseña."
            state.password != state.confirmPassword -> "Las contraseñas deben coincidir."
            else -> null
        }

        if (error != null) {
            _uiState.update { it.copy(errorMessage = error) }
            return
        }

        _uiState.update {
            it.copy(
                showFixCode = false,
                showNewCode = false,
                errorMessage = null,
                isLoading = true,
            )
        }

        viewModelScope.launch {
            val result = resetPasswordUseCase(
                email = state.email,
                code = state.code,
                newPassword = state.password,
            )

            when (result) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            successMessage = "Contraseña actualizada, redirigiendo a login...",
                            errorMessage = null,
                        )
                    }
                    //let user read message before redirecting
                    delay(2000)
                    _events.send(ResetPasswordEvent.PasswordChanged)
                }

                is Result.Error -> handleError(result.exception)

                is Result.Loading -> Unit
            }
        }
    }

    private fun handleError(exception: Throwable) {
        _uiState.update {
            it.copy(
                isLoading = false,
                errorMessage = exception.toAuthMessage(),
                showFixCode = exception == AuthError.CodeInvalid,
                showNewCode = exception == AuthError.CodeExpired ||
                    exception == AuthError.TooManyAttempts,
            )
        }
    }
}
