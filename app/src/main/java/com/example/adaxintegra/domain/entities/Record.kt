package com.example.adaxintegra.domain.entities

// Represents a record summary independently of the API response
data class Record(
    val recordId: String,
    val userId: String,
    val name: String,
    val recordNumber: String?,
    val status: String,
    val activeCasesCount: Int,
    val updatedAt: String?,
)
