package com.example.adaxintegra.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.usecases.ResetPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel
@Inject constructor(
    private val resetPasswordUseCase: ResetPasswordUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ResetPasswordUiState())
    val uiState: StateFlow<ResetPasswordUiState> = _uiState.asStateFlow()

    fun onPasswordChange(password: String) {
        _uiState.update {
            it.copy(
                password = password,
                successMessage = null,
                errorMessage = null,
            )
        }
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        _uiState.update {
            it.copy(
                confirmPassword = confirmPassword,
                successMessage = null,
                errorMessage = null,
            )
        }
    }

    //clicking button uses token
    fun onResetPasswordClick(token: String) {
        val password = _uiState.value.password.trim()
        val confirmPassword = _uiState.value.confirmPassword.trim()

        //check if fields are empty or match
        if (password.isBlank()) {
            _uiState.update {
                it.copy(
                    successMessage = null,
                    errorMessage = "Ingresa tu nueva contraseña.",
                )
            }
            return
        }

        if (confirmPassword.isBlank()) {
            _uiState.update {
                it.copy(
                    successMessage = null,
                    errorMessage = "Confirma tu nueva contraseña.",
                )
            }
            return
        }

        if (password != confirmPassword) {
            _uiState.update {
                it.copy(
                    successMessage = null,
                    errorMessage = "Asegurar que las contraseñas coincidan.",
                )
            }
            return
        }
        _uiState.update {
            it.copy(
                successMessage = null,
                errorMessage = null,
                isLoading = true,
                )
        }

        viewModelScope.launch {
            val result = resetPasswordUseCase(
                token = token,
                newPassword = password,
                )

            when (result) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            successMessage = "Contraseña actualizada",
                            errorMessage = null,
                        )
                    }
                }

                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            successMessage = null,
                            errorMessage = result.exception.message
                                ?: "Ocurrió un error al actualizar la contraseña.",
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

