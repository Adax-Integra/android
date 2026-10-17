package com.example.adaxintegra.data.remote.dto

import com.google.gson.annotations.SerializedName

// G-08: response of PATCH api/profile/{userId}/deactivate
data class DeactivateAccountResponseDto(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("data")
    val data: DeactivateAccountDataDto?,
)

data class DeactivateAccountDataDto(
    @SerializedName("userId")
    val userId: String,
    @SerializedName("deactivatedAt")
    val deactivatedAt: String?,
)
