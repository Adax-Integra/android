package com.example.adaxintegra.data.remote.dto

import com.google.gson.annotations.SerializedName

// G-09: boy for POSR api/auth/verify-email
// The response has the same shape as the login

data class VerifyEmailRequestDto(
    @SerializedName("email")
    val email: String,
    @SerializedName("code")
    val code: String,
)

// G-09: BODY FOR post api/auth/resend-verification
data class ResendVerificationRequestDto(
    @SerializedName("email")
    val email: String,
)

data class MessageDateDto(
    @SerializedName("message")
    val message: String? = null,
)

// Generic { success, data: { message } } resoponse
data class MessageResponseDto(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("data")
    val data: MessageDateDto? = null,
)


