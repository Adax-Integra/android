package com.example.adaxintegra.domain.usecases

import com.example.adaxintegra.data.repository.CaseDetailRepository
import com.example.adaxintegra.domain.common.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

// R-08: assigns required help to a specific case
class AssignHelpsToCaseUseCase @Inject constructor(
    private val caseDetailRepository: CaseDetailRepository
) {
    operator fun invoke(caseId: String, helpIds: List<String>): Flow<Result<Unit>> = flow {
        emit(Result.Loading)
        try {
            val success = caseDetailRepository.assignHelpsToCase(caseId, helpIds)
            if (success) {
                // If the operation is successful, you could also optionally reload the case here
                // like in CloseCaseUseCase to keep it in sync, if necessary.
                emit(Result.Success(Unit))
            } else {
                emit(Result.Error(Exception("No se pudo asignar la ayuda al caso.")))
            }
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }
}
