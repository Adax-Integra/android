package com.example.adaxintegra.data.remote.dto

data class CaseDto(
    val case_id: String,
    val case_number: String,
    val case_steps: List<CaseStepDto>,
    val description: String,
)
