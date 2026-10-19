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

    // Request in progress, so a new page or filter cancels the previous load
    private var loadJob: Job? = null

    init {
        loadPage(1)
    }

    // V-06: page buttons, same as the other lists
    fun nextPage() {
        val state = _uiState.value
        if (!state.canGoNext || state.isLoading) return
        loadPage(state.page + 1)
    }

    fun previousPage() {
        val state = _uiState.value
        if (!state.canGoBack || state.isLoading) return
        loadPage(state.page - 1)
    }

    // Tries again the page that could not be loaded
    fun retry() {
        loadPage(_uiState.value.page)
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    // A new filter starts again from the first page
    fun applyDateFilter(from: Date?, to: Date?) {
        _uiState.update { it.copy(fromDate = from, toDate = to) }
        loadPage(1)
    }

    fun clearDateFilter() {
        applyDateFilter(null, null)
    }

    // Loads one page of the log with the current date filter
    private fun loadPage(page: Int) {
        loadJob?.cancel()
        val state = _uiState.value

        loadJob = viewModelScope.launch {
            getActivityLogUseCase(page, state.fromDate, state.toDate).collect { result ->
                _uiState.update { current ->
                    when (result) {
                        is Result.Loading -> current.copy(isLoading = true, error = null)

                        is Result.Success -> current.copy(
                            entries = result.data.entries,
                            page = result.data.page,
                            total = result.data.total,
                            limit = result.data.limit,
                            isLoading = false,
                        )

                        // The page is kept, so "Reintentar" asks for the same page
                        is Result.Error -> current.copy(
                            entries = emptyList(),
                            page = page,
                            isLoading = false,
                            error = errorMessage(result.exception),
                        )
                    }
                }
            }
        }
    }

    private fun errorMessage(e: Throwable): String = when {
        e is HttpException && e.code() == 401 -> "Tu sesión expiró. Vuelve a iniciar sesión."
        e is HttpException && e.code() == 403 -> "No tienes permiso para ver la bitácora."
        e is HttpException && e.code() == 400 -> "Revisa las fechas del filtro."
        e is IOException -> "Sin conexión a internet. Inténtalo de nuevo."
        else -> "Ocurrió un error al cargar la bitácora."
    }
}
