package com.example.adaxintegra.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.repository.AuthRepository
import com.example.adaxintegra.domain.usecases.GetProfileByUserIdUseCase
import com.example.adaxintegra.presentation.views.screens.profile.ProfileUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getProfileByUserIdUseCase: GetProfileByUserIdUseCase,
    private val authRepository: AuthRepository, // get session to get userId
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState(isLoading = true))
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun loadProfile() {
        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            authRepository.session.collect { session ->
                val userId = session?.userId

                // if not logged in, no user id is available, so msg appears
                if (userId.isNullOrBlank()) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = "No hay una sesión activa."
                        )
                    }
                    return@collect
                }

                getProfileByUserIdUseCase(userId).collect { result ->
                    _uiState.update { state ->
                        when (result) {
                            is Result.Loading -> {
                                state.copy(
                                    isLoading = true,
                                    error = null,
                                )
                            }

                            is Result.Success -> {
                                state.copy(
                                    profile = result.data,
                                    isLoading = false,
                                    error = null,
                                )
                            }

                            is Result.Error -> {
                                state.copy(
                                    error = mensajeDeError(result.exception),
                                    isLoading = false,
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun mensajeDeError(e: Throwable): String =
        when (e) {
            is java.io.IOException ->
                "Sin conexión a internet. Revisa tu red e inténtalo de nuevo."

            else ->
                e.message.takeIf { !it.isNullOrBlank() } ?: "Ocurrió un error al cargar el perfil."
        }
}
