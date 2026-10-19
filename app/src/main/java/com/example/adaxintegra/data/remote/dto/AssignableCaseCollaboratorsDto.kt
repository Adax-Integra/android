package com.example.adaxintegra.data.remote.dto

data class AssignableCaseCollaboratorsResponseDto(
    val success: Boolean,
    val data: List<AssignableCaseCollaboratorDto>,
)

data class AssignableCaseCollaboratorDto(
    val userId: String?,
    val name: String?,
    val lastName: String?,
)
