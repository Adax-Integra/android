package com.example.adaxintegra.domain.model

import java.util.Date

// case returned by backend
data class Case(
    val caseId: String,
    val caseNumber: String?,
    val state: String?,
    val caseSteps: List<CaseProgressStep>?,
    val description: String?,
    val helpWanted: String?,
    val hasLawyer: Boolean?,
    val violenceList: List<Violence>?,
    val helpList: List<Helps>?,
    val createdAt: Date?,
    val updatedAt: Date?,
)
