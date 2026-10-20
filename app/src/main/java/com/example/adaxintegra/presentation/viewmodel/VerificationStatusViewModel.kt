package com.example.adaxintegra.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.repository.AuthRepository
import com.example.adaxintegra.domain.usecases.GetProfileByUserIdUseCase
import com.example.adaxintegra.presentation.views.designsystem.atoms.VerificationStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// B-01: verification status of the external user for the notice of the home
// It reads the same profile as the profile tab (GET /api/profile/:userId)
@HiltViewModel
class VerificationStatusViewModel @Inject constructor(
    private val getProfileByUserIdUseCase: GetProfileByUserIdUseCase,
    private val authRepository: AuthRepository,
) : ViewModel() {
    // Null until the first answer, the home does not show the notice without it
    private val _status = MutableStateFlow<VerificationStatus?>(null)
    val status: StateFlow<VerificationStatus?> = _status.asStateFlow()

    private var loadJob: Job? = null

    // Called on every entry to the home, so the notice changes after uploading the documents
    // A failed load keeps the last status
    fun load() {
        val userId = authRepository.session.value?.userId
        if (userId.isNullOrBlank()) {
            return
        }

        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            getProfileByUserIdUseCase(userId).collect { result ->
                if (result is Result.Success) {
                    _status.value = VerificationStatus.from(result.data.verificationStatus)
                }
            }
        }
    }
}
