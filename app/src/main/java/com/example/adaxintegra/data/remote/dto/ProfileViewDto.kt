package com.example.adaxintegra.data.remote.dto

import com.google.gson.annotations.SerializedName
import java.util.Date

data class ProfileViewDto(
    @SerializedName("user_id")
    val userId: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("last_name")
    val lastName: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("birth_date")
    val birthDate: String?,

    @SerializedName("phone")
    val phone: String?,

    @SerializedName("created_at")
    val createdAt: String,
)
