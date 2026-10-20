package com.example.adaxintegra.domain.usecases

import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.model.UploadDocumentFile
import com.example.adaxintegra.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UploadEventImageUseCase @Inject constructor(
    private val eventRepository: EventRepository,
) {
    operator fun invoke(eventId: String, imageFile: UploadDocumentFile): Flow<Result<String>> = flow {
        emit(Result.Loading)
        try {
            val imageUrl = eventRepository.uploadEventImage(eventId, imageFile)
            emit(Result.Success(imageUrl))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }
}
