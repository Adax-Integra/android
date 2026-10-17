package com.example.adaxintegra.domain.repository

import com.example.adaxintegra.domain.model.ProfileView

interface ProfileRepository {
    suspend fun getProfileByUserId(userId: String): ProfileView

    // G-08: deactivates the account of the signed in user
    suspend fun deactivateAccount(userId: String)
}
