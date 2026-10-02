package com.example.adaxintegra.domain.model

data class ProfileView (
    val userId: String,
    val name: String,
    val lastName: String,
    val email: String,
    val birthDate: String?,
    val phone: String?,
    val createdAt: String
)
