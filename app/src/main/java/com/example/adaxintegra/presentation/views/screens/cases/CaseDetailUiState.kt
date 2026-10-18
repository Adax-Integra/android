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
    // R-10: Assignation state of interns in cases
    val assignableCollaborators: List<CollaboratorSummary> = emptyList(),
    val assignedCollaboratorIds: Set<String> = emptySet(),
    val isAssigningCollaborators: Boolean = false,
    val assignCollaboratorsError: String? = null,
)
