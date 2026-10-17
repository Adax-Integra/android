package com.example.adaxintegra.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.repository.AuthRepository
import com.example.adaxintegra.domain.usecases.GetProfileByUserIdUseCase
import com.example.adaxintegra.domain.usecases.GetRecordsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

// NV-04: number of recent records shown in the internal home
private const val RECENT_RECORDS_LIMIT = 3

@HiltViewModel
class InternalHomeViewModel @Inject constructor(
    private val getRecordsUseCase: GetRecordsUseCase,
    private val getProfileByUserIdUseCase: GetProfileByUserIdUseCase,
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(InternalHomeUiState())
    val uiState = _uiState.asStateFlow()

    private var recordsJob: Job? = null
    private var nameJob: Job? = null

    // Called by the screen on every entry, so a record updated elsewhere moves to the top
    fun loadHome() {
        loadUserName()
        loadRecentRecords()
    }

    // The greeting falls back to "¡Hola!" when the name cannot be read,
    // so a failure here does not show an error on the screen
    private fun loadUserName() {
        // The name does not change while the user is signed in, so it is read only once
        if (_uiState.value.userName != null) {
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
                    _uiState.update { it.copy(userName = result.data.name) }
                }
            }
        }
    }

    fun loadRecentRecords() {
        // Cancel the previous request so older results cannot replace new ones
        recordsJob?.cancel()

        recordsJob = viewModelScope.launch {
            // The backend already sorts the records by their last update, newest first
            getRecordsUseCase(page = 1).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        // The pull indicator is already visible while refreshing,
                        // so the loading spinner is only shown on a normal load
                        _uiState.update {
                            it.copy(isLoading = !it.isRefreshing, error = null)
                        }
                    }

                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                recentRecords = result.data.records.take(RECENT_RECORDS_LIMIT),
                                isLoading = false,
                                isRefreshing = false,
                                error = null,
                            )
                        }
                    }

                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isRefreshing = false,
                                error = errorMessage(result.exception),
                            )
                        }
                    }
                }
            }
        }
    }

    // Pull to refresh: reloads the list without hiding the current records
    fun refresh() {
        val state = _uiState.value

        if (state.isLoading || state.isRefreshing) {
            return
        }

        _uiState.update { it.copy(isRefreshing = true) }
        loadRecentRecords()
    }

    private fun errorMessage(e: Throwable): String = when {
        e is HttpException && e.code() == 401 -> "Tu sesión expiró. Vuelve a iniciar sesión."
        e is IOException -> "Sin conexión a internet. Inténtalo de nuevo."
        else -> "No se pudieron cargar los expedientes."
    }
}
