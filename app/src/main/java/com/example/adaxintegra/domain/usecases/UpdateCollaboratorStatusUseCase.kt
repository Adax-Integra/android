package com.example.adaxintegra.domain.usecases

import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.repository.CollaboratorRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

// G-04: the admin activates or deactivates a collaborator account
class UpdateCollaboratorStatusUseCase @Inject constructor(
    private val repository: CollaboratorRepository,
) {
    operator fun invoke(userId: String, isActive: Boolean): Flow<Result<Boolean>> = flow {
        emit(Result.Loading)

        try {
            val newStatus = repository.updateCollaboratorStatus(userId, isActive)
            emit(Result.Success(newStatus))
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            emit(Result.Error(exception))
        }
    }
}
