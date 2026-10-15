package com.example.adaxintegra.domain.repository

import com.example.adaxintegra.domain.model.NewEvent

interface EventRepository {
    suspend fun createEvent(token: String, event: NewEvent): Result<Unit>
}
