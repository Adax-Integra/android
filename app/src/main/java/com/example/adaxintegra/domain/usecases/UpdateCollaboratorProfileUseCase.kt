package com.example.adaxintegra.domain.usecases

import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.repository.CollaboratorRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateCollaboratorProfileUseCase @Inject constructor(
    private val repository: CollaboratorRepository,
) {
    operator fun invoke(
        userId: String,
        name: String?,
        lastName: String?,
        email: String?,
    ): Flow<Result<Unit>> = flow {
        emit(Result.Loading)
        try {
            repository.updateCollaboratorProfile(
                userId = userId,
                name = name,
                lastName = lastName,
                email = email,
            )
            emit(Result.Success(Unit))
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            emit(Result.Error(exception))
        }
    }
}