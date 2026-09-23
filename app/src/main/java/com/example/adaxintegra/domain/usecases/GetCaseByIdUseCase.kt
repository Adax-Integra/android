package com.example.adaxintegra.domain.usecases

import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.model.Case
import com.example.adaxintegra.domain.repository.CaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

// caseRepository receives case id and returns Case from domain
class GetCaseByIdUseCase
    @Inject
    constructor(
        private val caseRepository: CaseRepository,
    ) {
        operator fun invoke(caseId: String): Flow<Result<Case>> =
            flow {
                emit(Result.Loading)
                try {
                    val case = caseRepository.getCaseById(caseId)
                    emit(Result.Success(case))
                } catch (e: Exception) {
                    emit(Result.Error(e))
                }
            }
    }
