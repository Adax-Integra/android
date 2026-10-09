package com.example.adaxintegra.data.remote.api

import com.example.adaxintegra.data.remote.dto.ActivityLogResponseDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ActivityLogApi {
    // V-06: admin consults the activity log (requires the admin token).
    @GET("api/internal-users/activity-log")
    suspend fun getActivityLog(
        @Header("Authorization") authorization: String,
        @Query("page") page: Int,
        @Query("from") from: String?,
        @Query("to") to: String?,
    ): ActivityLogResponseDto
}
