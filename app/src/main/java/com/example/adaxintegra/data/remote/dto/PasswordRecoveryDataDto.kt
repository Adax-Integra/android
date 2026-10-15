package com.example.adaxintegra.data.remote.dto

//all password recovery endpoints have the same response format
data class PasswordRecoveryDataDto(
    val message: String
)

data class PasswordRecoveryResponseDto(
    val success: Boolean,
    val data: PasswordRecoveryDataDto
)
