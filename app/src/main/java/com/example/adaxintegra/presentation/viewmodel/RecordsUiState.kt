package com.example.adaxintegra.presentation.viewmodel

import com.example.adaxintegra.domain.entities.Record

// HOlds the listing data, applied filters, and current request state
data class RecordsUiState(
    val records: List<Record> = emptyList(),
    val search: String = "",
    // null includes records both with and without open cases
    val hasOpenCases: Boolean? = null,
    // null includes all status, other values use the backend codes.
    val status: String? = null,
    val total: Int = 0,
    val page: Int = 1,
    val limit: Int = 10,
    val isLoading: Boolean = false,
    val error: String? = null,
)
