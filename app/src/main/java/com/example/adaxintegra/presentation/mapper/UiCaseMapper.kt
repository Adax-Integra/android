package com.example.adaxintegra.presentation.mapper

import com.example.adaxintegra.domain.model.Case
import com.example.adaxintegra.presentation.util.DateFormatter
import com.example.adaxintegra.domain.entities.Case as UiCase

fun Case.toUiEntity(): UiCase = UiCase(
    caseId = this.caseId,
    name = "Usuario", // Placeholder since the name isn't in the backend case model directly.
    violenceTypes = this.violenceList?.map { it.type } ?: emptyList(),
    state = this.state ?: "Sin estado",
    severity = null,
    urgency = "Sin evaluar",
    updatedAt = DateFormatter.dateTime(this.updatedAt).ifBlank { null }
)
