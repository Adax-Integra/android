package com.example.adaxintegra.domain.repository

import java.io.File

interface ReportRepository {
    
    suspend fun downloadReport(startDate: String, endDate: String): File
}
