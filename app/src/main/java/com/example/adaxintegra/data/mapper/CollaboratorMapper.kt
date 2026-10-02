package com.example.adaxintegra.data.mapper

import com.example.adaxintegra.data.remote.dto.CollaboratorDto
import com.example.adaxintegra.data.remote.dto.CreateCollaboratorRequestDto
import com.example.adaxintegra.domain.model.Collaborator
import com.example.adaxintegra.domain.model.NewCollaborator

// G-03: form data -> request body
fun NewCollaborator.toRequestDto(): CreateCollaboratorRequestDto = CreateCollaboratorRequestDto(
    name = name,
    lastName = lastName,
    email = email,
    password = password,
    phone = phone,
)

// G-03: backend response -> domain model
fun CollaboratorDto.toDomain(): Collaborator = Collaborator(
    userId = userId ?: "",
    name = name ?: "",
    lastName = lastName ?: "",
    email = email ?: "",
    phone = phone,
)
