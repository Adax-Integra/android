package com.example.adaxintegra.domain.usecases

import com.example.adaxintegra.domain.model.DocumentKind
import com.example.adaxintegra.domain.model.UploadDocumentFile
import com.example.adaxintegra.domain.repository.UploadDocumentsRepository
import javax.inject.Inject

// R-06: uploads or replaces one document, the other one stays the same in the backend
class UploadDocumentUseCase @Inject constructor(
    private val uploadDocumentsRepository: UploadDocumentsRepository,
) {
    suspend operator fun invoke(
        kind: DocumentKind,
        file: UploadDocumentFile,
    ) {
        when (kind) {
            DocumentKind.IDENTITY -> uploadDocumentsRepository.uploadDocuments(
                identityDocument = file,
                proofOfAddress = null,
            )

            DocumentKind.PROOF_OF_ADDRESS -> uploadDocumentsRepository.uploadDocuments(
                identityDocument = null,
                proofOfAddress = file,
            )
        }
    }
}
