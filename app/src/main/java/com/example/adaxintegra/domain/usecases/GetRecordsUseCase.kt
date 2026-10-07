package com.example.adaxintegra.domain.usecases

import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.entities.RecordPage
import com.example.adaxintegra.domain.repository.RecordRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetRecordsUseCase @Inject constructor(
    private val repository: RecordRepository,
) {
    // Emits loading, success, or error for the requested page and filter
    operator fun invoke(
        page: Int = 1,
        search: String = "",
        hasOpenCases: Boolean? = null,
        status: String? = null,
    ): Flow<Result<RecordPage>> = flow {
        emit(Result.Loading)

        try {
            val result = repository.getRecords(
                page = page,
                search = search,
                hasOpenCases = hasOpenCases,
                status = status,
            )

            emit(Result.Success(result))
        } catch (exception: CancellationException) {
            // Allow obsolete request to be cancelled without showing an error
            throw exception
        } catch (exception: Exception) {
            emit(Result.Error(exception))
        }
    }
}
