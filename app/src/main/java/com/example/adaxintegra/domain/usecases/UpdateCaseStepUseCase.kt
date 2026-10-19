package com.example.adaxintegra.domain.usecases

import com.example.adaxintegra.data.repository.CaseDetailRepository
import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.model.CaseDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateCaseStepUseCase @Inject constructor(
    private val caseDetailRepository: CaseDetailRepository,
) {
    operator fun invoke(
        caseId: String,
        stepNumber: Int,
    ): Flow<Result<CaseDetail>> = flow {
        emit(Result.Loading)

        try {
            val updatedCase = caseDetailRepository.updateCaseStep(
                caseId = caseId,
                stepNumber = stepNumber,
            )
            emit(Result.Success(updatedCase))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }
}
