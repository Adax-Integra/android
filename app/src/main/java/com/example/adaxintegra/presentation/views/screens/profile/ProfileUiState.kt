package com.example.adaxintegra.presentation.views.screens.profile

import com.example.adaxintegra.domain.model.ProfileView

//state of the screen
data class ProfileUiState (
    val profile: ProfileView? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)
