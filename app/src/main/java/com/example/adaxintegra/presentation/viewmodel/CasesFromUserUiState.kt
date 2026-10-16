package com.example.adaxintegra.presentation.viewmodel

import com.example.adaxintegra.domain.entities.Case
import com.example.adaxintegra.domain.entities.ExpedientStatus

data class CasesFromUserUiState(
    val cases: List<Case> = emptyList(),
    val currentStatus: ExpedientStatus = ExpedientStatus.FOLLOWING,
    val isLoading: Boolean = false,
    val isUpdatingStatus: Boolean = false,
    val error: String? = null,
)
