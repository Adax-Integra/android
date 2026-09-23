package com.example.adaxintegra.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LoginResponseDto(
    @SerializedName("token")
    val token: String,
    @SerializedName("user")
    val user: UserDto, // returning user data...
)

data class UserDto(
    val id: String,
    val name: String,
    val email: String,
)
