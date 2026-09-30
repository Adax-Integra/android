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
