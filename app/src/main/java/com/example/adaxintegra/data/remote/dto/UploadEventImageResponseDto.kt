package com.example.adaxintegra.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UploadEventImageResponseDto(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: UploadImageItemDto?,
)

data class UploadImageItemDto(
    @SerializedName("eventId") val eventId: String?,
    @SerializedName("imageUrl") val imageUrl: String?,
)
