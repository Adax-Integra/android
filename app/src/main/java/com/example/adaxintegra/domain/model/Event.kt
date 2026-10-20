package com.example.adaxintegra.domain.model

import java.time.Instant

// E-01: active event shown in the homes, the list and the detail
data class Event(
    val id: String,
    val title: String,
    val description: String,
    val notes: String?,
    val startsAt: Instant,
    val endsAt: Instant,
    val modality: String,
    val location: String?,
    val link: String?,
    val phone: String?,
    val imageUrl: String?,
)

// E-01: which active events the backend returns
enum class EventScope(val query: String) {
    // Home carousel: active events of the current month
    MONTH("month"),
    // Events screen: every active event
    ALL("all"),
}
