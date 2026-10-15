package com.example.adaxintegra.data.mapper

import com.example.adaxintegra.data.remote.dto.CollaboratorDetailDto
import com.example.adaxintegra.data.remote.dto.CollaboratorDto
import com.example.adaxintegra.data.remote.dto.CollaboratorListItemDto
import com.example.adaxintegra.data.remote.dto.CreateCollaboratorRequestDto
import com.example.adaxintegra.domain.model.Collaborator
import com.example.adaxintegra.domain.model.CollaboratorDetail
import com.example.adaxintegra.domain.model.CollaboratorSummary
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

// G-06: one item of the collaborators list -> domain model
fun CollaboratorListItemDto.toDomain(): CollaboratorSummary = CollaboratorSummary(
    userId = userId ?: "",
    name = name ?: "",
    lastName = lastName ?: "",
    email = if (email.isNullOrBlank()) null else email,
    isActive = isActive ?: false,
)

// G-10: collaborator detail -> domain model.
// Blank values become null so the screen can show a fallback text
fun CollaboratorDetailDto.toDomain(): CollaboratorDetail = CollaboratorDetail(
    userId = userId ?: "",
    name = name ?: "",
    lastName = lastName ?: "",
    email = if (email.isNullOrBlank()) null else email,
    phone = if (phone.isNullOrBlank()) null else phone,
    role = if (role.isNullOrBlank()) null else role,
    createdAt = if (createdAt.isNullOrBlank()) null else createdAt,
    isActive = isActive ?: false,
)
