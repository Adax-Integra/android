package com.example.adaxintegra.domain.usecases

import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

// G-08: the external or internal user deactivates her own account
class DeactivateAccountUseCase
@Inject
constructor(
    private val profileRepository: ProfileRepository,
) {
    operator fun invoke(userId: String): Flow<Result<Unit>> =
        flow {
            emit(Result.Loading)
            try {
                profileRepository.deactivateAccount(userId)
                emit(Result.Success(Unit))
            } catch (e: Exception) {
                emit(Result.Error(e))
            }
        }
}
