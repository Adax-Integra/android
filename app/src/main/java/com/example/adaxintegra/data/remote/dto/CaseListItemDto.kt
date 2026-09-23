package com.example.adaxintegra.data.remote.dto

data class CaseListItemDto(
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
