package com.example.adaxintegra.presentation.views.designsystem.organisms.timeline

// possible status for timeline in CaseProgressScreen and the home progress bar
enum class TimelineStatus(val displayText: String) {
    Completed("Completado"),
    InProgress("En curso"),
    NotStarted("Pendiente"),
    ;

    companion object {
        // backend sends the status in english or spanish, anything unknown counts as pending
        fun from(raw: String): TimelineStatus =
            when (raw.trim().lowercase()) {
                "completed", "complete", "completado", "completada" -> Completed
                "in_progress", "in progress", "en curso", "en progreso" -> InProgress
                else -> NotStarted
            }
    }
}
