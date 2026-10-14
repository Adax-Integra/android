package com.example.adaxintegra.data.repository

import android.content.Context
import com.example.adaxintegra.data.remote.api.ReportApi
import com.example.adaxintegra.domain.repository.AuthRepository
import com.example.adaxintegra.domain.repository.ReportRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReportRepositoryImpl @Inject constructor(
    private val reportApi: ReportApi,
    private val authRepository: AuthRepository,
    @ApplicationContext private val context: Context,
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

            // What the file will be named
            val fileName = "reporte_${startDate}_${endDate}.csv"
            // Tells where the file will be saved
            // It will be a temporary saved so we use the cacheDir
            val file = File(context.cacheDir, fileName)

            // Writes the file to temporary storage
            responseBody.byteStream().use { inputStream ->
                file.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }

            // Returns the file
            file
        }
}
