package com.example.adaxintegra.presentation.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adaxintegra.domain.common.AuthError
import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.usecases.SendRecoveryEmailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

//since the backend responds success whether account exists or not,
//the true success will be asking for the code
sealed interface ForgotPasswordEvent {
    data class CodeRequested(val email: String) : ForgotPasswordEvent
}
@HiltViewModel
class ForgotPasswordViewModel
@Inject constructor(
    private val sendRecoveryEmailUseCase: SendRecoveryEmailUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ForgotPasswordUiState())
    val uiState: StateFlow<ForgotPasswordUiState> = _uiState.asStateFlow()

    //handle event of moving to code validation at success
    private val _events = Channel<ForgotPasswordEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun onEmailChange(email: String) {
        _uiState.update {
            it.copy(
                email = email,
                emailError = null,
                errorMessage = null,
            )
        }
    }

    fun onSendEmailClick() {
        val email = _uiState.value.email.trim()

        val emailError = when {
            email.isBlank() -> "Ingresa tu correo electrónico."

            //checks email format
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                "Ingresa un correo electrónico válido."
            else -> null
        }
        if (emailError != null) {
            _uiState.update { it.copy(
                emailError = emailError,
                errorMessage = null)
            }
            return
        }

        _uiState.update {
            it.copy(
                email = email,
                emailError = null,
                errorMessage = null,
                isLoading = true)
        }

        viewModelScope.launch {
            when (
                val result = sendRecoveryEmailUseCase(email)) {

                is Result.Success -> {
                    _uiState.update { it.copy(
                        isLoading = false)
                    }
                    _events.send(ForgotPasswordEvent.CodeRequested(email))
                }

                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.exception.toMessage())
                    }
                }
                is Result.Loading -> Unit
            }
        }
    }

    //add error messages for user
    //add possible solution for the user
    private fun Throwable.toMessage(): String = when (this) {
        AuthError.Network -> "Sin conexión. Revisa tu red e inténtalo de nuevo."
        AuthError.TooManyRequests -> "Demasiados intentos. Favor de esperar e intentar de nuevo más tarde."
        is AuthError.Validation -> "Revisa que el correo electrónico sea válido."
        else -> "No se pudo enviar el código. Favor de intentar de nuevo."
    }
}

