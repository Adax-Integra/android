package com.example.adaxintegra.presentation.views.screens.admin

import com.example.adaxintegra.domain.model.CollaboratorDetail

// G-10: state of the collaborator detail screen
data class CollaboratorDetailUiState(
    val collaborator: CollaboratorDetail? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    // G-04: change of the account status
    val showStatusDialog: Boolean = false,
    val isUpdatingStatus: Boolean = false,
    val statusError: String? = null,
    // G-18 profile editing
    val showEditDialog: Boolean = false,
    val isUpdatingProfile: Boolean = false,
    val profileError: String? = null,
    val profileUpdated: Boolean = false,
)
