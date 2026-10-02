package com.example.adaxintegra.presentation.viewmodel

import com.example.adaxintegra.domain.entities.Case

data class CasesFromUserUiState(
    val cases: List<Case> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)
