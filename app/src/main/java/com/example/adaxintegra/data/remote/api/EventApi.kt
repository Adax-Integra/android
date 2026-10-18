package com.example.adaxintegra.data.remote.api

import com.example.adaxintegra.data.remote.dto.CreateEventRequestDto
import com.example.adaxintegra.data.remote.dto.CreateEventResponseDto
import com.example.adaxintegra.data.remote.dto.EventListResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface EventApi {
    // E-01: any signed-in user consults the active events.
    // scope "month" is for the home carousel and "all" for the events screen

    @GET("api/events")
    suspend fun getEvents(
        @Header("Authorization") authorization: String,
        @Query("scope") scope: String,
    ): EventListResponseDto

    @POST("api/events")
    suspend fun createEvent(
        @Header("Authorization") authorization: String,
        @Body request: CreateEventRequestDto,
    ): CreateEventResponseDto
}
