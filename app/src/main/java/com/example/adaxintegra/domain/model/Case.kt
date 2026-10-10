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

//R-02: data the external user fills to register a new case
data class NewCase(
    val writtenDescription: String,
    val writtenHelpsWanted: String,
    val hasExternalSupport: Boolean,
)

//R-02: limits repeated by the backend validator (createCase.validator.js)
//Kept here so the form and the counter stop the text before sending it
const val MAX_CASE_DESCRIPTION_LENGTH = 5000
const val MAX_CASE_HELPS_WANTED_LENGTH = 400
