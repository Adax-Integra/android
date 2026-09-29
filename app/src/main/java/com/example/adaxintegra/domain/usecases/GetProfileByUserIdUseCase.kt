package com.example.adaxintegra.domain.usecases

import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

//retrieve data and have message depending on case
class GetProfileByUserId
@Inject
constructor(
    private val profileRepository: ProfileRepository,
) {
    operator fun invoke(userId: String): Flow<Result<Profile>> =
        flow {
            emit(Result.Loading)
            try {
                val profile = profileRepository.getProfileByUserId(userId)
                emit(Result.Success(profile))
            } catch (e: Exception) {
                emit(Result.Error(e))
            }
        }
}
