package com.example.adaxintegra.data.mapper

import com.example.adaxintegra.data.remote.dto.EditableProfileDto
import com.example.adaxintegra.domain.model.EditableAddress
import com.example.adaxintegra.domain.model.EditableProfile

fun EditableProfileDto.toDomain(): EditableProfile =
    EditableProfile(
        userId = userId,
        name = name,
        lastName = lastName,
        phone = phone,
        address = address?.let {
            EditableAddress(
                country = it.country.orEmpty(),
                state = it.state.orEmpty(),
                municipality = it.municipality.orEmpty()
            )
        }
    )
