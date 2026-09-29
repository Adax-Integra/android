package com.example.adaxintegra.data.repository

import com.example.adaxintegra.data.mapper.toDomain
import com.example.adaxintegra.data.remote.api.ProfileApi
import com.example.adaxintegra.domain.repository.ProfileRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepositoryImpl @Inject constructor(
    private val api: ProfileApi,
) : ProfileRepository {
    override suspend fun getProfileByUserId(userId: String): Profile {
        val response = api.getProfile(userId)
        return response.data.toDomain()
    }
}
