package com.example.adaxintegra.data.remote.api

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import retrofit2.http.Streaming

interface ReportApi {
    @GET("api/reports/export")
    // Using streaming to not store the entire file in memory
    @Streaming
    suspend fun exportReportCsv(
        @Header("Authorization") authorization: String,
        @Query("start-date") startDate: String,
        @Query("end-date") endDate: String,
    ): ResponseBody
}
