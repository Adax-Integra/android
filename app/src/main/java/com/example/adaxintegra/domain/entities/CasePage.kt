package com.example.adaxintegra.domain.entities

// Represents a page of cases and its pagination information
data class CasePage(
    val cases: List<Case>,
    val total: Int,
    val page: Int,
    val limit: Int,
)
