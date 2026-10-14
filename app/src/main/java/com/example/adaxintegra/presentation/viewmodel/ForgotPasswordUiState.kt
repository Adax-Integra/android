package com.example.adaxintegra.presentation.viewmodel

data class ForgotPasswordUiState(
    val email: String = "",
    val emailError: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)
