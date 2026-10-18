package com.example.adaxintegra.presentation.views.screens.admin

import com.example.adaxintegra.domain.model.ExternalUserProfile

data class ExternalUserDetailUiState(
    val profile: ExternalUserProfile? = null,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isEditing: Boolean = false,
    val showConfirmation: Boolean = false,
    val email: String = "",
    val phone: String = "",
    val country: String = "",
    val state: String = "",
    val municipality: String = "",
    val reason: String = "",
    val consentConfirmed: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null,
)
