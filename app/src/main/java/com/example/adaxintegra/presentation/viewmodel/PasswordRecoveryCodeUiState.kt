package com.example.adaxintegra.presentation.viewmodel

data class PasswordRecoveryCodeUiState(
    val email: String = "",
    val otpCode: String = "",
    val cooldownSeconds: Int = 60,
    val sendCount: Int = 1,
    val maxSends: Int = 3,
    val isResending: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null,
)

//reset password screen receives these values
sealed interface PasswordRecoveryCodeEvent {
    data class CodeEntered(
        val email: String,
        val code: String
    ): PasswordRecoveryCodeEvent
}
