package com.example.adaxintegra.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CreateEventResponseDto(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: EventDataDto?,
)

data class EventDataDto(
    @SerializedName("eventId") val eventId: String,
    @SerializedName("title") val title: String,
)
