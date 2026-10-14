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

    override suspend fun downloadReport(startDate: String, endDate: String): File =
        withContext(Dispatchers.IO) {
            val responseBody = reportApi.exportReportCsv(
                authorization = authorizationHeader(),
                startDate = startDate,
                endDate = endDate,
            )

            val fileName = "reporte_${startDate}_${endDate}.csv"

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
