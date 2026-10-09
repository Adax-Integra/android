package com.example.adaxintegra.domain.usecases

import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.model.ActivityLogPage
import com.example.adaxintegra.domain.repository.ActivityLogRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.Date
import javax.inject.Inject

// V-06: the admin consults the activity log, one page at a time
class GetActivityLogUseCase @Inject constructor(
    private val repository: ActivityLogRepository,
) {
    operator fun invoke(page: Int, from: Date?, to: Date?): Flow<Result<ActivityLogPage>> = flow {
        try {
            emit(Result.Loading)
            val logPage = repository.getActivityLog(page, from, to)
            emit(Result.Success(logPage))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }
}

