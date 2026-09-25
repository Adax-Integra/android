package com.example.adaxintegra.presentation.viewmodel

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isLoginSuccess: Boolean = false,
    val userId: String? = null,
    val userRole: String? = null,
)
