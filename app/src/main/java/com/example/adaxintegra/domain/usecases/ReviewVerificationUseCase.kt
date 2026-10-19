package com.example.adaxintegra.domain.usecases

import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.repository.ExternalUserRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

// B-02: an internal user or admin approves or rejects the identity verification
// Emits the status the backend saved
class ReviewVerificationUseCase @Inject constructor(
    private val repository: ExternalUserRepository,
) {
    operator fun invoke(userId: String, status: String, reason: String?): Flow<Result<String>> = flow {
        emit(Result.Loading)

        try {
            val result = repository.reviewVerification(userId, status, reason)
            emit(Result.Success(result))
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            emit(Result.Error(exception))
        }
    }
}
