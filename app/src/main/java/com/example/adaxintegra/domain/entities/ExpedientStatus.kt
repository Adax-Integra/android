package com.example.adaxintegra.domain.entities

import androidx.compose.ui.graphics.Color

enum class ExpedientStatus(
    val code: String,
    val label: String,
    val color: Color,
    val order: Int
) {
    NOT_STARTED("SIN_EMPEZAR", "Sin empezar", Color(0xFF9E9E9E), 0),
    UNDER_REVIEW("EN_REVISION", "En revisión", Color(0xFFD32F2F), 1),
    FOLLOWING("EN_SEGUIMIENTO", "En seguimiento", Color(0xFF7B1FA2), 2),
    COMPLETED("COMPLETADO", "Completado", Color(0xFF388E3C), 3);

    fun isTransitionAllowedTo(target: ExpedientStatus): Boolean {
        if (this == target) return true
        return when (this) {
            COMPLETED -> false
            FOLLOWING -> target == COMPLETED || target == UNDER_REVIEW
            UNDER_REVIEW -> target.order > this.order
            NOT_STARTED -> target.order > this.order
        }
    }

    companion object {
        fun fromLabel(label: String): ExpedientStatus = from(label)

        fun from(value: String?): ExpedientStatus {
            if (value.isNullOrBlank()) return NOT_STARTED
            val cleanValue = value.trim()

            return entries.firstOrNull { status ->
                status.code.equals(cleanValue, ignoreCase = true) ||
                    status.label.equals(cleanValue, ignoreCase = true) ||
                    status.name.equals(cleanValue, ignoreCase = true) ||
                    status.code.replace("_", " ").equals(cleanValue.replace("_", " "), ignoreCase = true) ||
                    status.name.replace("_", " ").equals(cleanValue.replace("_", " "), ignoreCase = true)
            } ?: NOT_STARTED
        }
    }
}
