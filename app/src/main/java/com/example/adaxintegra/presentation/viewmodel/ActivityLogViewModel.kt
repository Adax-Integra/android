package com.example.adaxintegra.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.usecases.GetActivityLogUseCase
import com.example.adaxintegra.presentation.views.screens.admin.ActivityLogUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.util.Date
import javax.inject.Inject

// V-06: the admin consults the activity log of the system
@HiltViewModel
class ActivityLogViewModel @Inject constructor(
    private val getActivityLogUseCase: GetActivityLogUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ActivityLogUiState())
    val uiState: StateFlow<ActivityLogUiState> = _uiState.asStateFlow()

    // Last page received from the backend
    private var currentPage = 0

    // Request in progress, so a new filter cancels the previous load
    private var loadJob: Job? = null

    init {
        loadFirstPage()
    }

    // Loads the log from the beginning with the current date filter
    fun loadFirstPage() {
        loadJob?.cancel()
        val state = _uiState.value

        loadJob = viewModelScope.launch {
            getActivityLogUseCase(1, state.fromDate, state.toDate).collect { result ->
                _uiState.update { current ->
                    when (result) {
                        // V-06 fix: starts from zero, so entries of the previous filter are not
                        // shown and a cancelled "load more" does not block the next pages
                        is Result.Loading -> current.copy(
                            entries = emptyList(),
                            hasNextPage = false,
                            isLoading = true,
                            isLoadingMore = false,
                            error = null,
                        )

                        is Result.Success -> {
                            currentPage = result.data.page
                            current.copy(
                                entries = result.data.entries,
                                hasNextPage = result.data.hasNextPage,
                                isLoading = false,
                            )
                        }

                        is Result.Error -> current.copy(
                            isLoading = false,
                            error = errorMessage(result.exception),
                        )
                    }
                }
            }
        }
    }

    // Acceptance criteria: scroll without problems, the next page loads at the end of the list
    fun loadNextPage() {
        val state = _uiState.value
        if (!state.hasNextPage || state.isLoading || state.isLoadingMore) return

        loadJob = viewModelScope.launch {
            getActivityLogUseCase(currentPage + 1, state.fromDate, state.toDate).collect { result ->
                _uiState.update { current ->
                    when (result) {
                        is Result.Loading -> current.copy(isLoadingMore = true, error = null)
                        is Result.Success -> {
                            currentPage = result.data.page
                            current.copy(
                                entries = current.entries + result.data.entries,
                                hasNextPage = result.data.hasNextPage,
                                isLoadingMore = false,
                            )
                        }

                        is Result.Error -> current.copy(
                            isLoadingMore = false,
                            error = errorMessage(result.exception),
                        )
                    }
                }
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun applyDateFilter(from: Date?, to: Date?) {
        _uiState.update { it.copy(fromDate = from, toDate = to) }
        loadFirstPage()
    }

    fun clearDateFilter() {
        applyDateFilter(null, null)
    }

    private fun errorMessage(e: Throwable): String = when {
        e is HttpException && e.code() == 401 -> "Tu sesión expiró. Vuelve a iniciar sesión."
        e is HttpException && e.code() == 403 -> "No tienes permiso para ver la bitácora."
        e is HttpException && e.code() == 400 -> "Revisa las fechas del filtro."
        e is IOException -> "Sin conexión a internet. Inténtalo de nuevo."
        else -> "Ocurrió un error al cargar la bitácora."
    }
}
