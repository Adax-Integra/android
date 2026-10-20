package com.example.adaxintegra.data.remote.dto

import com.google.gson.annotations.SerializedName

// E-01: one event of the list returned by GET /api/events
data class EventListItemDto(
    @SerializedName("id")
    val id: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("notes")
    val notes: String?,
    // UTC dates
    @SerializedName("startsAt")
    val startsAt: String?,
    @SerializedName("endsAt")
    val endsAt: String?,
    @SerializedName("modality")
    val modality: String?,
    @SerializedName("location")
    val location: String?,
    @SerializedName("link")
    val link: String?,
    @SerializedName("phone")
    val phone: String?,
    @SerializedName("imageUrl")
    val imageUrl: String?,
)

// E-01: response of the active events, already sorted by start date
data class EventListResponseDto(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("data")
    val data: List<EventListItemDto>?,
)
