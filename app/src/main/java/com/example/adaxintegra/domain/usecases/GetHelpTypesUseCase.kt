package com.example.adaxintegra.domain.usecases

import com.example.adaxintegra.data.repository.HelpRepository
import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.model.HelpType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetHelpTypesUseCase @Inject constructor(
    private val helpRepository: HelpRepository
) {
    operator fun invoke(): Flow<Result<List<HelpType>>> = flow {
        emit(Result.Loading)
        try {
            val helpTypes = helpRepository.getHelpTypes()
            emit(Result.Success(helpTypes))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }
}
