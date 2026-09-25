package com.example.adaxintegra.domain.model

data class UserSession(
    val token: String,
    val userId: String? = null,
    val role: String? = null,
)
