package com.example.adaxintegra.data.dto

data class CaseDto (
    val case_id: String,
    val case_number: String,
    val state: String,
    val case_steps: List<CaseStepDto>
)