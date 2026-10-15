package com.example.adaxintegra.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CreateEventRequestDto(
    @SerializedName("title") val title: String,
    @SerializedName("startDate") val startDate: String,
    @SerializedName("endDate") val endDate: String,
    @SerializedName("startTime") val startTime: String,
    @SerializedName("endTime") val endTime: String,
    @SerializedName("modality") val modality: String,
    @SerializedName("location") val location: String,
    @SerializedName("link") val link: String?,
    @SerializedName("phone") val phone: String?,
    @SerializedName("description") val description: String,
    @SerializedName("notes") val notes: String?,
)
