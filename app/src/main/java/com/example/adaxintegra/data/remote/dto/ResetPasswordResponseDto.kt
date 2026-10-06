package com.example.adaxintegra.data.remote.dto

data class ResetPasswordData(
    val message: String,
)

data class ResetPasswordResponseDto(
    val success: Boolean,
    val dataval : ResetPasswordData,
)
