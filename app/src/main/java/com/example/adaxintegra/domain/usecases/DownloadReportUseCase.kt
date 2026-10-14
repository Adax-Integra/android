package com.example.adaxintegra.domain.usecases

import com.example.adaxintegra.domain.repository.ReportRepository
import java.io.File
import java.time.LocalDate
import java.time.Period
import javax.inject.Inject

class DownloadReportUseCase @Inject constructor(
    private val repository: ReportRepository,
) {
    suspend operator fun invoke(startDate: String, endDate: String): File {
        require(startDate.isNotBlank()) { "Selecciona una fecha de inicio" }
        require(endDate.isNotBlank()) { "Selecciona una fecha de fin" }

        val start = LocalDate.parse(startDate)
        val end = LocalDate.parse(endDate)

        require(!start.isAfter(end)) { "La fecha de inicio tiene que ser antes de la fecha de fin" }

        val period = Period.between(start, end)
        val isGreaterThanOneYear = (period.years > 1) || ((period.years == 1) && (period.months > 0 || period.days > 0))
        require(!isGreaterThanOneYear) { "El rango del reporte no puede ser mayor a un año" }

        return repository.downloadReport(startDate, endDate)
    }
}
