package com.example.adaxintegra.data.remote.dto


data class ForgotPasswordDataDto(
    val message: String,
)

data class ForgotPasswordResponseDto(
    val success: Boolean,
    val data: ForgotPasswordDataDto,
)
