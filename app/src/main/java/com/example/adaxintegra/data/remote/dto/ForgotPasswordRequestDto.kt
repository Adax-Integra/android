package com.example.adaxintegra.data.remote.dto

//only the email is required for the first step
data class ForgotPasswordRequestDto(
    val email: String,
)
