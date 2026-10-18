package com.example.adaxintegra.presentation.views.screens.admin

import com.example.adaxintegra.domain.model.CollaboratorSummary
import com.example.adaxintegra.presentation.model.CollaboratorStatusFilter

// G-06: the list is shown in pages of 10, the same size as the other lists
private const val PAGE_SIZE = 10

// G-06: state of the collaborators list
data class CollaboratorsUiState(
    // Acceptance criteria: already sorted by creation date by the backend
    val collaborators: List<CollaboratorSummary> = emptyList(),
    val searchQuery: String = "",
    val statusFilter: CollaboratorStatusFilter = CollaboratorStatusFilter.ALL,
    val page: Int = 1,
    val isLoading: Boolean = false,
    val error: String? = null,
) {
    // The search box and the status filter work over the loaded list
    val filteredCollaborators: List<CollaboratorSummary>
        get() {
            val query = searchQuery.trim()
            return collaborators.filter { collaborator ->
                matchesStatus(collaborator) && matchesSearch(collaborator, query)
            }
        }

    // Example: 23 collaborators are 3 pages, an empty list still has page 1
    val lastPage: Int
        get() {
            val pages = (filteredCollaborators.size + PAGE_SIZE - 1) / PAGE_SIZE
            return if (pages < 1) 1 else pages
        }

    // If the list gets shorter after a reload, the page stays inside the limits
    val currentPage: Int
        get() = page.coerceIn(1, lastPage)

    // Only the collaborators of the current page
    val visibleCollaborators: List<CollaboratorSummary>
        get() = filteredCollaborators
            .drop((currentPage - 1) * PAGE_SIZE)
            .take(PAGE_SIZE)

    val canGoBack: Boolean
        get() = currentPage > 1

    val canGoNext: Boolean
        get() = currentPage < lastPage

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
