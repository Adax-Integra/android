package com.example.adaxintegra.data.remote.dto

data class ResetPasswordRequestDto(
    val email: String,
    val code: String,
    val newPassword: String,
)
