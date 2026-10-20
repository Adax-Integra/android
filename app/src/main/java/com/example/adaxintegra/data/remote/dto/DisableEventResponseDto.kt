package com.example.adaxintegra.data.remote.dto

import com.google.gson.annotations.SerializedName

data class DisableEventResponseDto(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: DisableEventDataDto?
)

data class DisableEventDataDto(
    @SerializedName("eventId")
    val eventId: String?,

    @SerializedName("disabledAt")
    val disabledAt: String?
)
