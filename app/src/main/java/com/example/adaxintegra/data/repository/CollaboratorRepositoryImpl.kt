package com.example.adaxintegra.data.repository

import com.example.adaxintegra.data.mapper.toDomain
import com.example.adaxintegra.data.mapper.toRequestDto
import com.example.adaxintegra.data.remote.api.CollaboratorApi
import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.model.Collaborator
import com.example.adaxintegra.domain.model.NewCollaborator
import com.example.adaxintegra.domain.repository.CollaboratorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

// G-03: sends the new collaborator to the backend and reports the state
@Singleton
class CollaboratorRepositoryImpl @Inject constructor(
    private val api : CollaboratorApi,
) : CollaboratorRepository {
    override fun createCollaborator(
        token: String,
        collaborator : NewCollaborator,
    ): Flow<Result<Collaborator>> = flow {
        emit(Result.Loading)
        try {
            val response = api.createCollaborator("Bearer $token", collaborator.toRequestDto())
            val created = response.data
            if (created == null) {
                emit(Result.Error(Exception("Empty response from server")))
            } else {
                emit(Result.Success(created.toDomain()))
            }
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }
}
