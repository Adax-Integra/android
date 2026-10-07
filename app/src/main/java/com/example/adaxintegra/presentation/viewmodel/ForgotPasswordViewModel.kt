package com.example.adaxintegra.presentation.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.usecases.SendRecoveryEmailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel
@Inject constructor(
    private val sendRecoveryEmailUseCase: SendRecoveryEmailUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ForgotPasswordUiState())
    val uiState: StateFlow<ForgotPasswordUiState> = _uiState.asStateFlow()

    fun onEmailChange(email: String) {
        _uiState.update {
            it.copy(
                email = email,
                emailError = null,
                successMessage = null,
                errorMessage = null,
            )
        }
    }

    fun onSendEmailClick() {
        val email = _uiState.value.email.trim()

        if (email.isBlank()) {
            _uiState.update {
                it.copy(
                    emailError = "Ingresa tu correo electrónico.",
                    successMessage = null,
                    errorMessage = null,
                )
            }
            return
        }

        //validate input is an email address
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _uiState.update {
                it.copy(
                    emailError = "Ingresa un correo electrónico válido.",
                    successMessage = null,
                    errorMessage = null,
                )
            }
            return
        }

        //when email is correctly input, button shows loading state
        _uiState.update {
            it.copy(
                email = email,
                emailError = null,
                successMessage = null,
                errorMessage = null,
                isLoading = true,
            )
        }

        viewModelScope.launch {
            val result = sendRecoveryEmailUseCase(email)

            when (result) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            successMessage = "Correo ha sido enviado.",
                            errorMessage = null,
                        )
                    }
                }

                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.exception.message
                                ?: "Ocurrió un error al enviar el correo.",
                        )
                    }
                }

                is Result.Loading -> {
                    //adding to avoid errors
                }
            }
        }
    }
}
