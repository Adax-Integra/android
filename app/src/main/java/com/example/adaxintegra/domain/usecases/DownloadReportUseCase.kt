package com.example.adaxintegra.domain.usecases

import com.example.adaxintegra.domain.repository.ReportRepository
import java.io.File
import javax.inject.Inject

class DownloadReportUseCase @Inject constructor(
    private val repository: ReportRepository,
) {
    suspend operator fun invoke(startDate: String, endDate: String): File {
        require(startDate.isNotBlank()) { "Selecciona una fecha de inicio" }
        require(endDate.isNotBlank()) { "Selecciona una fecha de fin" }

        return repository.downloadReport(startDate, endDate)
    }
}
