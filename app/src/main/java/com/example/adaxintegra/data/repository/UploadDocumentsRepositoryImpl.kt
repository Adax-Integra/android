package com.example.adaxintegra.data.repository

import com.example.adaxintegra.data.mapper.toDomain
import com.example.adaxintegra.data.remote.api.UploadDocumentsApi
import com.example.adaxintegra.domain.model.RecordDocuments
import com.example.adaxintegra.domain.model.UploadDocumentFile
import com.example.adaxintegra.domain.repository.AuthRepository
import com.example.adaxintegra.domain.repository.UploadDocumentsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UploadDocumentsRepositoryImpl @Inject constructor(
    private val api: UploadDocumentsApi,
    private val authRepository: AuthRepository,
) : UploadDocumentsRepository {

    override suspend fun getDocuments(): RecordDocuments {
        val (token, userId) = requireSession()

        val response = api.getDocuments(
            userId = userId,
            authorization = "Bearer $token",
        )
        val data = response.body()?.data

        if (!response.isSuccessful || data == null) {
            response.errorBody()?.close()
            throw IllegalStateException(
                errorMessage(
                    code = response.code(),
                    fallback = "No se pudieron cargar tus documentos. Intenta de nuevo.",
                ),
            )
        }

        return data.toDomain()
    }

    override suspend fun uploadDocuments(
        identityDocument: UploadDocumentFile?,
        proofOfAddress: UploadDocumentFile?,
    ) {
        if (identityDocument == null && proofOfAddress == null) {
            throw IllegalArgumentException("Selecciona al menos un documento")
        }

        val (token, userId) = requireSession()

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
                errorMessage(
                    code = response.code(),
                    fallback = "No se pudo subir el documento. Intenta de nuevo.",
                ),
            )
        }
    }

    override suspend fun downloadDocument(url: String): ByteArray {
        val response = api.downloadDocument(url)

        if (!response.isSuccessful) {
            response.errorBody()?.close()
            throw IllegalStateException("No se pudo descargar el documento. Intenta de nuevo.")
        }

        // @Streaming leaves the body open, it is read off the main thread
        return withContext(Dispatchers.IO) {
            response.body()?.use { it.bytes() }
        } ?: throw IllegalStateException("El documento está vacío")
    }

    // Token and id of the external user that is signed in
    private fun requireSession(): Pair<String, String> {
        val session = authRepository.session.value
        val token = session?.token
        val userId = session?.userId

        if (token.isNullOrBlank() || userId.isNullOrBlank()) {
            throw IllegalStateException("Tu sesión expiró. Vuelve a iniciar sesión.")
        }

        return token to userId
    }

    // Same messages as IdentityDocumentsViewModel on iOS
    private fun errorMessage(
        code: Int,
        fallback: String,
    ): String = when (code) {
        400 -> "El documento debe ser PNG o JPG de máximo 5 MB."
        401 -> "Tu sesión expiró. Vuelve a iniciar sesión."
        429 -> "Hiciste demasiados intentos. Intenta más tarde."
        else -> fallback
    }
}
