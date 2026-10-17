package com.example.adaxintegra.domain.usecases

import com.example.adaxintegra.domain.repository.ProfileRepository
import javax.inject.Inject

class UpdateProfileUseCase @Inject
constructor(
    private val profileRepository: ProfileRepository,
) {
    suspend operator fun invoke(userId: String, fields: Map<String, String>) {
        profileRepository.updateProfile(userId, fields)
    }
}
