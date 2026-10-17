package com.example.adaxintegra.presentation.views.screens.reports

import com.example.adaxintegra.domain.model.ProfileView
import java.io.File
import java.util.Calendar

data class ReportSelectRangeUiState(
    val startDate: Calendar? = null,
    val endDate: Calendar? = null,
    val startDateError: String? = null,
    val endDateError: String? = null,
    val selectedChip: String? = null,
    val profile: ProfileView? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val downloadedFile: File? = null,
    val downloadedSuccessfully: Boolean = false,
)
