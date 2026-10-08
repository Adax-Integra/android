package com.example.adaxintegra.data.remote.api

import com.example.adaxintegra.data.remote.dto.RecordListResponseDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface RecordApi {
    // Null filters are omitted, the backend returns 10 records per page
    @GET("api/records")
    suspend fun getRecords(
        @Header("Authorization") authorization: String,
        @Query("page") page: Int = 1,
        @Query("search") search: String = "",
        @Query("hasOpenCases") hasOpenCases: Boolean? = null,
        @Query("status") status: String? = null,
    ): RecordListResponseDto
}
