package com.example.adaxintegra.domain.entities

// Contains one page and the total number of matching records
data class RecordPage(
    val records: List<Record>,
    val total: Int,
    val page: Int,
    val limit: Int,
)
