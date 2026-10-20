package com.example.adaxintegra.presentation.viewmodel

import com.example.adaxintegra.domain.model.Event

// E-01: state of the events carousel and the events screen
data class EventsUiState(
    val events: List<Event> = emptyList(),
    val isLoading: Boolean = false,
    // True while the user pulls the screen down to reload it
    val isRefreshing: Boolean = false,
    val error: String? = null,
    )
