package com.example.adaxintegra.domain.usecases

import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.model.Case
import com.example.adaxintegra.domain.repository.CaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCasesFromUserUseCase @Inject constructor(
    private val caseRepository: CaseRepository,
) {
    operator fun invoke(userId: String): Flow<Result<List<Case>>> = flow {
        emit(Result.Loading)
        try {
            val cases = caseRepository.getCasesFromUser(userId)
            emit(Result.Success(cases))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }
}
