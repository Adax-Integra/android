package com.example.adaxintegra.domain.repository

interface ProfileRepository {
    suspend fun getProfileByUserId(userId: String): Profile
}
