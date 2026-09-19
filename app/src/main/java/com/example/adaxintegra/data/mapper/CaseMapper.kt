package com.example.adaxintegra.data.mapper

import com.example.adaxintegra.data.remote.dto.CaseDto
import com.example.adaxintegra.data.remote.dto.CaseStepDto
import com.example.adaxintegra.domain.model.Case
import com.example.adaxintegra.domain.model.CaseProgressStep

//object from DTO converted to case or case progress step
fun CaseDto.toDomain(): Case {
    return Case(
        caseId = case_id,
        caseNumber = case_number,
        state = state,
        caseSteps = case_steps.map { it.toDomain() }
    )
}

fun CaseStepDto.toDomain(): CaseProgressStep {
    return CaseProgressStep(
        stepNumber = step_number,
        status = status
    )
}