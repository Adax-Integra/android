package com.example.adaxintegra.domain.usecases

import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.model.Event
import com.example.adaxintegra.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CreateEventUseCase @Inject constructor(
    private val eventRepository: EventRepository,
) {
    operator fun invoke(event: Event): Flow<Result<Event>> = flow {
        emit(Result.Loading)
        try {
            val created = eventRepository.createEvent(event)
            emit(Result.Success(created))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }
}
