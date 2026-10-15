package com.example.adaxintegra.data.remote.api

import com.example.adaxintegra.data.remote.dto.CreateEventRequestDto
import com.example.adaxintegra.data.remote.dto.CreateEventResponseDto
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface EventApi {
    @POST("api/events")
    suspend fun createEvent(
        @Header("Authorization") authorization: String,
        @Body request: CreateEventRequestDto,
    ): CreateEventResponseDto
}
