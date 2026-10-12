package com.example.adaxintegra.domain.model

import java.util.Date

// V-06: one action registered in the activity log
data class ActivityLogEntry(
    val logId: String,
    val actorName: String,
    val action: String,
    val title: String,
    val description: String,
    val reason: String,
    val createdAt: Date?,
)

// V-06: one page of the activity log
data class ActivityLogPage(
    val entries: List<ActivityLogEntry>,
    val total: Int,
    val page: Int,
    val limit: Int,
) {
    // There are more entries when the loaded ones are less than the total
    val hasNextPage: Boolean
        get() = page * limit < total
}
