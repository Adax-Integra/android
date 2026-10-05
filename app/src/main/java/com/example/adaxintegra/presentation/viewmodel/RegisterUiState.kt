package com.example.adaxintegra.presentation.viewmodel

// uistate that holds the form's values
data class RegisterUiState(
    val name: String = "",
    val lastname: String = "",
    val countryCode: String = "+52",
    val phone: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val showConfirmationDialog: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isRegisterSuccess: Boolean = false,
    val userRole: String? = null,
)
