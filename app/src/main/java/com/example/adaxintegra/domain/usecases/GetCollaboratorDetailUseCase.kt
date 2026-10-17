package com.example.adaxintegra.domain.usecases

import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.model.CollaboratorDetail
import com.example.adaxintegra.domain.repository.CollaboratorRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

// G-10: the admin consults the detail of one collaborator
class GetCollaboratorDetailUseCase @Inject constructor(
    private val repository: CollaboratorRepository,
) {
    operator fun invoke(userId: String): Flow<Result<CollaboratorDetail>> = flow {
        emit(Result.Loading)

        try {
            val detail = repository.getCollaboratorDetail(userId)
            emit(Result.Success(detail))
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            emit(Result.Error(exception))
        }
    }
}
