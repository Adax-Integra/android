package com.example.adaxintegra.presentation.views.screens.admin

import com.example.adaxintegra.domain.model.CollaboratorDetail

// G-10: state of the collaborator detail screen
data class CollaboratorDetailUiState(
    val collaborator: CollaboratorDetail? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)
