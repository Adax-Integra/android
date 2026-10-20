package com.example.adaxintegra.domain.usecases

import com.example.adaxintegra.data.repository.ViolenceRepository
import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.model.Violence
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetViolenceTypesUseCase @Inject constructor(
    private val violenceRepository: ViolenceRepository
) {
    operator fun invoke(): Flow<Result<List<Violence>>> = flow {
        emit(Result.Loading)
        try {
            val violenceTypes = violenceRepository.getViolenceTypes()
            emit(Result.Success(violenceTypes))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }
}
