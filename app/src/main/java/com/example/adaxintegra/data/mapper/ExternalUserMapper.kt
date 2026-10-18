package com.example.adaxintegra.data.mapper

import com.example.adaxintegra.data.remote.dto.ExternalUserListDataDto
import com.example.adaxintegra.data.remote.dto.ExternalUserSummaryDto
import com.example.adaxintegra.domain.model.ExternalUserPage
import com.example.adaxintegra.domain.model.ExternalUserSummary

// G-16: empty values are replaced so the list can always be drawn
fun ExternalUserSummaryDto.toDomain(): ExternalUserSummary =
    ExternalUserSummary(
        userId = userId.orEmpty(),
        name = name.orEmpty(),
        lastName = lastName.orEmpty(),
        email = email,
        createdAt = createdAt,
        isActive = isActive ?: false,
    )

fun ExternalUserListDataDto.toDomain(): ExternalUserPage =
    ExternalUserPage(
        users = users.orEmpty().map { it.toDomain() },
        total = total ?: 0,
        page = page ?: 1,
        limit = limit ?: 10,
    )
