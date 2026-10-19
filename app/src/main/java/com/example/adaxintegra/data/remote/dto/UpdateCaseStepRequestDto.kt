package com.example.adaxintegra.data.remote.dto

data class UpdateCaseStepRequestDto(
    val stepNumber: Int,
)

data class UpdateCaseStepResponseDto(
    val success: Boolean,
    val data: CaseDetailDto,
)
