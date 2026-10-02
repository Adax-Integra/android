package com.example.adaxintegra.domain.usecases

import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.model.ProfileView
import com.example.adaxintegra.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

//retrieve data and have message depending on case
class GetProfileByUserIdUseCase
@Inject
constructor(
    private val profileRepository: ProfileRepository,
) {
    operator fun invoke(userId: String): Flow<Result<ProfileView>> =
        flow {
            emit(Result.Loading)
            try {
                val profileData = profileRepository.getProfileByUserId(userId)
                emit(Result.Success(profileData))
            } catch (e: Exception) {
                emit(Result.Error(e))
            }
        }
}
