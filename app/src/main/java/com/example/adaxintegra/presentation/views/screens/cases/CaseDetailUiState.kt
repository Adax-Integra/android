package com.example.adaxintegra.presentation.views.screens.cases

import com.example.adaxintegra.domain.model.CaseDetail
import com.example.adaxintegra.domain.model.CollaboratorSummary
import com.example.adaxintegra.domain.model.HelpType
import com.example.adaxintegra.domain.model.Violence

data class CaseDetailUiState(
    val case: CaseDetail? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isRefreshing: Boolean = false,
    val isHelpExpanded: Boolean = false,
    val availableHelps: List<HelpType> = emptyList(),
    val isAssigningHelps: Boolean = false,
    val assignHelpsError: String? = null,
    val availableViolences: List<Violence> = emptyList(),
    val isAssigningViolences: Boolean = false,
    val assignViolencesError: String? = null,
    val assignableCollaborators: List<CollaboratorSummary> = emptyList(),
    val isLoadingAssignableCollaborators: Boolean = false,
    val assignableCollaboratorsLoaded: Boolean = false,
    val assignableCollaboratorsError: String? = null,
    val isSavingCollaborators: Boolean = false,
    val saveCollaboratorsError: String? = null,
    val collaboratorsSaved: Boolean = false,
)
