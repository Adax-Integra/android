package com.example.adaxintegra.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RegisterRequestDto(
    @SerializedName("name")
    val name: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("confirm_password")
    val confirmPassword: String,
)

data class RegisterDataDto(
    @SerializedName("userId")
    val userId: String? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("lastName")
    val lastName: String? = null,
    @SerializedName("email")
    val email: String? = null,
    @SerializedName("phone")
    val phone: String? = null,
    @SerializedName("token")
    val token: String? = null,
    @SerializedName("roles")
    val roles: List<String>? = null,
)

data class RegisterResponseDto(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("data")
    val data: RegisterDataDto?,
)
