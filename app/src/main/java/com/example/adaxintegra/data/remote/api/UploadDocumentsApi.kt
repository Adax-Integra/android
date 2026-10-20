package com.example.adaxintegra.data.remote.api

import com.example.adaxintegra.data.remote.dto.RecordDocumentsResponseDto
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Streaming
import retrofit2.http.Url

interface UploadDocumentsApi {

    // R-06: documents the owner already uploaded. Same body as V-13, so its DTO is reused
    @GET("api/external-users/{userId}/documents")
    suspend fun getDocuments(
        @Path("userId") userId: String,
        @Header("Authorization") authorization: String,
    ): Response<RecordDocumentsResponseDto>

    // A null part is not sent, so the backend keeps that document as it is
    @Multipart
    @PATCH("api/external-users/{userId}/documents")
    suspend fun uploadDocuments(
        @Path("userId") userId: String,
        @Header("Authorization") authorization: String,
        @Part identityDocument: MultipartBody.Part?,
        @Part proofOfAddress: MultipartBody.Part?,
    ): Response<ResponseBody>

    // The signed URL already carries its own token, no Authorization header is sent
    @Streaming
    @GET
    suspend fun downloadDocument(
        @Url url: String,
    ): Response<ResponseBody>
}
