package com.example.adaxintegra.domain.usecases

import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.model.Event
import com.example.adaxintegra.domain.model.EventScope
import com.example.adaxintegra.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

// E-01: any user consults the active events of the association
class GetEventsUseCase @Inject constructor(
    private val repository: EventRepository,
) {
    operator fun invoke(scope: EventScope): Flow<Result<List<Event>>> = flow {
        try {
            emit(Result.Loading)
            val events = repository.getEvents(scope)
            emit(Result.Success(events))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }
}
