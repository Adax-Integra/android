package com.example.adaxintegra.data.repository

import android.os.Environment
import com.example.adaxintegra.data.remote.api.ReportApi
import com.example.adaxintegra.domain.repository.AuthRepository
import com.example.adaxintegra.domain.repository.ReportRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReportRepositoryImpl @Inject constructor(
    private val reportApi: ReportApi,
    private val authRepository: AuthRepository,
) : ReportRepository {

    private fun authorizationHeader(): String {
        val token = authRepository.session.value?.token

        if (token.isNullOrBlank()) {
            throw IllegalStateException("Inicia sesión para descargar reportes.")
        }
        return "Bearer $token"
    }

    private fun formatDateForFilename(dateStr: String): String {
        return try {
            // We separate the string into YYYY, MM and DD
            val parts = dateStr.split("-")
            if (parts.size == 3) {
                // We only use the last 2 digits of the year
                val year = parts[0].takeLast(2)
                val monthIdx = parts[1].toIntOrNull()?.minus(1) ?: 0
                // We remove the leading 0 of the day number
                val day = parts[2].toInt().toString()
                val months = listOf(
                    "enero",
                    "febrero",
                    "marzo",
                    "abril",
                    "mayo",
                    "junio",
                    "julio",
                    "agosto",
                    "septiembre",
                    "octubre",
                    "noviembre",
                    "diciembre"
                )
                // Change the month index to the month name
                val monthStr = months.getOrElse(monthIdx) { "mes" }

                // The final date format
                "${day}-${monthStr}-$year"
            } else {
                dateStr
            }
        } catch (_: Exception) {
            dateStr
        }
    }

    override suspend fun downloadReport(startDate: String, endDate: String): File =
        withContext(Dispatchers.IO) {
            val responseBody = reportApi.exportReportCsv(
                authorization = authorizationHeader(),
                startDate = startDate,
                endDate = endDate,
            )

            // To save the report we change the dates to more a more human-readable format
            val startFormatted = formatDateForFilename(startDate)
            val endFormatted = formatDateForFilename(endDate)

            // An example of filename would be "reporte de 1-ago-26 al 30-ago-26.csv"
            val fileName = "reporte de ${startFormatted} al ${endFormatted}.csv"

            // Tells where the file will be saved
            // We save it in the public Downloads directory so the user can easily find it
            val downloadsDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

            val file = File(downloadsDir, fileName)

            responseBody.byteStream().use { inputStream ->
                file.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }

            // Returns the file
            file
        }
}
