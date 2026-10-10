package com.example.adaxintegra.data.mapper

import com.example.adaxintegra.data.remote.dto.AddressDto
import com.example.adaxintegra.data.remote.dto.ProfileDto
import com.example.adaxintegra.data.remote.dto.RegisterExpedientRequestDto
import com.example.adaxintegra.domain.model.PersonalDataForm

fun PersonalDataForm.toDto(): RegisterExpedientRequestDto = RegisterExpedientRequestDto(
    profile = ProfileDto(
        name = this.name,
        lastName = this.lastName,
        email = this.email,
        birthDate = this.birthDate.ifBlank { null },
        phone = this.phone.ifBlank { null },
    ),
    address = AddressDto(
        addressLine1 = this.addressLine1,
        addressLine2 = this.addressLine2.ifBlank { null },
        neighborhood = this.neighborhood,
        zipCode = this.zipCode,
        country = this.country,
        state = this.state,
        city = this.city,
    ),
)
