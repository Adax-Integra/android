package com.example.adaxintegra.domain.entities

// Represents the information displayed on a case card.
data class Case(
    val caseId: String,
    val name: String,
    val violenceTypes: List<String>,
    val state: String,
    // Null means the case has not been assessed yet.
    val severity: Int?,
    val urgency: String,
    val updatedAt: String?,
    val caseNumber: String? = null,
    val internsAssigned: List<String> = emptyList(),
) {
    companion object {
        // Fictional data for previews and UI development, as in the lab.
        // These scores are examples, not a severity policy for violence types.
        // Real severity and urgency will come from the backend.
        fun getMockData(): List<Case> = listOf(
            Case(
                caseId = "13221-4231",
                name = "María García López",
                violenceTypes = listOf("Violencia familiar", "Violencia económica"),
                state = "Abierto",
                severity = 9,
                urgency = "Alta",
                updatedAt = "2026-09-21T10:00:00",
                caseNumber = "CAS-26-0216",
                internsAssigned = listOf(
                    "María Gómez",
                    "Lucía Martínez",
                    "Amanda Luna"
                )
            ),
            Case(
                caseId = "13221-4232",
                name = "Sofía Méndez Duarte",
                violenceTypes = listOf("Violencia psicológica"),
                state = "Abierto",
                severity = 5,
                urgency = "Media",
                updatedAt = "2026-09-20T12:30:00",
                caseNumber = "CAS-26-0001",
                internsAssigned = emptyList()
            ),
            Case(
                caseId = "13221-4233",
                name = "Laura Torres Ortiz",
                violenceTypes = listOf("Violencia económica"),
                state = "Abierto",
                severity = 2,
                urgency = "Baja",
                updatedAt = "2026-09-18T09:15:00",
                caseNumber = "CAS-26-5100",
                internsAssigned = listOf(
                    "Carla Rodríguez",
                    "Ana García"
                )

            ),
            Case(
                caseId = "13221-4234",
                name = "Ana Martínez Ruiz",
                violenceTypes = emptyList(),
                state = "Cerrado",
                severity = null,
                urgency = "Sin evaluar",
                updatedAt = null,
                caseNumber = "CAS-27-0516",
                internsAssigned = listOf(
                    "Dana Ortíz"
                )
            ),
        )
    }
}
