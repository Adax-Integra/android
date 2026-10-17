
package com.example.adaxintegra.data.remote.api

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.Part
import retrofit2.http.Path

interface UploadDocumentsApi {

    @Multipart
    @PATCH("api/external-users/{userId}/documents")
    suspend fun uploadDocuments(
        @Path("userId") userId: String,
        @Header("Authorization") authorization: String,
        @Part identityDocument: MultipartBody.Part?,
        @Part proofOfAddress: MultipartBody.Part?
    ): Response<ResponseBody>
}
