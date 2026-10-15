package com.example.adaxintegra.domain.model

// G-03: data the admin fills to create a collaborator
data class NewCollaborator(
    val name: String,
    val lastName: String,
    val email: String,
    val password: String,
    val phone: String?,
)

// G-03: collaborator account already created
data class Collaborator(
    val userId: String,
    val name : String,
    val lastName: String,
    val email: String,
    val phone: String?,
)

// G-06: collaborator shown in the admin's list
data class CollaboratorSummary(
    val userId: String,
    val name: String,
    val lastName: String,
    val email: String?,
    val isActive: Boolean,
)

// G-10: detail of one collaborator that the admin consults
data class CollaboratorDetail(
    val userId: String,
    val name: String,
    val lastName: String,
    val email: String?,
    val phone: String?,
    val role: String?,
    val createdAt: String?,
    val isActive: Boolean,
)
