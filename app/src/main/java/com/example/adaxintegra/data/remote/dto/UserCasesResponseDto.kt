package com.example.adaxintegra.data.remote.dto

data class UserCasesResponseDto(
    val success: Boolean,
    val data: List<CaseListItemDto>,
)
