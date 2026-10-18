package com.example.adaxintegra.presentation.views.screens.admin

import com.example.adaxintegra.domain.model.ExternalUserSummary
import com.example.adaxintegra.presentation.model.CollaboratorStatusFilter

// G-16: state of the external users list.
// The search, the status filter and the pages are applied by the backend
data class ExternalUsersUiState(
    val users: List<ExternalUserSummary> = emptyList(),
    val searchQuery: String = "",
    val statusFilter: CollaboratorStatusFilter = CollaboratorStatusFilter.ALL,
    val page: Int = 1,
    val limit: Int = 10,
    val total: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null,
) {
    val hasStatusFilter: Boolean
        get() = statusFilter != CollaboratorStatusFilter.ALL

    val canGoBack: Boolean
        get() = page > 1

    // Example: page 2 with 10 per page shows up to 20, if total is 15 it is the last page
    val canGoNext: Boolean
        get() = error == null && page.toLong() * limit < total
}
