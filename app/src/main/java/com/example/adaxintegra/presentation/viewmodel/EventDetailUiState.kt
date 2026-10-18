package com.example.adaxintegra.presentation.viewmodel

import com.example.adaxintegra.domain.model.Event

// E-01: state of the event detail screen
data class EventDetailUiState(
    val event: Event? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    // True when the event was disabled or already ended, retrying cannot bring it back
    val isUnavailable: Boolean = false,

    //disable events ui states
    val isDisabling: Boolean = false,
    val disableSuccess: Boolean = false,
    val disableError: Boolean = false,
)
