package com.example.adaxintegra.data.mapper

import com.example.adaxintegra.data.remote.dto.CountryDto
import com.example.adaxintegra.domain.model.Country

// G-07: a country without its spanish name can not be shown in the dropdown
fun CountryDto.toDomain(): Country? {
    val name = nameEs?.takeIf { it.isNotBlank() } ?: return null

    return Country(
        name = name,
        phoneCode = phoneCode.orEmpty(),
        states = states.orEmpty().mapNotNull { state ->
            state.nameEs?.takeIf { it.isNotBlank() }
        },
    )
}
