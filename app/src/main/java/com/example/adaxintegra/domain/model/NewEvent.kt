package com.example.adaxintegra.domain.model

data class NewEvent(
    val title: String,
    val startDate: String,
    val endDate: String,
    val startTime: String,
    val endTime: String,
    val modality: String,
    val location: String,
    val link: String?,
    val phone: String?,
    val description: String,
    val notes: String?,
)
