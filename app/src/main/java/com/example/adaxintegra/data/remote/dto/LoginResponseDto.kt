package com.example.adaxintegra.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LoginDataDto(
    @SerializedName("token")
    val token: String,
    @SerializedName("user_id")
    val userId: String? = null,
    @SerializedName("roles")
    val roles: List<String>? = null,
)

data class LoginResponseDto(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("data")
    val data: LoginDataDto,
)
