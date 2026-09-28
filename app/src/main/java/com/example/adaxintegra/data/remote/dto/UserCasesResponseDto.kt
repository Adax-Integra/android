package com.example.adaxintegra.data.remote.dto

// response of the user cases endpoints (V-04 and V-10): plain list, no pagination
data class UserCasesResponseDto(
    val success: Boolean,
    val data: List<UserCaseItemDto>,
)
