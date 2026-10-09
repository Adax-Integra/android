package com.example.adaxintegra.domain.usecases

import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.model.CollaboratorSummary
import com.example.adaxintegra.domain.repository.CollaboratorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

// G-06: the admin consults every collaborator account
class GetCollaboratorsUseCase @Inject constructor(
    private val repository: CollaboratorRepository,
) {
    operator fun invoke(): Flow<Result<List<CollaboratorSummary>>> = flow {
        try {
            emit(Result.Loading)
            val collaborators = repository.getCollaborators()
            emit(Result.Success(collaborators))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }
}
