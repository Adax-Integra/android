package com.example.adaxintegra.domain.usecases

import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.model.RecordDocuments
import com.example.adaxintegra.domain.repository.RecordDocumentsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

// V-13: documents of an external user's record, seen by an internal user
class GetRecordDocumentsUseCase @Inject constructor(
    private val recordDocumentsRepository: RecordDocumentsRepository,
) {
    operator fun invoke(userId: String): Flow<Result<RecordDocuments>> = flow {
        emit(Result.Loading)
        try {
            val documents = recordDocumentsRepository.getRecordDocuments(userId)
            emit(Result.Success(documents))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }
}
