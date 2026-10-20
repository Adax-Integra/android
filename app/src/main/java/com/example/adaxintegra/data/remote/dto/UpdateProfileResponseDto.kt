package com.example.adaxintegra.data.remote.dto

data class UpdateProfileResponseDto(
    val success: Boolean,
    val data: UpdateProfileMessageDto
)

data class UpdateProfileMessageDto(
    val message: String
)
