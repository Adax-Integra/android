package com.example.adaxintegra.data.remote.dto

// individual step
data class CaseStepDto(
    val step_number: Int,
    val status: String,
    val case_step_id: String? = null,
)
