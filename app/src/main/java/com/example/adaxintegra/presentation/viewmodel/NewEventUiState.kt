package com.example.adaxintegra.presentation.viewmodel

import java.time.Instant

data class NewEventUiState(
    val title: String = "",
    val titleError: String? = null,
    val startDate: Instant? = null,
    val endDate: Instant? = null,
    val startDateError: String? = null,
    val endDateError: String? = null,
    val startTime: Instant? = null,
    val endTime: Instant? = null,
    val startTimeError: String? = null,
    val endTimeError: String? = null,
    val modality: String = "",
    val modalityError: String? = null,
    val location: String = "",
    val locationError: String? = null,
    val link: String = "",
    val linkError: String? = null,
    val phone: String = "",
    val phoneError: String? = null,
    val countryCode: String = "+52",
    val description: String = "",
    val descriptionError: String? = null,
    val notes: String = "",
    val notesError: String? = null,
    val hasAddedImage: Boolean = false,
    val hasAddedImageError: Boolean = false,
    val error: String? = null,
)
