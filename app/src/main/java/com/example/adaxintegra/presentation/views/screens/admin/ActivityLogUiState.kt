package com.example.adaxintegra.presentation.views.screens.admin

import com.example.adaxintegra.domain.model.ActivityLogEntry
import java.util.Date

// V-06: state of the activity log screen
data class ActivityLogUiState(
    // Entries of the current page with the newest first
    val entries: List<ActivityLogEntry> = emptyList(),
    val searchQuery: String = "",
    // Date and time filter, null means no limit
    val fromDate: Date? = null,
    val toDate: Date? = null,
    // V-06: the backend sends the log in pages of 10
    val page: Int = 1,
    val total: Int = 0,
    val limit: Int = 10,
    val isLoading: Boolean = false,
    val error: String? = null,
) {
    // The search box filters the entries of the current page
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

    // Example: 23 entries are 3 pages, an empty log still has page 1
    val lastPage: Int
        get() {
            if (limit <= 0 || total <= 0) return 1
            return (total + limit - 1) / limit
        }

    val canGoBack: Boolean
        get() = page > 1

    val canGoNext: Boolean
        get() = page < lastPage

    val hasDateFilter: Boolean
        get() = fromDate != null || toDate != null
}
