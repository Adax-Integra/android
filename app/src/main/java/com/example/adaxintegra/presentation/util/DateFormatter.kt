package com.example.adaxintegra.presentation.util

import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateFormatter {
    private val locale = Locale.forLanguageTag("es-MX")

    // ex. "22/09/2026, 13:45"
    fun dateTime(date: Date?): String {
        if (date == null) return ""
        return SimpleDateFormat("dd/MM/yyyy, HH:mm", locale).format(date)
    }

    // ex. "22/09/2026"
    fun day(date: Date?): String {
        if (date == null) return ""
        return SimpleDateFormat("dd/MM/yyyy", locale).format(date)
    }

    // ex. "13:45"
    fun time(date: Date?): String {
        if (date == null) return ""
        return SimpleDateFormat("HH:mm", locale).format(date)
    }

    // ex. "hace 2 horas"
    fun relative(date: Date?): String {
        if (date == null) return ""
        return DateUtils
            .getRelativeTimeSpanString(
                date.time,
                System.currentTimeMillis(),
                DateUtils.MINUTE_IN_MILLIS,
            ).toString()
    }

    // V-06: "Hoy", "Ayer" or the date, used to separate the days of a list
    fun dayLabel(date: Date?): String {
        if (date == null) return "Sin fecha"
        val yesterday = Calendar.getInstance()
        yesterday.add(Calendar.DAY_OF_YEAR, -1)
        return when (day(date)) {
            day(Date()) -> "Hoy"
            day(yesterday.time) -> "Ayer"
            else -> day(date)
        }
    }

    // V-06: true when both dates are on the same day
    fun isSameDay(first: Date?, second: Date?): Boolean = day(first) == day(second)
}
