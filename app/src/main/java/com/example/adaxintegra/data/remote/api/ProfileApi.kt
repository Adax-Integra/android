package com.example.adaxintegra.data.remote.api

import com.example.adaxintegra.data.remote.dto.EditableProfileResponseDto
import com.example.adaxintegra.data.remote.dto.DeactivateAccountResponseDto
import com.example.adaxintegra.data.remote.dto.ProfileResponseDto
import com.example.adaxintegra.data.remote.dto.UpdateProfileResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.Path

//simply calling for userId to find correct profile to display
interface ProfileApi {
    @GET("api/profile/{userId}")
    suspend fun getProfile(
        @Path("userId") userId: String,
        @Header("Authorization") authorization: String
    ): ProfileResponseDto

    //edit profile endpoints
    @GET("api/profile/{userId}/edit")
    suspend fun getEditableProfile(
        @Path("userId") userId: String,
        @Header("Authorization") authorization: String
    ): EditableProfileResponseDto

    @PATCH("api/profile/{userId}")
    suspend fun updateProfile(
        @Path("userId") userId: String,
        @Header("Authorization") authorization: String,
        @Body fields: Map<String, String>
    ): UpdateProfileResponseDto
  
    // G-08: the user deactivates her own account, the id must match the token
    @PATCH("api/profile/{userId}/deactivate")
    suspend fun deactivateAccount(
        @Path("userId") userId: String,
        @Header("Authorization") authorization: String
    ): DeactivateAccountResponseDto
}
