package com.example.adaxintegra.data.remote.api

import com.example.adaxintegra.data.remote.dto.RecordDocumentsResponseDto
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Streaming
import retrofit2.http.Url

interface RecordDocumentsApi {
    // V-13: signed URLs of the external user's documents (internal and admin only)
    @GET("api/internal-users/external-users/{userId}/documents")
    suspend fun getRecordDocuments(
        @Path("userId") userId: String,
        @Header("Authorization") authorization: String,
    ): Response<RecordDocumentsResponseDto>

    // The signed URL already carries its own token, no Authorization header is sent
    @Streaming
    @GET
    suspend fun downloadDocument(
        @Url url: String,
    ): Response<ResponseBody>
}
