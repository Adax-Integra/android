package com.example.adaxintegra.data.repository

import com.example.adaxintegra.data.mapper.toDomain
import com.example.adaxintegra.data.remote.api.ProfileApi
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
}
