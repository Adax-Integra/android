package com.example.adaxintegra.presentation.util

import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

// E-01: the backend sends the event dates in UTC, so they are shown in the time zone of the phone
object EventDateFormatter {
    private val locale = Locale.forLanguageTag("es-MX")
    private val shortMonthFormatter =
        DateTimeFormatter.ofPattern("MMM", locale)
    private val longDateFormatter = DateTimeFormatter.ofPattern("d 'de' MMMM, yyyy", locale)

    private val timeFormatter = DateTimeFormatter.ofPattern("h:mm a", locale)
    private fun local(instant: Instant): ZonedDateTime =
        instant.atZone(ZoneId.systemDefault())

    private val detailDateFormatter = DateTimeFormatter.ofPattern("EEEE d 'de' MMMM, yyyy", locale)

    private val dayMonthFormatter = DateTimeFormatter.ofPattern("d 'de' MMMM", locale)

    // ex. "25" for the date badge
    fun day(instant: Instant): String =
        local(instant).dayOfMonth.toString()

    // ex. "OCT" for the date badge, Java writes "oct." so the dot is removed
    fun shortMonth(instant: Instant): String =
        shortMonthFormatter.format(local(instant)).replace(".", "").uppercase(locale)

    // ex. "25 de octubre, 2026"
    fun longDate(instant: Instant): String =
        longDateFormatter.format(local(instant))

    // ex "6:00 p.m." for the events list.
    // The spaces become non-breaking, so a line never ends between "12:00" and "p. m."
    fun time(instant: Instant): String =
        timeFormatter.format(local(instant)).replace(' ', '\u00A0')

    // ex. "Sábado 25 de octubre, 2026" for the detail screen
    fun detailDate(instant: Instant): String = detailDateFormatter.format(local(instant)).replaceFirstChar { it.uppercase(locale) }

    // ex. "6:00 p.m. - 8:00 p.m.", events that end on another day
    fun timeRange(start: Instant, end: Instant): String {
        val sameDay = local(start).toLocalDate() == local(end).toLocalDate()
        if (sameDay) {
            return "${time(start)} - ${time(end)}"
        }
        return "${dayMonthFormatter.format(local(start))}, ${time(start)} - " +
            "${dayMonthFormatter.format(local(end))}, ${time(end)}"
    }
}
