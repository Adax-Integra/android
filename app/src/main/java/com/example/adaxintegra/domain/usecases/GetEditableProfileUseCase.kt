package com.example.adaxintegra.domain.usecases

import com.example.adaxintegra.domain.repository.ProfileRepository
import javax.inject.Inject

class GetEditableProfileUseCase @Inject
constructor(
    private val profileRepository: ProfileRepository,
) {
    suspend operator fun invoke(userId: String) =
        profileRepository.getEditableProfile(userId)
}
