package com.example.adaxintegra.presentation.viewmodel

import com.example.adaxintegra.domain.entities.Case

// Holds the current state of the collaborator's case listing
data class CasesUiState(
    val cases: List<Case> = emptyList(),
    val search: String = "",
    val urgency: String = "",
    val total: Int = 0,
    val page: Int = 1,
    val limit: Int = 20,
    val isLoading: Boolean = false,
    val error: String? = null,
)
