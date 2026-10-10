package com.example.adaxintegra.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RegisterExpedientRequestDto(
    @SerializedName("profile") val profile: ProfileDto,
    @SerializedName("address") val address: AddressDto,
)

data class ProfileDto(
    @SerializedName("name") val name: String,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("email") val email: String,
    @SerializedName("birth_date") val birthDate: String? = null,
    @SerializedName("phone") val phone: String? = null,
)

data class AddressDto(
    @SerializedName("country") val country: String,
    @SerializedName("state") val state: String,
    @SerializedName("municipality") val municipality: String,
)

data class RegisterExpedientResponseDto(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: UserDataDto?,
)

data class UserDataDto(
    @SerializedName("user_id") val userId: String,
    @SerializedName("record_id") val recordId: String,
    @SerializedName("address_id") val addressId: String,
)
