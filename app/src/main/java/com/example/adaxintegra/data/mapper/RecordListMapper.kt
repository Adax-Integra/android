package com.example.adaxintegra.data.mapper

import com.example.adaxintegra.data.remote.dto.RecordListDataDto
import com.example.adaxintegra.data.remote.dto.RecordListItemDto
import com.example.adaxintegra.domain.entities.Record
import com.example.adaxintegra.domain.entities.RecordPage

// COnverts an API item into the domain model using listing
fun RecordListItemDto.toDomain(): Record = Record(
    recordId = recordId,
    userId = userId,
    name = name,
    recordNumber = recordNumber,
    status = status,
    activeCasesCount = activeCasesCount,
    updatedAt = updatedAt,
)

// Preserves the backend order and pagination without filtering locally
fun RecordListDataDto.toDomain(): RecordPage = RecordPage(
    records = records.map { it.toDomain() },
    total = total,
    page = page,
    limit = limit,
)
