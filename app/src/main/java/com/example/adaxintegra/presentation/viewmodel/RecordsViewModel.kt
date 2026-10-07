package com.example.adaxintegra.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.usecases.GetRecordsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecordsViewModel @Inject constructor(
    private val getRecordsUseCase: GetRecordsUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(RecordsUiState())
    val uiState = _uiState.asStateFlow()

    // Tracks the loading coroutine so it can be cancelled before a new request.
    private var loadJob: Job? = null

    init {
        loadRecords()
    }

    // Restart pagination when the search changes, preserving both filters.
    fun searchRecords(search: String) {
        _uiState.update {
            it.copy(search = search, page = 1)
        }
        loadRecords()
    }

    // Apply both selections together when the user confirms the filters.
    fun applyFilters(hasOpenCases: Boolean?, status: String?) {
        _uiState.update {
            it.copy(
                hasOpenCases = hasOpenCases,
                status = status,
                page = 1,
            )
        }
        loadRecords()
    }

    // Reset search and filters to show the complete listing.
    fun clearFilters() {
        _uiState.update {
            it.copy(
                search = "",
                hasOpenCases = null,
                status = null,
                page = 1,
            )
        }
        loadRecords()
    }

    fun nextPage() {
        val state = _uiState.value
        // Example: 2 * 10 < 15 -> It's the last one
        val hasNextPage = state.page.toLong() * state.limit < state.total

        if (!state.isLoading && state.error == null && hasNextPage) {
            _uiState.update { it.copy(page = it.page + 1) }
            loadRecords()
        }
    }

    fun previousPage() {
        val state = _uiState.value

        if (!state.isLoading && state.page > 1) {
            _uiState.update { it.copy(page = it.page - 1) }
            loadRecords()
        }
    }

    // Retry the current page using the same search and filters.
    fun retry() {
        loadRecords()
    }

    private fun loadRecords() {
        // Cancel the previous request so old results cannot replace new ones.
        loadJob?.cancel()

        val request = _uiState.value

        // Clear old results while loading a different page or selection.
        _uiState.update {
            it.copy(
                records = emptyList(),
                total = 0,
                isLoading = true,
                error = null,
            )
        }

        loadJob = viewModelScope.launch {
            getRecordsUseCase(
                page = request.page,
                search = request.search,
                hasOpenCases = request.hasOpenCases,
                status = request.status,
            ).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.update {
                            it.copy(isLoading = true, error = null)
                        }
                    }

                    is Result.Success -> {
                        val data = result.data

                        // Keep the order and pagination calculated by the backend.
                        _uiState.update {
                            it.copy(
                                records = data.records,
                                total = data.total,
                                page = data.page,
                                limit = data.limit,
                                isLoading = false,
                                error = null,
                            )
                        }
                    }

                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = "No se pudieron cargar los expedientes. Intenta nuevamente.",
                            )
                        }
                    }
                }
            }
        }
    }
}
