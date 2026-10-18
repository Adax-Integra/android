package com.example.adaxintegra.data.mapper

import com.example.adaxintegra.data.remote.dto.EventListItemDto
import com.example.adaxintegra.domain.model.Event
import java.time.OffsetDateTime
import java.time.format.DateTimeParseException
import java.time.Instant


// The backend sends the offset ("+00:00"), so OffsetDateTime reads it with or without miliseconds
private fun parseDate(value: String?): Instant? {
    if (value.isNullOrBlank()) return null
    return try {
        OffsetDateTime.parse(value).toInstant()
    } catch (e: DateTimeParseException) {
        null
    }
}

// E-01: one item of the events list -> domain model.
// Returns null when the id or the dates cannot be read, so that event is skipped
fun EventListItemDto.toDomain(): Event? {
    val eventId = id ?: return null
    val start = parseDate(startsAt) ?: return null
    val end = parseDate(endsAt) ?: return null

    return Event(
        id = eventId,
        title = title ?: "",
        description = description ?: "",
        notes = if (notes.isNullOrBlank()) null else notes,
        startsAt = start,
        endsAt = end,
        modality = modality ?: "",
        location = if (location.isNullOrBlank()) null else location,
        link = if (link.isNullOrBlank()) null else link,
        phone = if(phone.isNullOrBlank()) null else phone,
        imageUrl = if (imageUrl.isNullOrBlank()) null else imageUrl
    )
}
