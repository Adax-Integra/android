package com.example.adaxintegra.presentation.views.screens.admin

import com.example.adaxintegra.domain.model.ActivityLogEntry
import java.util.Date

// V-06: state of the activity log screen
data class ActivityLogUiState(
    // Entries of all the pages loaded so far with the newest first
    val entries: List<ActivityLogEntry> = emptyList(),
    val searchQuery: String = "",
    // Date and time filter, null means no limit
    val fromDate: Date? = null,
    val toDate: Date? = null,
    // Loading of the first page in full screen and of the next pages in the bottom of the list
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val hasNextPage: Boolean = false,
    val error: String? = null,
) {
    // The search box filters the entries that are already loaded
    val visibleEntries: List<ActivityLogEntry>
        get() {
            val query = searchQuery.trim()
            if (query.isEmpty()) return entries
            return entries.filter { entry ->
                entry.actorName.contains(query, ignoreCase = true) ||
                    entry.title.contains(query, ignoreCase = true) ||
                    entry.description.contains(query, ignoreCase = true)
            }
        }

    val hasDateFilter: Boolean
        get() = fromDate != null || toDate != null
}
