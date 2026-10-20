package com.example.adaxintegra.data.mapper

import com.example.adaxintegra.data.remote.dto.CreateEventRequestDto
import com.example.adaxintegra.data.remote.dto.CreateEventResponseDataDto
import com.example.adaxintegra.data.remote.dto.EventListItemDto
import com.example.adaxintegra.domain.model.Event
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

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
        phone = if (phone.isNullOrBlank()) null else phone,
        imageUrl = if (imageUrl.isNullOrBlank()) null else imageUrl,
    )
}

fun Event.toCreateDto(): CreateEventRequestDto {
    val zone = ZoneId.systemDefault()
    val startIso = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(startsAt.atZone(zone))
    val endIso = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(endsAt.atZone(zone))

    return CreateEventRequestDto(
        title = title,
        startsAt = startIso,
        endsAt = endIso,
        modality = modality,
        location = location,
        link = link,
        phone = phone,
        description = description,
        notes = notes,
    )
}

fun CreateEventResponseDataDto.toDomain(): Event? {
    val id = eventId ?: return null
    val start = parseDate(startsAt) ?: return null
    val end = parseDate(endsAt) ?: return null

    return Event(
        id = id,
        title = title ?: "",
        description = description ?: "",
        notes = if (notes.isNullOrBlank()) null else notes,
        startsAt = start,
        endsAt = end,
        modality = modality ?: "",
        location = if (location.isNullOrBlank()) null else location,
        link = if (link.isNullOrBlank()) null else link,
        phone = if (phone.isNullOrBlank()) null else phone,
        imageUrl = null,
    )
}
