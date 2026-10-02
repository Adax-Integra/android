package com.example.adaxintegra.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RegisterRequestDto(

    @SerializedName("name")
    val name: String,
    @SerializedName("lastname")
    val lastname: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
)

data class RegisterResponseDto(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("data")
    val data: LoginDataDto?,
)
