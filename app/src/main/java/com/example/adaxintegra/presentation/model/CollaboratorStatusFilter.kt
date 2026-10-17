package com.example.adaxintegra.presentation.model

// G-06: options of the "Filtrar" dialog of the collaborators list
enum class CollaboratorStatusFilter(
    val label: String,
) {
    ALL("Todas"),
    ACTIVE("Activas"),
    INACTIVE("Inactivas"),
}
