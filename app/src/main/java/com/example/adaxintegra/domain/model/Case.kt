package com.example.adaxintegra.domain.model

//case returned by backend
data class Case(
        val caseId: String,
        val caseNumber: String,
        val state: String,
        val caseSteps: List<CaseProgressStep>
        )