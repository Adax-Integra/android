package com.example.adaxintegra.presentation.views.screens.profile

import com.example.adaxintegra.domain.model.ProfileView

// state of the screen
data class ProfileUiState(
    val profile: ProfileView? = null,
    val isLoading: Boolean = true,
    val error: String? = null,
    val isRefreshing: Boolean = false,
    val isDeactivating: Boolean = false,
    val deactivateError: String? = null,
    val isDeactivated: Boolean = false,
)
