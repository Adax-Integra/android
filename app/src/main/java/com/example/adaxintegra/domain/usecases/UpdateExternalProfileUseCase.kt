package com.example.adaxintegra.domain.usecases

import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.model.ExternalUserProfile
import com.example.adaxintegra.domain.model.ExternalUserProfileUpdate
import com.example.adaxintegra.domain.repository.ExternalUserRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

// G-07: update an external user's editable profile data
class UpdateExternalProfileUseCase @Inject constructor(
    private val repository: ExternalUserRepository,
) {
    operator fun invoke(userId: String, update: ExternalUserProfileUpdate): Flow<Result<ExternalUserProfile>> = flow {
        emit(Result.Loading)

        try {
            val result = repository.updateExternalProfile(userId, update)
            emit(Result.Success(result))
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            emit(Result.Error(exception))
        }
    }
}
