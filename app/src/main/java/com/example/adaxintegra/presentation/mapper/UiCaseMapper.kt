package com.example.adaxintegra.presentation.mapper

import com.example.adaxintegra.domain.model.Case
import com.example.adaxintegra.presentation.util.DateFormatter
import com.example.adaxintegra.domain.entities.Case as UiCase

fun Case.toUiEntity(): UiCase = UiCase(
    caseId = this.caseId,
    name = "Usuario", // Placeholder since the name isn't in the backend case model directly.
    violenceTypes = this.violenceList?.map { it.type } ?: emptyList(),
    state = mapStateToSpanish(this.state),
    severity = null,
    urgency = "Sin evaluar",
    updatedAt = DateFormatter.dateTime(this.updatedAt).ifBlank { null },
    caseNumber = this.caseNumber,
    internsAssigned = emptyList(),
)

private fun mapStateToSpanish(rawState: String?): String {
    return when (rawState?.trim()?.lowercase()) {
        "open", "abierto" -> "Abierto"
        "closed", "cerrado" -> "Cerrado"
        else -> rawState ?: "Sin estado"
    }
}
