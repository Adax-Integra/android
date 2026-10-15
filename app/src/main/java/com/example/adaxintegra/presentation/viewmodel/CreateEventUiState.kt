package com.example.adaxintegra.presentation.viewmodel

data class CreateEventUiState(
    val title: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val startHour: String = "",
    val startMinute: String = "",
    val startAmPm: String = "a.m.",
    val endHour: String = "",
    val endMinute: String = "",
    val endAmPm: String = "a.m.",
    val modality: String = "Presencial",
    val location: String = "",
    val link: String = "",
    val phone: String = "",
    val description: String = "",
    val notes: String = "",
    val isSaving: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null,
)
