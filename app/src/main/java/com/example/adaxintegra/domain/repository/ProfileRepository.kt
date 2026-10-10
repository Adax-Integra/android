package com.example.adaxintegra.domain.repository

import com.example.adaxintegra.domain.model.ProfileView

interface ProfileRepository {
    suspend fun getProfileByUserId(userId: String): ProfileView
}
