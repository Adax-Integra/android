package com.example.adaxintegra.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.model.Case
import com.example.adaxintegra.domain.usecases.GetExternalUserCasesUseCase
import com.example.adaxintegra.presentation.views.screens.cases.ExternalCasesUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// V-04: case list of the external user
@HiltViewModel
class ExternalCasesViewModel @Inject constructor(
    private val getExternalUserCasesUseCase: GetExternalUserCasesUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ExternalCasesUiState())
    val uiState: StateFlow<ExternalCasesUiState> = _uiState.asStateFlow()

    private var allCases: List<Case> = emptyList()

    init {
        loadCases()
    }

    private fun loadCases() {
        viewModelScope.launch {
            getExternalUserCasesUseCase(TEST_USER_ID).collect { result ->
                _uiState.update { state ->
                    when (result) {
                        is Result.Loading -> {
                            state.copy(isLoading = true, error = null)
                        }

                        is Result.Success -> {
                            // Acceptance criteria: sorted by last update.
                            allCases = result.data.sortedByDescending { it.updatedAt }
                            state.copy(cases = allCases, isLoading = false, error = null)
                        }

                        is Result.Error -> {
                            state.copy(
                                isLoading = false,
                                error = mensajeDeError(result.exception),
                            )
                        }
                    }
                }
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        val filtrados =
            allCases.filter { caso ->
                caso.caseId.contains(query, ignoreCase = true) ||
                    caso.violenceList.orEmpty().any { it.type.contains(query, ignoreCase = true) } ||
                    caso.description.orEmpty().contains(query, ignoreCase = true)
            }
        _uiState.update { it.copy(searchQuery = query, cases = filtrados) }
    }

    private fun mensajeDeError(e: Throwable): String = when (e) {
        is java.io.IOException -> "Sin conexión a internet. Revisa tu red e inténtalo de nuevo."
        else -> "Ocurrió un error al cargar tus casos."
    }

    companion object {
        // TODO: replace with the authenticated user's real userId once login is ready.
        // Temporary: external test user from the database, used to test V-04 locally.
        private const val TEST_USER_ID = "c4d665c3-56b7-4616-b322-69d3ffa261d4"
    }
}
