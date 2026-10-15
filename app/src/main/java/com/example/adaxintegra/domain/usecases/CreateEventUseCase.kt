package com.example.adaxintegra.domain.usecases

import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.model.NewEvent
import com.example.adaxintegra.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CreateEventUseCase @Inject constructor(
    private val repository: EventRepository,
) {
    operator fun invoke(token: String, event: NewEvent): Flow<Result<Unit>> = flow {
        emit(Result.Loading)
        try {
            val result = repository.createEvent(token, event)
            if (result.isSuccess) {
                emit(Result.Success(Unit))
            } else {
                emit(Result.Error(result.exceptionOrNull() ?: Exception("Error al crear el evento")))
            }
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }
}
