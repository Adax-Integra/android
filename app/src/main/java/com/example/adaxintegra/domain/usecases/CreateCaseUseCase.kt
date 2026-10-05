package com.example.adaxintegra.domain.usecases

import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.model.Case
import com.example.adaxintegra.domain.model.NewCase
import com.example.adaxintegra.domain.repository.CaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CreateCaseUseCase @Inject constructor(
    private val caseRepository: CaseRepository,
) {
    operator fun invoke (userId: String, newCase: NewCase):
        Flow<Result<Case>> = flow{
            emit(Result.Loading)
            try{
                val created = caseRepository.createCase(userId, newCase)
                emit(Result.Success(created))
            } catch (e: Exception){
                emit(Result.Error(e))
            }
    }
}
