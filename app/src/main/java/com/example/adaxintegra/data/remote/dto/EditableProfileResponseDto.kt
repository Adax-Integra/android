package com.example.adaxintegra.data.remote.dto

import com.google.gson.annotations.SerializedName

data class EditableProfileResponseDto(
    val success: Boolean,
    val data: EditableProfileDto
)

data class EditableProfileDto(
    @SerializedName("user_id")
    val userId: String,

    val name: String,

    @SerializedName("last_name")
    val lastName: String,

    val phone: String,
    val address: EditableAddressDto? = null
)

//fields could be null
data class EditableAddressDto(
    val country: String?,
    val state: String?,
    val municipality: String?
)
