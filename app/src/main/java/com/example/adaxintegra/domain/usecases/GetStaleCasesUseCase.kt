package com.example.adaxintegra.domain.usecases

import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.entities.Case
import com.example.adaxintegra.domain.repository.CaseRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

// NV-04: open cases that have gone the longest without changes, for the internals home
class GetStaleCasesUseCase @Inject constructor(
    private val repository: CaseRepository,
) {
    operator fun invoke(limit: Int = 5): Flow<Result<List<Case>>> = flow {
        emit(Result.Loading)

        try {
            val result = repository.getStaleCases(limit = limit)
            emit(Result.Success(result.cases))
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            emit(Result.Error(exception))
        }
    }
}
