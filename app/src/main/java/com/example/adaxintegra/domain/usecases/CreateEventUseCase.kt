package com.example.adaxintegra.domain.usecases

import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.model.Event
import com.example.adaxintegra.domain.model.NewEvent
import com.example.adaxintegra.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CreateEventUseCase @Inject constructor(
    private val eventRepository: EventRepository,
) {
    operator fun invoke(newEvent: NewEvent): Flow<Result<Event>> = flow {
        emit(Result.Loading)
        try {
            val event = eventRepository.createEvent(newEvent)
            emit(Result.Success(event))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }
}
