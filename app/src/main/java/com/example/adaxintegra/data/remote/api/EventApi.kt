package com.example.adaxintegra.data.remote.api

import com.example.adaxintegra.data.remote.dto.CreateEventRequestDto
import com.example.adaxintegra.data.remote.dto.CreateEventResponseDto
import com.example.adaxintegra.data.remote.dto.UploadEventImageResponseDto
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface EventApi {
    @POST("api/events")
    suspend fun createEvent(
        @Header("Authorization") authorization: String,
        @Body request: CreateEventRequestDto,
    ): CreateEventResponseDto

    @Multipart
    @PATCH("api/events/{eventId}/image")
    suspend fun uploadEventImage(
        @Header("Authorization") authorization: String,
        @Path("eventId") eventId: String,
        @Part image: MultipartBody.Part,
    ): UploadEventImageResponseDto
}
