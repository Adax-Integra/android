package com.example.adaxintegra.data.mapper

import com.example.adaxintegra.data.remote.dto.CaseDetailDto
import com.example.adaxintegra.data.remote.dto.CaseDetailHelpTypeDto
import com.example.adaxintegra.domain.model.CaseDetail
import com.example.adaxintegra.domain.model.CaseProgressStep
import com.example.adaxintegra.domain.model.Helps
import com.example.adaxintegra.domain.model.Violence
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

// converts the V-11 backend response into the case detail domain model
fun CaseDetailDto.toDomain(): CaseDetail {
    val userName =
        listOfNotNull(
            record?.user?.name,
            record?.user?.last_name,
        ).joinToString(" ").ifBlank { null }

    return CaseDetail(
        caseId = case_id,
        caseNumber = case_number,
        state = state,
        description = written_description,
        helpWanted = written_helps_wanted,
        hasLawyer = has_lawyer,
        recordId = record?.record_id ?: record_id,
        userName = userName,
        createdAt = parseCaseDetailDate(created_at),
        updatedAt = parseCaseDetailDate(updated_at),
        caseSteps =
            case_steps.map { step ->
                CaseProgressStep(
                    stepNumber = step.step_number,
                    status = step.status,
                    caseStepId = step.case_step_id
                )
            },
        violenceList =
            case_violence.mapNotNull { relation ->
                val violence = relation.violence_types

                if (violence?.description.isNullOrBlank()) {
                    null
                } else {
                    Violence(
                        id = violence.violence_id.orEmpty(),
                        type = violence.description.orEmpty(),
                        severity = violence.severity ?: 0,
                    )
                }
            },
        helpList =
            case_help.mapNotNull { relation ->
                val help = relation.help_types ?: CaseDetailHelpTypeDto(
                    help_id = relation.help_id,
                    description = relation.description
                )

                if (help.description.isNullOrBlank() && help.help_id.isNullOrBlank()) {
                    null
                } else {
                    Helps(
                        id = help.help_id.orEmpty(),
                        description = help.description.orEmpty(),
                    )
                }
            },
    )
}

// converts backend ISO dates into Date values for V-11
private fun parseCaseDetailDate(value: String?): Date? {
    if (value.isNullOrBlank()) return null

    val normalizedValue =
        value
            .trim()
            .replace(Regex("(\\.\\d{3})\\d+"), "\$1")

    val supportedFormats =
        listOf(
            "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
            "yyyy-MM-dd'T'HH:mm:ssXXX",
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            "yyyy-MM-dd'T'HH:mm:ss'Z'",
            "yyyy-MM-dd'T'HH:mm:ss.SSS",
            "yyyy-MM-dd'T'HH:mm:ss",
        )

    supportedFormats.forEach { format ->
        try {
            val parser =
                SimpleDateFormat(
                    format,
                    Locale.US,
                )

            parser.isLenient = false
            parser.timeZone = TimeZone.getTimeZone("UTC")

            return parser.parse(normalizedValue)
        } catch (_: ParseException) {
            // tries the next supported backend date format
        }
    }

    return null
}
