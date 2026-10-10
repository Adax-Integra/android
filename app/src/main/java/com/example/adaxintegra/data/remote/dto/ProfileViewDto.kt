package com.example.adaxintegra.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ProfileViewDto(
    @SerializedName(value = "user_id", alternate = ["userId"])
    val userId: String? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName(value = "last_name", alternate = ["lastName"])
    val lastName: String? = null,

    @SerializedName("email")
    val email: String? = null,

    @SerializedName(value = "birth_date", alternate = ["birthDate"])
    val birthDate: String? = null,

    @SerializedName("phone")
    val phone: String? = null,

    @SerializedName(value = "created_at", alternate = ["createdAt"])
    val createdAt: String? = null,
)
