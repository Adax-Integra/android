package com.example.adaxintegra.presentation.views.screens.cases

import com.example.adaxintegra.domain.model.CaseDetail

data class CaseDetailUiState(
    val case: CaseDetail? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)
