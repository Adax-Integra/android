package com.example.adaxintegra.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.repository.AuthRepository
import com.example.adaxintegra.domain.usecases.DeactivateAccountUseCase
import com.example.adaxintegra.domain.usecases.GetProfileByUserIdUseCase
import com.example.adaxintegra.presentation.views.screens.profile.ProfileUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getProfileByUserIdUseCase: GetProfileByUserIdUseCase,
    private val deactivateAccountUseCase: DeactivateAccountUseCase,
    private val authRepository: AuthRepository, // get session to get userId
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState(isLoading = true))
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun refresh() {
        val state = _uiState.value
        if (state.isRefreshing || state.isLoading) return
    }

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

    // G-08: the user deactivates her own account, the screen closes the session after it
    fun deactivateAccount() {
        if (_uiState.value.isDeactivating) return

        val userId = authRepository.session.value?.userId

        if (userId.isNullOrBlank()) {
            _uiState.update { it.copy(deactivateError = "No hay una sesión activa.") }
            return
        }

        viewModelScope.launch {
            deactivateAccountUseCase(userId).collect { result ->
                _uiState.update { state ->
                    when (result) {
                        is Result.Loading -> {
                            state.copy(
                                isDeactivating = true,
                                deactivateError = null,
                            )
                        }

                        is Result.Success -> {
                            state.copy(
                                isDeactivating = false,
                                isDeactivated = true,
                            )
                        }

                        is Result.Error -> {
                            val exception = result.exception

                            // 409: the account was already deactivated, so the session is closed too
                            if (exception is HttpException && exception.code() == 409) {
                                state.copy(
                                    isDeactivating = false,
                                    isDeactivated = true,
                                )
                            } else {
                                state.copy(
                                    isDeactivating = false,
                                    deactivateError = mensajeDeErrorDesactivar(exception),
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // G-08: clears the error when the confirmation dialog is closed
    fun dismissDeactivateError() {
        _uiState.update { it.copy(deactivateError = null) }
    }

    private fun mensajeDeError(e: Throwable): String =
        when (e) {
            is java.io.IOException ->
                "Sin conexión a internet. Revisa tu red e inténtalo de nuevo."

            else ->
                e.message.takeIf { !it.isNullOrBlank() } ?: "Ocurrió un error al cargar el perfil."
        }

    private fun mensajeDeErrorDesactivar(e: Throwable): String =
        when {
            e is HttpException && e.code() == 401 -> "Tu sesión expiró. Vuelve a iniciar sesión."
            e is HttpException && e.code() == 403 -> "Esta cuenta no se puede desactivar."
            e is IOException -> "Sin conexión a internet. Revisa tu red e inténtalo de nuevo."
            else -> "No se pudo desactivar la cuenta. Inténtalo de nuevo."
        }
}
