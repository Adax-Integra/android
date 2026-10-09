package com.example.adaxintegra.domain.usecases

import com.example.adaxintegra.data.repository.CaseDetailRepository
import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.model.CaseDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

// closes a case and reloads its updated information for the V-11 flow
class CloseCaseUseCase
@Inject
constructor(
    private val caseDetailRepository: CaseDetailRepository,
) {
    operator fun invoke(caseId: String): Flow<Result<CaseDetail>> =
        flow {
            emit(Result.Loading)

            try {
                caseDetailRepository.closeCase(caseId)

                // reloads the case after closure to keep the UI synchronized
                val updatedCase = caseDetailRepository.getCaseDetail(caseId)

                emit(Result.Success(updatedCase))
            } catch (e: Exception) {
                emit(Result.Error(e))
            }
        }
}
