package com.example.adaxintegra.domain.model

// G-16: data of one external account shown in the list
data class ExternalUserSummary(
    val userId: String,
    val name: String,
    val lastName: String,
    val email: String?,
    val createdAt: String?,
    val isActive: Boolean,
)

// G-16: one page of the list with the values needed to change pages
data class ExternalUserPage(
    val users: List<ExternalUserSummary>,
    val total: Int,
    val page: Int,
    val limit: Int,
)
