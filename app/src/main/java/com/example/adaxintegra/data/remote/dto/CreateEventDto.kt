package com.example.adaxintegra.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CreateEventRequestDto(
    @SerializedName("title")
    val title: String,
    @SerializedName("startsAt")
    val startsAt: String,
    @SerializedName("endsAt")
    val endsAt: String,
    @SerializedName("modality")
    val modality: String,
    @SerializedName("location")
    val location: String? = null,
    @SerializedName("link")
    val link: String? = null,
    @SerializedName("phone")
    val phone: String? = null,
    @SerializedName("description")
    val description: String,
    @SerializedName("notes")
    val notes: String? = null,
)

data class CreateEventResponseDataDto(
    @SerializedName(value = "eventId")
    val eventId: String?,
    @SerializedName("title")
    val title: String?,
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
    @SerializedName("description")
    val description: String?,
    @SerializedName("notes")
    val notes: String?,
)

data class CreateEventResponseDto(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("data")
    val data: CreateEventResponseDataDto?,
    @SerializedName("error")
    val error: String?,
)
