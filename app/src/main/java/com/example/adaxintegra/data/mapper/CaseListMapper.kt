package com.example.adaxintegra.data.mapper

import com.example.adaxintegra.data.remote.dto.CollaboratorCaseListItemDto
import com.example.adaxintegra.domain.entities.Case

// Converts an API case into the model used by the collaborator's screen
fun CollaboratorCaseListItemDto.toDomain(): Case = Case(
    caseId = caseId,
    name = name,
    violenceTypes = violenceTypes,
    state = mapStateToSpanish(state),
    severity = severity,
    urgency = urgency,
    updatedAt = updatedAt,
    caseNumber = caseNumber,
    internsAssigned = internsAssigned ?: emptyList(),
)

private fun mapStateToSpanish(rawState: String?): String {
    return when (rawState?.trim()?.lowercase()) {
        "open", "abierto" -> "Abierto"
        "closed", "cerrado" -> "Cerrado"
        else -> rawState ?: "Sin estado"
    }
}
