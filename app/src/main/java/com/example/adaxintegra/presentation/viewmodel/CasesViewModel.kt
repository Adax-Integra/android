package com.example.adaxintegra.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.usecases.GetCasesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CasesViewModel @Inject constructor(
    private val getCasesUseCase: GetCasesUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CasesUiState())
    val uiState = _uiState.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadCases()
    }

    fun searchCases(search: String) {
        _uiState.update {
            it.copy(search = search, page = 1, total = 0)
        }
        loadCases()
    }

    fun filterCases(urgency: String) {
        _uiState.update {
            it.copy(urgency = urgency, page = 1, total = 0)
        }
        loadCases()
    }

    fun clearFilters() {
        _uiState.update {
            it.copy(search = "", urgency = "", page = 1, total = 0)
        }
        loadCases()
    }

    fun nextPage() {
        val state = _uiState.value
        val hasNextPage = state.page.toLong() * state.limit < state.total

        if (!state.isLoading && hasNextPage) {
            _uiState.update { it.copy(page = it.page + 1) }
            loadCases()
        }
    }

    fun previousPage() {
        val state = _uiState.value

        if (!state.isLoading && state.page > 1) {
            _uiState.update { it.copy(page = it.page - 1) }
            loadCases()
        }
    }

    fun retry() {
        loadCases()
    }

    private fun loadCases() {
        // Cancel the previous request so older results cannot replace new ones.
        loadJob?.cancel()

        val request = _uiState.value

        _uiState.update {
            it.copy(
                cases = emptyList(),
                isLoading = true,
                error = null,
            )
        }

        loadJob = viewModelScope.launch {
            getCasesUseCase(
                search = request.search,
                urgency = request.urgency,
                page = request.page,
                limit = request.limit,
            ).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.update {
                            it.copy(isLoading = true, error = null)
                        }
                    }

                    is Result.Success -> {
                        val data = result.data

                        _uiState.update {
                            it.copy(
                                cases = data.cases,
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
                                error = "No se pudieron cargar los casos. Intenta nuevamente.",
                            )
                        }
                    }
                }
            }
        }
    }
}
