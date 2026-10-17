package com.example.adaxintegra.domain.usecases

import com.example.adaxintegra.domain.repository.UploadDocumentsRepository
import javax.inject.Inject

// R-06: downloads one of her documents from its signed URL to save or share it
class DownloadMyDocumentUseCase @Inject constructor(
    private val uploadDocumentsRepository: UploadDocumentsRepository,
) {
    suspend operator fun invoke(url: String): ByteArray = uploadDocumentsRepository.downloadDocument(url)
}
