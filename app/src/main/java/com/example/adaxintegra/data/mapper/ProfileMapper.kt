package com.example.adaxintegra.data.mapper

import com.example.adaxintegra.data.remote.dto.ProfileViewDto
import com.example.adaxintegra.domain.model.ProfileView

// receives dto to convert into domain models
// extension function
fun ProfileViewDto.toDomain(): ProfileView = ProfileView(
    userId = userId ?: "",
    name = name ?: "",
    lastName = lastName ?: "",
    email = email ?: "",
    birthDate = birthDate ?: "",
    phone = phone ?: "",
    createdAt = createdAt ?: "",
)
