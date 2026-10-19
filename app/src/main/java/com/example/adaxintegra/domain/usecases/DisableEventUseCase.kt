package com.example.adaxintegra.domain.usecases

import com.example.adaxintegra.domain.repository.EventRepository
import javax.inject.Inject
import com.example.adaxintegra.domain.common.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DisableEventUseCase @Inject constructor(
    private val repository: EventRepository,
) {
    operator fun invoke(
        eventId: String
    ): Flow<Result<Unit>> = flow {
        emit(Result.Loading)

        try {
            repository.disableEvent(eventId)
            emit(Result.Success(Unit))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }
}
