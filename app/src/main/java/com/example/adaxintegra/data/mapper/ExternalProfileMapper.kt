package com.example.adaxintegra.data.mapper

import com.example.adaxintegra.data.remote.dto.ExternalProfileDataDto
import com.example.adaxintegra.data.remote.dto.ExternalEditableAddressDto
import com.example.adaxintegra.data.remote.dto.ExternalEditableProfileDto
import com.example.adaxintegra.data.remote.dto.UpdateExternalProfileRequestDto
import com.example.adaxintegra.domain.model.ExternalUserProfile
import com.example.adaxintegra.domain.model.ExternalUserProfileUpdate

fun ExternalProfileDataDto.toDomain(): ExternalUserProfile {
    val id = userId?.takeIf { it.isNotBlank() }
        ?: throw IllegalStateException("External profile has no user ID")

    return ExternalUserProfile(
        userId = id,
        verificationStatus = verificationStatus,
        name = profile?.name,
        lastName = profile?.lastName,
        email = profile?.email,
        birthDate = profile?.birthDate,
        phone = profile?.phone,
        country = address?.country,
        state = address?.state,
        municipality = address?.municipality,
    )
}

fun ExternalUserProfileUpdate.toRequestDto(): UpdateExternalProfileRequestDto {
    return UpdateExternalProfileRequestDto(
        profile = if (email != null || phone != null) {
            ExternalEditableProfileDto(email = email, phone = phone)
        } else null,
        address = if (country != null || state != null || municipality != null) {
            ExternalEditableAddressDto(
                country = country,
                state = state,
                municipality = municipality,
            )
        } else null,
        reason = reason,
        consentConfirmed = consentConfirmed,
    )
}
