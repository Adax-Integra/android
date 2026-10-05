package com.example.adaxintegra.presentation.viewmodel

// uistate that holds the form's values and per-field validation error messages
data class RegisterUiState(
    val name: String = "",
    val nameError: String? = null,
    val lastname: String = "",
    val lastnameError: String? = null,
    val countryCode: String = "+52",
    val phone: String = "",
    val phoneError: String? = null,
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val confirmPassword: String = "",
    val confirmPasswordError: String? = null,
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val showConfirmationDialog: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isRegisterSuccess: Boolean = false,
    val userRole: String? = null,
)
