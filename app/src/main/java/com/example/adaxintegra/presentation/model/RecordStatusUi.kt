package com.example.adaxintegra.presentation.model

// Maps backend status codes to labels used by cards and filters
enum class RecordStatusUi(
    val value: String,
    val displayText: String,
) {
    NOT_STARTED("SIN_EMPEZAR", "Sin empezar"),
    UNDER_REVIEW("EN_REVISION", "En revisión"),
    FOLLOW_UP("EN_SEGUIMIENTO", "En seguimiento"),
    COMPLETED("COMPLETADO", "Completado"), ;

    companion object {
        // Returns null if the status is not in this list.
        fun from(value: String?): RecordStatusUi? = entries.find { it.value == value }
    }
}
