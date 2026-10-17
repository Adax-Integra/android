
package com.example.adaxintegra.data.repository

import com.example.adaxintegra.data.remote.api.UploadDocumentsApi
import com.example.adaxintegra.domain.model.UploadDocumentFile
import com.example.adaxintegra.domain.repository.AuthRepository
import com.example.adaxintegra.domain.repository.UploadDocumentsRepository
import javax.inject.Inject
import javax.inject.Singleton
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

@Singleton
class UploadDocumentsRepositoryImpl @Inject constructor(
    private val api: UploadDocumentsApi,
    private val authRepository: AuthRepository,
) : UploadDocumentsRepository {

    override suspend fun uploadDocuments(
        identityDocument: UploadDocumentFile?,
        proofOfAddress: UploadDocumentFile?,
    ) {
        if (identityDocument == null && proofOfAddress == null) {
            throw IllegalArgumentException("Selecciona al menos un documento")
        }

        val session = authRepository.session.value
        val token = session?.token
        val userId = session?.userId

        if (token.isNullOrBlank() || userId.isNullOrBlank()) {
            throw IllegalStateException("Inicia sesión para subir documentos")
        }

        val identityPart = identityDocument?.let { file ->
            MultipartBody.Part.createFormData(
                "identity_document",
                file.fileName,
                file.bytes.toRequestBody(file.mimeType.toMediaType()),
            )
        }

        val addressPart = proofOfAddress?.let { file ->
            MultipartBody.Part.createFormData(
                "proof_of_address",
                file.fileName,
                file.bytes.toRequestBody(file.mimeType.toMediaType()),
            )
        }

        val response = api.uploadDocuments(
            userId = userId,
            authorization = "Bearer $token",
            identityDocument = identityPart,
            proofOfAddress = addressPart,
        )

        if (response.isSuccessful) {
            response.body()?.close()
        } else {
            response.errorBody()?.close()
            throw IllegalStateException(
                "No se pudieron subir los documentos. Código: ${response.code()}",
            )
        }
    }
}
