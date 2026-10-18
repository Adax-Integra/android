package com.example.adaxintegra.domain.usecases

import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.repository.CaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AssignCollaboratorsToCaseUseCase @Inject constructor(
    private val caseRepository: CaseRepository,
) {
    operator fun invoke(caseId: String, userIds: List<String>): Flow<Result<Unit>> = flow {
        try {
            emit(Result.Loading)
            caseRepository.assignCollaboratorsToCase(caseId, userIds)
            emit(Result.Success(Unit))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }
}
