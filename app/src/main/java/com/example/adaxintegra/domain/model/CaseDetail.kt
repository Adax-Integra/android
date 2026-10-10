package com.example.adaxintegra.domain.model

import java.util.Date

// complete case information required by the V-11 detail screen
data class CaseDetail(
    val caseId: String,
    val caseNumber: String?,
    val state: String?,
    val description: String?,
    val helpWanted: String?,
    val hasLawyer: Boolean?,
    val recordId: String?,
    val userName: String?,
    val createdAt: Date?,
    val updatedAt: Date?,
    val violenceList: List<Violence>,
    val helpList: List<Helps>,
)
