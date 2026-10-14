package com.example.adaxintegra.presentation.viewmodel

import com.example.adaxintegra.domain.entities.Record

// NV-04: state of the internal home
data class InternalHomeUiState(
    // Name shown in the greeting, null while it loads or if it could not be read
    val userName: String? = null,
    val recentRecords: List<Record> = emptyList(),
    val isLoading: Boolean = false,
    // True while the user pulls the screen down to reload it
    val isRefreshing: Boolean = false,
    val error: String? = null,
)
