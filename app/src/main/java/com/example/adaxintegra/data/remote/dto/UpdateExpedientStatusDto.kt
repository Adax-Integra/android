package com.example.adaxintegra.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UpdateExpedientStatusRequestDto(
    @SerializedName("status") val status: String
)

data class UpdateExpedientStatusResponseDto(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String? = null
)

