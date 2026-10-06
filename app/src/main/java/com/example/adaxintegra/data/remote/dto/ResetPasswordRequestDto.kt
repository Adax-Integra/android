package com.example.adaxintegra.data.remote.dto

data class ResetPasswordRequestDto(
    val token: String,
    val newPassword: String,
)
