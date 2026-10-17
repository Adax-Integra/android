package com.example.adaxintegra.domain.usecases

import com.example.adaxintegra.domain.repository.RecordDocumentsRepository
import javax.inject.Inject

// V-13: downloads one document from its signed URL to preview it inside the app
class DownloadRecordDocumentUseCase @Inject constructor(
    private val recordDocumentsRepository: RecordDocumentsRepository,
) {
    suspend operator fun invoke(url: String): ByteArray = recordDocumentsRepository.downloadDocument(url)
}
