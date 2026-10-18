package com.example.adaxintegra.presentation.viewmodel

import com.example.adaxintegra.domain.model.Event

// E-01: state of the event detail screen
data class EventDetailUiState(
    val event: Event? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)
