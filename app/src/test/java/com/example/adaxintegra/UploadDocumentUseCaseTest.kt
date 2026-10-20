package com.example.adaxintegra

import com.example.adaxintegra.domain.model.DocumentKind
import com.example.adaxintegra.domain.model.RecordDocuments
import com.example.adaxintegra.domain.model.UploadDocumentFile
import com.example.adaxintegra.domain.repository.UploadDocumentsRepository
import com.example.adaxintegra.domain.usecases.UploadDocumentUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Test

class UploadDocumentUseCaseTest {

    // Repositorio de prueba: solo guarda lo que recibe.
    private class FakeUploadDocumentsRepository : UploadDocumentsRepository {
        var identityDocument: UploadDocumentFile? = null
        var proofOfAddress: UploadDocumentFile? = null

        override suspend fun getDocuments(): RecordDocuments = RecordDocuments(identityDocument = null, proofOfAddress = null)

        override suspend fun uploadDocuments(
            identityDocument: UploadDocumentFile?,
            proofOfAddress: UploadDocumentFile?,
        ) {
            this.identityDocument = identityDocument
            this.proofOfAddress = proofOfAddress
        }

        override suspend fun downloadDocument(url: String): ByteArray = ByteArray(0)
    }

    private val file = UploadDocumentFile(
        fileName = "comprobante-identidad.jpg",
        mimeType = "image/jpeg",
        bytes = "foto-ficticia".toByteArray(),
    )

    @Test
    fun identity_isSentAsIdentityDocumentOnly() {
        val repository = FakeUploadDocumentsRepository()
        val useCase = UploadDocumentUseCase(repository)

        runBlocking { useCase(DocumentKind.IDENTITY, file) }

        assertSame(file, repository.identityDocument)
        assertNull(repository.proofOfAddress)
    }

    @Test
    fun proofOfAddress_isSentAsProofOfAddressOnly() {
        val repository = FakeUploadDocumentsRepository()
        val useCase = UploadDocumentUseCase(repository)

        runBlocking { useCase(DocumentKind.PROOF_OF_ADDRESS, file) }

        assertNull(repository.identityDocument)
        assertSame(file, repository.proofOfAddress)
    }
}
