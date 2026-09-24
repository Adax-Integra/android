package com.example.adaxintegra.data.mapper

import com.example.adaxintegra.data.remote.dto.CaseDto
import com.example.adaxintegra.data.remote.dto.CaseListItemDto
import com.example.adaxintegra.data.remote.dto.CaseStepDto
import com.example.adaxintegra.domain.model.Case
import com.example.adaxintegra.domain.model.CaseProgressStep
import com.example.adaxintegra.domain.model.Helps
import com.example.adaxintegra.domain.model.Violence

// object from DTO converted to case or case progress step
fun CaseDto.toDomain(): Case = Case(
    caseId = case_id,
    caseNumber = case_number,
    state = state,
    caseSteps = case_steps.map { it.toDomain() },
    description = description,
    helpWanted = null,
    hasLawyer = null,
    violenceList = emptyList(),
    createdAt = null,
    helpList = emptyList(),
)

fun CaseListItemDto.toDomain(): Case = Case(
    caseId = caseId ?: "",
    caseNumber = null,
    state = state,
    caseSteps = emptyList(),
    description = writtenDescription,
    helpWanted = writtenHelpsWanted,
    hasLawyer = hasLawyer,
    violenceList = violenceTypes?.map { Violence(type = it, severity = 0) } ?: emptyList(),
    createdAt = null,
    helpList = helps?.map { Helps(description = it) } ?: emptyList(),
)

fun CaseStepDto.toDomain(): CaseProgressStep = CaseProgressStep(
    stepNumber = step_number,
    status = status,
)
