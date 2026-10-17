package com.example.adaxintegra.domain.usecases

import com.example.adaxintegra.domain.model.RecordDocuments
import com.example.adaxintegra.domain.repository.UploadDocumentsRepository
import javax.inject.Inject

// R-06: documents the external user already uploaded, with signed URLs of 15 minutes
class GetMyDocumentsUseCase @Inject constructor(
    private val uploadDocumentsRepository: UploadDocumentsRepository,
) {
    suspend operator fun invoke(): RecordDocuments = uploadDocumentsRepository.getDocuments()
}
