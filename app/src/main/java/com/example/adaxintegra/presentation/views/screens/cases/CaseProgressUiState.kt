package com.example.adaxintegra.presentation.views.screens.cases

import com.example.adaxintegra.domain.model.Case

data class CaseProgressUiState(
    val case: Case? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)
