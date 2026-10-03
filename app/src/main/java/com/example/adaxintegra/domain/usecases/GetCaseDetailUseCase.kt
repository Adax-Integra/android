package com.example.adaxintegra.domain.usecases

import com.example.adaxintegra.data.repository.CaseDetailRepository
import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.model.CaseDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

// retrieves the complete case information required by the V-11 detail flow
class GetCaseDetailUseCase
@Inject
constructor(
    private val caseDetailRepository: CaseDetailRepository,
) {
    operator fun invoke(caseId: String): Flow<Result<CaseDetail>> =
        flow {
            emit(Result.Loading)

            try {
                val caseDetail = caseDetailRepository.getCaseDetail(caseId)
                emit(Result.Success(caseDetail))
            } catch (e: Exception) {
                emit(Result.Error(e))
            }
        }
}
