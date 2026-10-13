package com.example.adaxintegra.presentation.viewmodel

import com.example.adaxintegra.domain.entities.Case

// NV-04: state of the internal home, only the stale cases section needs data for now
data class InternalHomeUiState(
    val staleCases: List<Case> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)
