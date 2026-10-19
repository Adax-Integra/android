package com.example.adaxintegra.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UploadEventImageResponseDataDto(
    @SerializedName(value = "eventId")
    val eventId: String?,
    @SerializedName(value = "imageUrl")
    val imageUrl: String?,
)

data class UploadEventImageResponseDto(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("data")
    val data: UploadEventImageResponseDataDto?,
    @SerializedName("error")
    val error: String?,
)
