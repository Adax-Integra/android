package com.example.adaxintegra.data.remote.dto

// case item returned by the user cases endpoints
data class UserCaseItemDto(
    val caseId: String?,
    val writtenDescription: String?,
    val writtenHelpsWanted: String?,
    val hasLawyer: Boolean?,
    val state: String?,
    val createdAt: String?,
    val updatedAt: String?,
    val helps: List<String>?,
    val violenceTypes: List<String>?,
)
