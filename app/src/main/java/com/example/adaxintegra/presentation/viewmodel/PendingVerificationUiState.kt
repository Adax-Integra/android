package com.example.adaxintegra.presentation.viewmodel

data class PendingVerificationUiState(
    val email: String = "",
    val cooldownSeconds: Int = 0,
    val isLoading: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null,
)
