package com.example.adaxintegra.data.remote.dto

//receives email and code
data class ValidateCodeRequestDto (
    val email: String,
    val code: String
)
