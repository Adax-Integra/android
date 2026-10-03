package com.example.adaxintegra.data.remote.dto

// backend response used specifically by the V-11 case detail flow
data class CaseDetailResponseDto(
    val success: Boolean,
    val data: CaseDetailDto,
)

// complete case information returned by the case detail endpoint
data class CaseDetailDto(
    val case_id: String,
    val case_number: String? = null,
    val written_description: String? = null,
    val written_helps_wanted: String? = null,
    val has_lawyer: Boolean? = null,
    val state: String? = null,
    val record_id: String? = null,
    val created_at: String? = null,
    val updated_at: String? = null,
    val record: CaseDetailRecordDto? = null,
    val case_steps: List<CaseStepDto> = emptyList(),
    val case_violence: List<CaseDetailViolenceDto> = emptyList(),
    val case_help: List<CaseDetailHelpDto> = emptyList(),
)

// record information associated with the selected case
data class CaseDetailRecordDto(
    val record_id: String? = null,
    val user: CaseDetailUserDto? = null,
)

// user information nested inside the case record
data class CaseDetailUserDto(
    val user_id: String? = null,
    val name: String? = null,
    val last_name: String? = null,
)

// violence information returned by the case relation
data class CaseDetailViolenceDto(
    val violence_types: CaseDetailViolenceTypeDto? = null,
)

data class CaseDetailViolenceTypeDto(
    val violence_id: String? = null,
    val description: String? = null,
    val severity: Int? = null,
)

// requested help information returned by the case relation
data class CaseDetailHelpDto(
    val help_types: CaseDetailHelpTypeDto? = null,
)

data class CaseDetailHelpTypeDto(
    val help_id: String? = null,
    val description: String? = null,
)

// backend response returned after closing a case in V-11
data class CloseCaseResponseDto(
    val success: Boolean,
    val data: CloseCaseDataDto,
)

data class CloseCaseDataDto(
    val caseId: String,
    val state: String,
    val updatedAt: String?,
)
