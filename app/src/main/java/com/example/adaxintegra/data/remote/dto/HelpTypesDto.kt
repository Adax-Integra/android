package com.example.adaxintegra.data.remote.dto

// R-08: response when getting all available helps
data class HelpTypesResponseDto(
    val success: Boolean,
    val data: List<HelpTypeItemDto>
)

data class HelpTypeItemDto(
    val helpId: String,
    val description: String
)
