package com.example.adaxintegra.domain.repository

import com.example.adaxintegra.domain.model.NewEvent
import okhttp3.MultipartBody

interface EventRepository {
    suspend fun createEvent(token: String, event: NewEvent): Result<String>
    suspend fun uploadEventImage(token: String, eventId: String, imagePart: MultipartBody.Part): Result<Unit>
}
