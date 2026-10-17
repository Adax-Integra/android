package com.example.adaxintegra.presentation.views.screens.cases

import com.example.adaxintegra.domain.model.CaseDetail
import com.example.adaxintegra.domain.model.HelpType

data class CaseDetailUiState(
    val case: CaseDetail? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isRefreshing: Boolean = false,
    val isHelpExpanded: Boolean = false,
    val availableHelps: List<HelpType> = emptyList(),
    val isAssigningHelps: Boolean = false,
    val assignHelpsError: String? = null,
)
