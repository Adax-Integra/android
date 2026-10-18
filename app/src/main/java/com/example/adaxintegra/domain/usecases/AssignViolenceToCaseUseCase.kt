package com.example.adaxintegra.domain.usecases

import com.example.adaxintegra.data.repository.CaseDetailRepository
import com.example.adaxintegra.domain.common.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

// R-07: assigns required violence types to a specific case
class AssignViolenceToCaseUseCase @Inject constructor(
    private val caseDetailRepository: CaseDetailRepository
) {
    operator fun invoke(caseId: String, violenceIds: List<String>): Flow<Result<Unit>> = flow {
        emit(Result.Loading)
        try {
            val success = caseDetailRepository.assignViolenceToCase(caseId, violenceIds)
            if (success) {
                emit(Result.Success(Unit))
            } else {
                emit(Result.Error(Exception("No se pudo asignar la violencia al caso.")))
            }
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }
}
