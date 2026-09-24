package com.example.adaxintegra.domain.usecases

import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.entities.CasePage
import com.example.adaxintegra.domain.repository.CaseRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCasesUseCase @Inject constructor(
    private val repository: CaseRepository,
) {
    operator fun invoke(
        search: String = "",
        urgency: String = "",
        page: Int = 1,
        limit: Int = 20,
    ): Flow<Result<CasePage>> = flow {
        emit(Result.Loading)

        try {
            val result = repository.getCases(
                search = search,
                urgency = urgency,
                page = page,
                limit = limit,
            )
            emit(Result.Success(result))
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            emit(Result.Error(exception))
        }
    }
}
