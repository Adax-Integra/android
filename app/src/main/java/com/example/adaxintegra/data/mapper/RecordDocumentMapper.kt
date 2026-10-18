package com.example.adaxintegra.data.mapper

import com.example.adaxintegra.data.remote.dto.RecordDocumentDto
import com.example.adaxintegra.data.remote.dto.RecordDocumentsDataDto
import com.example.adaxintegra.domain.model.RecordDocument
import com.example.adaxintegra.domain.model.RecordDocumentType
import com.example.adaxintegra.domain.model.RecordDocuments

fun RecordDocumentsDataDto.toDomain(): RecordDocuments = RecordDocuments(
    identityDocument = identityDocument?.toDomain(),
    proofOfAddress = proofOfAddress?.toDomain(),
)

// R-06 only accepts PDF, JPG and PNG, so anything that is not a PDF is an image
fun RecordDocumentDto.toDomain(): RecordDocument = RecordDocument(
    url = url,
    fileType = if (fileType == "pdf") RecordDocumentType.PDF else RecordDocumentType.IMAGE,
)
