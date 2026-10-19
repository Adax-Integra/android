package com.example.adaxintegra.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.repository.AuthRepository
import com.example.adaxintegra.domain.usecases.GetProfileByUserIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// NV-01: name of the external user for the greeting of her home, same as the internal home
@HiltViewModel
class ExternalHomeViewModel @Inject constructor(
    private val getProfileByUserIdUseCase: GetProfileByUserIdUseCase,
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _userName = MutableStateFlow<String?>(null)
    val userName: StateFlow<String?> = _userName.asStateFlow()

    private var nameJob: Job? = null

    // The greeting falls back to "¡Hola!" when the name cannot be read,
    // so a failure here does not show an error on the screen
    fun loadUserName() {
        // The name does not change while the user is signed in, so it is read only once
        if (_userName.value != null) {
            return
        }

        val userId = authRepository.session.value?.userId

        if (userId.isNullOrBlank()) {
            return
        }

        nameJob?.cancel()

        nameJob = viewModelScope.launch {
            getProfileByUserIdUseCase(userId).collect { result ->
                if (result is Result.Success) {
                    _userName.value = result.data.name
                }
            }
        }
    }
}
