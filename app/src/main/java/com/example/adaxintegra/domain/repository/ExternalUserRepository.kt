package com.example.adaxintegra.domain.repository

import com.example.adaxintegra.domain.model.ExternalUserPage
import com.example.adaxintegra.domain.model.ExternalUserProfile
import com.example.adaxintegra.domain.model.ExternalUserProfileUpdate

interface ExternalUserRepository {
    // G-16: returns one page of external accounts, newest first
    suspend fun getExternalUsers(page: Int, search: String, status: String): ExternalUserPage

    suspend fun getExternalProfile(userId: String): ExternalUserProfile
    suspend fun updateExternalProfile(userId: String, update: ExternalUserProfileUpdate): ExternalUserProfile
}
