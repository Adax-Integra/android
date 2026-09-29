package com.example.adaxintegra.presentation.views.screens.cases

import com.example.adaxintegra.domain.model.Case

// state of the external user's case list (V-04)
data class ExternalCasesUiState(
    val cases: List<Case> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
)
