package com.example.adaxintegra.presentation.viewmodel

/**
 * // G-09-VerifyOTP: UI State holder for the OTP email verification screen.
 * Tracks the 6-digit code, cooldown timer, resend attempts, and operation states.
 */
data class PendingVerificationUiState(
    val email: String = "",
    val otpCode: String = "",
    val otpCodeError: String? = null,
    val cooldownSeconds: Int = 0,
    val resendAttempts: Int = 1,
    val maxResendAttempts: Int = 3,
    val isLoading: Boolean = false,
    val isVerifying: Boolean = false,
    val isVerifiedSuccess: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null,
)
