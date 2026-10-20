package com.example.adaxintegra.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ExternalProfileResponseDto(
    val success: Boolean,
    val data: ExternalProfileDataDto?,
)

data class ExternalProfileDataDto(
    @SerializedName("user_id") val userId: String?,
    @SerializedName("verification_status") val verificationStatus: String?,
    val profile: ExternalProfileInfoDto?,
    val address: ExternalProfileAddressDto?,
)

data class ExternalProfileInfoDto(
    val name: String?,
    @SerializedName("last_name") val lastName: String?,
    val email: String?,
    @SerializedName("birth_date") val birthDate: String?,
    val phone: String?,
)

data class ExternalProfileAddressDto(
    val country: String?,
    val state: String?,
    val municipality: String?,
)

data class UpdateExternalProfileRequestDto(
    val profile: ExternalEditableProfileDto? = null,
    val address: ExternalEditableAddressDto? = null,
    val reason: String,
    val consentConfirmed: Boolean,
)

data class ExternalEditableProfileDto(
    val email: String? = null,
    val phone: String? = null,
)

data class ExternalEditableAddressDto(
    val country: String? = null,
    val state: String? = null,
    val municipality: String? = null,
)
