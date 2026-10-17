package com.example.adaxintegra.domain.repository

import com.example.adaxintegra.domain.model.ExternalUserPage

interface ExternalUserRepository {
    // G-16: returns one page of external accounts, newest first
    suspend fun getExternalUsers(page: Int, search: String, status: String): ExternalUserPage
}
