package com.example.adaxintegra.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RegisterExpedientRequestDto(
    @SerializedName("profile") val profile: ProfileDto,
    @SerializedName("address") val address: AddressDto,
)

data class ProfileDto(
    @SerializedName("user_id") val userId: String? = null,
    @SerializedName("name") val name: String,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("email") val email: String,
    @SerializedName("birth_date") val birthDate: String? = null,
    @SerializedName("phone") val phone: String? = null,
    @SerializedName("created_at") val createdAt: String? = null,
)

data class AddressDto(
    @SerializedName("address_line_1") val addressLine1: String,
    @SerializedName("address_line_2") val addressLine2: String? = null,
    @SerializedName("neighborhood") val neighborhood: String,
    @SerializedName("zip_code") val zipCode: String,
    @SerializedName("country") val country: String,
    @SerializedName("state") val state: String,
    @SerializedName("city") val city: String,
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
