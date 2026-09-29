package com.example.adaxintegra.data.mapper

import com.example.adaxintegra.data.remote.dto.ProfileDto

//receives dto to convert into domain models
//extension function
fun ProfileDto.toDomain(): Profile = Profile(
    userId = userId,
    name = name,
    lastName = lastName,
    email = email,
    birthDate = birthDate,
    phone = phone,
    createdAt = createdAt
)
