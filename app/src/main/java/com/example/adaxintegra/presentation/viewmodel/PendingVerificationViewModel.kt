package com.example.adaxintegra.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.usecases.ResendVerificationEmailUseCase
import com.example.adaxintegra.domain.usecases.VerifyEmailCodeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * // G-09-VerifyOTP: ViewModel managing OTP verification logic, 60s cooldown timer, and resend attempts.
 */
@HiltViewModel
class PendingVerificationViewModel @Inject constructor(
    private val resendVerificationEmailUseCase: ResendVerificationEmailUseCase,
    private val verifyEmailCodeUseCase: VerifyEmailCodeUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(PendingVerificationUiState())
    val uiState = _uiState.asStateFlow()

    // // G-09-VerifyOTP: Sets the target email address passed from registration
    fun setEmail(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    // // G-09-VerifyOTP: Updates the 6-digit OTP code value and clears errors
    fun onOtpCodeChanged(code: String) {
        if (code.length <= 6) {
            _uiState.update { it.copy(otpCode = code, otpCodeError = null, errorMessage = null) }
        }
    }

    // // G-09-VerifyOTP: Validates and executes 6-digit OTP code verification with Supabase
    fun verifyCode() {
        val state = _uiState.value
        if (state.otpCode.length < 6) {
            _uiState.update { it.copy(otpCodeError = "Ingresa el código completo de 6 dígitos") }
            return
        }

        viewModelScope.launch {
            verifyEmailCodeUseCase(state.email, state.otpCode).collect { result ->
                when (result) {
                    is Result.Loading -> _uiState.update {
                        it.copy(
                            isVerifying = true,
                            errorMessage = null,
                            otpCodeError = null,
                        )
                    }

                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                isVerifying = false,
                                isVerifiedSuccess = true,
                                successMessage = "Cuenta verificada exitosamente.",
                                errorMessage = null,
                            )
                        }
                    }

                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                isVerifying = false,
                                errorMessage = "Código incorrecto o expirado. Verifica el código e intenta de nuevo.",
                                otpCodeError = "Código inválido",
                            )
                        }
                    }
                }
            }
        }
    }

    // // G-09-VerifyOTP: Resends the 6-digit verification code to the target email
    fun resendEmail() {
        val state = _uiState.value
        if (state.email.isBlank() || state.cooldownSeconds > 0) return

        viewModelScope.launch {
            resendVerificationEmailUseCase(state.email).collect { result ->
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
                                resendAttempts = (it.resendAttempts + 1).coerceAtMost(it.maxResendAttempts),
                                successMessage = "Código de 6 dígitos reenviado a tu correo.",
                                errorMessage = null,
                            )
                        }
                        startCooldownTimer()
                    }

                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = "Error al reenviar el código. Inténtalo más tarde.",
                                successMessage = null,
                            )
                        }
                    }
                }
            }
        }
    }

    // // G-09-VerifyOTP: Starts 60-second countdown timer between resend requests
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
