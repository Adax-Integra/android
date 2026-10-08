package com.example.adaxintegra.presentation.views.screens.reports

import com.example.adaxintegra.domain.model.ProfileView
import java.util.Calendar

data class ReportSelectRangeUiState (
    val startDate: Calendar? = null,
    val endDate: Calendar? = null,
    val startDateError: String? = null,
    val endDateError: String? = null,
    val profile: ProfileView? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)
