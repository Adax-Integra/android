package com.example.adaxintegra.presentation.util

import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateFormatter {
    // ex. "22/09/2026, 13:45"
    fun dateTime(date: Date?): String {
        if (date == null) return ""
        return SimpleDateFormat("dd/MM/yyyy, HH:mm", Locale.forLanguageTag("es-MX")).format(date)
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
}
