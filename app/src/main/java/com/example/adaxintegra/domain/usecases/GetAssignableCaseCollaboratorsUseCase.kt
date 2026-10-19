package com.example.adaxintegra.domain.usecases

import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.model.CollaboratorSummary
import com.example.adaxintegra.domain.repository.CaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAssignableCaseCollaboratorsUseCase @Inject constructor(
    private val caseRepository: CaseRepository,
) {
    operator fun invoke(): Flow<Result<List<CollaboratorSummary>>> = flow {
        emit(Result.Loading)
        try {
            emit(Result.Success(caseRepository.getAssignableCaseCollaborators()))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }
}
