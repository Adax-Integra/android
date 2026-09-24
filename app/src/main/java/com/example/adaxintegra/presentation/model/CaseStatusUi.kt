package com.example.adaxintegra.presentation.model

import androidx.compose.ui.graphics.Color

enum class CaseStatusUi(
    val value: String,
    val displayText: String,
    val color: Color,
) {
    IN_PROGRESS("in_progress", "En proceso", Color(0xFFFF9500)),
    CLOSED("closed", "Cerrado", Color(0xFF34C759)),
    PENDING("pending", "Pendiente", Color(0xFFFF3B30)),
    ;

    companion object {
        fun from(value: String?): CaseStatusUi? = entries.find { it.value == value }
    }
}
