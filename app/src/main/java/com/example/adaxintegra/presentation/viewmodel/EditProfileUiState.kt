package com.example.adaxintegra.presentation.viewmodel

data class EditProfileUiState (
    val name: String,
    val lastName: String = "",
    val urgency: String = "",
    val country: String = "",
    val state: String = "",
    val municipality: String = "",
    val isEditing: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isRefreshing: Boolean = false,
)
