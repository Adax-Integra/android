package com.example.adaxintegra.domain.usecases

import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.model.Collaborator
import com.example.adaxintegra.domain.model.NewCollaborator
import com.example.adaxintegra.domain.repository.CollaboratorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

// G-03: the admin creates a new collaborator account
class CreateCollaboratorUseCase @Inject constructor(
    private val repository: CollaboratorRepository,
) {
    operator fun invoke(token: String, collaborator: NewCollaborator): Flow<Result<Collaborator>> = flow {
        try {
            emit(Result.Loading)
            val created = repository.createCollaborator(token, collaborator)
            emit(Result.Success(created))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }
}
