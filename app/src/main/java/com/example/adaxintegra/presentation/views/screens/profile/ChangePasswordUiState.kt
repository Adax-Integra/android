package com.example.adaxintegra.presentation.views.screens.profile

data class PasswordRules(
    val hasValidLength: Boolean = false,
    val hasUpperAndLower: Boolean = false,
    val hasNumber: Boolean = false,
    val isDifferentFromCurrent: Boolean = false,
    val doPasswordsMatch: Boolean = false,
    val hasNoSpecialAccents: Boolean = false,
) {
    val isValid: Boolean
        get() = hasValidLength &&
            hasUpperAndLower &&
            hasNumber &&
            isDifferentFromCurrent &&
            doPasswordsMatch &&
            hasNoSpecialAccents
}

data class ChangePasswordUiState(
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val isCurrentPasswordVisible: Boolean = false,
    val isNewPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val rules: PasswordRules = PasswordRules(),
)
