package com.example.adaxintegra.domain.model

data class NewEvent(
    val title: String,
    val startsAt: String,
    val endsAt: String,
    val modality: String,
    val location: String? = null,
    val link: String? = null,
    val phone: String? = null,
    val description: String,
    val notes: String? = null,
)
