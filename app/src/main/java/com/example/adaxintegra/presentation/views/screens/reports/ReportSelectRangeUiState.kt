package com.example.adaxintegra.presentation.views.screens.reports

import com.example.adaxintegra.domain.model.ProfileView

data class ReportSelectRangeUiState (
    val profile: ProfileView? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)
