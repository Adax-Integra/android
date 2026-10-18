package com.example.adaxintegra.data.remote.api

import com.example.adaxintegra.data.remote.dto.DisableEventResponseDto
import com.example.adaxintegra.data.remote.dto.EventListResponseDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query

interface EventApi {
    // E-01: any signed-in user consults the active events.
    // scope "month" is for the home carousel and "all" for the events screen

    @GET("api/events")
    suspend fun getEvents(
        @Header("Authorization") authorization: String,
        @Query("scope") scope: String,
    ): EventListResponseDto

    //disable an event
    @PATCH("api/events/{eventId}/disable")
    suspend fun disableEvent(
        @Path("eventId") eventId: String,
        @Header("Authorization") authorization: String,
    ): DisableEventResponseDto
}
