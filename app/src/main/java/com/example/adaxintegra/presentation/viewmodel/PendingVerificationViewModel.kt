package com.example.adaxintegra.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.usecases.ResendVerificationEmailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PendingVerificationViewModel @Inject constructor(
    private val resendVerificationEmailUseCase: ResendVerificationEmailUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(PendingVerificationUiState())
    val uiState = _uiState.asStateFlow()

    fun setEmail(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    fun resendEmail() {
        val email = _uiState.value.email
        if (email.isBlank() || _uiState.value.cooldownSeconds > 0) return

        viewModelScope.launch {
            resendVerificationEmailUseCase(email).collect { result ->
                when (result) {
                    is Result.Loading -> _uiState.update {
                        it.copy(
                            isLoading = true,
                            errorMessage = null,
                            successMessage = null,
                        )
                    }

                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                successMessage = "Correo de verificación reenviado exitosamente.",
                                errorMessage = null,
                            )
                        }
                        startCooldownTimer()
                    }

                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = "Error al reenviar el correo. Inténtalo de nuevo.",
                                successMessage = null,
                            )
                        }
                    }
                }
            }
        }
    }

    private fun startCooldownTimer() {
        viewModelScope.launch {
            _uiState.update { it.copy(cooldownSeconds = 60) }
            while (_uiState.value.cooldownSeconds > 0) {
                delay(1000L)
                _uiState.update { it.copy(cooldownSeconds = it.cooldownSeconds - 1) }
            }
        }
    }
}
