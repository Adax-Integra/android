package com.example.adaxintegra.presentation.viewmodel

data class ResetPasswordUiState(
    val email: String = "",
    val code: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null,
    val showFixCode: Boolean = false,
    val showNewCode: Boolean = false,
)

//event of password successfully changed leads to login
sealed interface ResetPasswordEvent {
    data object PasswordChanged : ResetPasswordEvent
}
