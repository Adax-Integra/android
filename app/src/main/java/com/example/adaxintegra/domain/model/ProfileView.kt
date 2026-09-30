package com.example.adaxintegra.domain.model

import java.util.Date

data class ProfileView (
    val userId: String,
    val name: String,
    val lastName: String,
    val email: String,
    val birthDate: String,
    val phone: String,
    val createdAt: String
)
