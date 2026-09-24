package com.example.adaxintegra.data.mapper

import com.example.adaxintegra.data.remote.dto.CollaboratorCaseListItemDto
import com.example.adaxintegra.domain.entities.Case

// Converts an API case into the model used by the collaborator's screen
fun CollaboratorCaseListItemDto.toDomain(): Case = Case(
    caseId = caseId,
    name = name,
    violenceTypes = violenceTypes,
    state = state,
    severity = severity,
    urgency = urgency,
    updatedAt = updatedAt,
)
