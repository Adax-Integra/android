package com.example.adaxintegra.data.repository

import com.example.adaxintegra.data.mapper.toDomain
import com.example.adaxintegra.data.remote.api.RecordDocumentsApi
import com.example.adaxintegra.domain.model.RecordDocuments
import com.example.adaxintegra.domain.repository.AuthRepository
import com.example.adaxintegra.domain.repository.RecordDocumentsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecordDocumentsRepositoryImpl @Inject constructor(
    private val api: RecordDocumentsApi,
    private val authRepository: AuthRepository,
) : RecordDocumentsRepository {

    override suspend fun getRecordDocuments(userId: String): RecordDocuments {
        val token = authRepository.session.value?.token

        if (token.isNullOrBlank()) {
            throw IllegalStateException("Inicia sesión para consultar los documentos")
        }

        val response = api.getRecordDocuments(
            userId = userId,
            authorization = "Bearer $token",
        )
        val data = response.body()?.data

        if (!response.isSuccessful || data == null) {
            response.errorBody()?.close()
            throw IllegalStateException(
                when (response.code()) {
                    401 -> "Tu sesión expiró, vuelve a iniciar sesión"
                    403 -> "No tienes permiso para ver estos documentos"
                    404 -> "No se encontró el expediente"
                    else -> "No se pudieron cargar los documentos"
                },
            )
        }

        return data.toDomain()
    }

    override suspend fun downloadDocument(url: String): ByteArray {
        val response = api.downloadDocument(url)

        if (!response.isSuccessful) {
            response.errorBody()?.close()
            throw IllegalStateException("No se pudo descargar el documento")
        }

        // @Streaming leaves the body open, it is read off the main thread
        return withContext(Dispatchers.IO) {
            response.body()?.use { it.bytes() }
        } ?: throw IllegalStateException("El documento está vacío")
    }
}
