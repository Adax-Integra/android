package com.example.adaxintegra.data.remote.api

import com.example.adaxintegra.data.remote.dto.CreateEventRequestDto
import com.example.adaxintegra.data.remote.dto.CreateEventResponseDto
import com.example.adaxintegra.data.remote.dto.DisableEventResponseDto
import com.example.adaxintegra.data.remote.dto.EventListResponseDto
import com.example.adaxintegra.data.remote.dto.UploadEventImageResponseDto
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PATCH
import retrofit2.http.PUT
import retrofit2.http.Part
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

    @POST("api/events")
    suspend fun createEvent(
        @Header("Authorization") authorization: String,
        @Body request: CreateEventRequestDto,
    ): CreateEventResponseDto

    // E-02: internal/admin edit an event. The body has the same fields as creating it
    @PUT("api/events/{eventId}")
    suspend fun updateEvent(
        @Header("Authorization") authorization: String,
        @Path("eventId") eventId: String,
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
