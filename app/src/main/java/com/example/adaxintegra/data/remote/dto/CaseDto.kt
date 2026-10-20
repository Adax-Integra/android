package com.example.adaxintegra.data.remote.dto

data class CaseDto(
    val case_id: String,
    val case_number: String,
    val state: String?,
    val case_steps: List<CaseStepDto>,
    val description: String? = null,
    val written_description: String? = null,
    val written_helps_wanted: String? = null,
    val has_lawyer: Boolean? = null,
)
