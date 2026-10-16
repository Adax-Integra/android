package com.example.adaxintegra.domain.model

data class EditableProfile(
    val userId: String,
    val name: String,
    val lastName: String,
    val phone: String,
    val address: EditableAddress? = null
)

data class EditableAddress(
    val country: String,
    val state: String,
    val municipality: String
)
