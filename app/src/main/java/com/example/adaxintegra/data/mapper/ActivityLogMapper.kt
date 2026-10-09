package com.example.adaxintegra.data.mapper

import com.example.adaxintegra.data.remote.dto.ActivityLogDataDto
import com.example.adaxintegra.data.remote.dto.ActivityLogItemDto
import com.example.adaxintegra.domain.model.ActivityLogEntry
import com.example.adaxintegra.domain.model.ActivityLogPage
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

// V-06: backend page -> domain page
fun ActivityLogDataDto.toDomain(): ActivityLogPage = ActivityLogPage(
    entries = logs.orEmpty().map { it.toDomain() },
    total = total ?: 0,
    page = page ?: 1,
    limit = limit ?: 10,
)

// V-06: backend entry -> domain entry
fun ActivityLogItemDto.toDomain(): ActivityLogEntry = ActivityLogEntry(
    logId = logId ?: "",
    actorName = actorName ?: "",
    action = action ?: "",
    title = actionLabel ?: "",
    description = description ?: "",
    reason = reason ?: "",
    createdAt = parseLogDate(createdAt),
)

// converts backend ISO dates to Date
private fun parseLogDate(value: String?): Date? {
    if (value.isNullOrBlank()) return null
    return try {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        parser.timeZone = TimeZone.getTimeZone("UTC")
        parser.parse(value)
    } catch (e: ParseException) {
        null
    }
}
