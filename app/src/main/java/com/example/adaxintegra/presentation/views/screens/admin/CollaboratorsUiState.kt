package com.example.adaxintegra.presentation.views.screens.admin

import com.example.adaxintegra.domain.model.CollaboratorSummary
import com.example.adaxintegra.presentation.model.CollaboratorStatusFilter

// G-06: state of the collaborators list
data class CollaboratorsUiState(
    // Acceptance criteria: already sorted by creation date by the backend
    val collaborators: List<CollaboratorSummary> = emptyList(),
    val searchQuery: String = "",
    val statusFilter: CollaboratorStatusFilter = CollaboratorStatusFilter.ALL,
    val isLoading: Boolean = false,
    val error: String? = null,
) {
    // The search box and the status filter work over the loaded list
    val visibleCollaborators: List<CollaboratorSummary>
        get() {
            val query = searchQuery.trim()
            return collaborators.filter { collaborator ->
                matchesStatus(collaborator) && matchesSearch(collaborator, query)
            }
        }

    val hasStatusFilter: Boolean
        get() = statusFilter != CollaboratorStatusFilter.ALL

    private fun matchesStatus(collaborator: CollaboratorSummary): Boolean = when (statusFilter) {
        CollaboratorStatusFilter.ALL -> true
        CollaboratorStatusFilter.ACTIVE -> collaborator.isActive
        CollaboratorStatusFilter.INACTIVE -> !collaborator.isActive
    }

    // Search by full name or email
    private fun matchesSearch(collaborator: CollaboratorSummary, query: String): Boolean {
        if (query.isEmpty()) return true
        val fullName = "${collaborator.name} ${collaborator.lastName}"
        val email = collaborator.email ?: ""
        return fullName.contains(query, ignoreCase = true) ||
            email.contains(query, ignoreCase = true)
    }
}
