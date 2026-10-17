package com.example.adaxintegra.data.repository

import com.example.adaxintegra.data.mapper.toDomain
import com.example.adaxintegra.data.remote.api.ProfileApi
import com.example.adaxintegra.domain.model.EditableProfile
import com.example.adaxintegra.domain.model.ProfileView
import com.example.adaxintegra.domain.repository.AuthRepository
import com.example.adaxintegra.domain.repository.ProfileRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepositoryImpl @Inject constructor(
    private val api: ProfileApi,
    private val authRepository: AuthRepository,
) : ProfileRepository {

    private fun authorizationHeader(): String {
        val token = authRepository.session.value?.token

        if (token.isNullOrBlank()) {
            throw IllegalStateException(
                "Inicia sesión para consultar el perfil"
            )
        }

        return "Bearer $token"
    }

    override suspend fun getProfileByUserId(userId: String): ProfileView {
        val response = api.getProfile(
            userId = userId,
            authorization  = authorizationHeader())
        return response.data.toDomain()
    }

    override suspend fun getEditableProfile(userId: String): EditableProfile {
        val response = api.getEditableProfile(
            userId = userId,
            authorization  = authorizationHeader())
        return response.data.toDomain()
    }

    override suspend fun updateProfile(userId: String, fields: Map<String, String>) {
        api.updateProfile(
            userId = userId,
            authorization = authorizationHeader(),
            fields = fields)
        
    // G-08: only confirms the deactivation, the app closes the session after it
    override suspend fun deactivateAccount(userId: String) {
        api.deactivateAccount(
            userId = userId,
            authorization = authorizationHeader(),
        )
    }
}
