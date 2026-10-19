package com.example.adaxintegra.domain.usecases

import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.model.ExternalUserProfile
import com.example.adaxintegra.domain.repository.ExternalUserRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

// G-07: retrieve an external user's profile
class GetExternalProfileUseCase @Inject constructor(
    private val repository: ExternalUserRepository,
) {
    operator fun invoke(userId: String): Flow<Result<ExternalUserProfile>> = flow {
        emit(Result.Loading)

        try {
            val result = repository.getExternalProfile(userId)
            emit(Result.Success(result))
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            emit(Result.Error(exception))
        }
    }
}
