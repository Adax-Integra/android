package com.example.adaxintegra.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.usecases.GetCaseByIdUseCase
import com.example.adaxintegra.presentation.views.screens.cases.CaseProgressUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CaseProgressViewModel
    @Inject
    constructor(
        private val getCaseByIdUseCase: GetCaseByIdUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(CaseProgressUiState())
        val uiState: StateFlow<CaseProgressUiState> = _uiState.asStateFlow()

        fun loadCase(caseId: String) {
            viewModelScope.launch {
                getCaseByIdUseCase(caseId).collect { result ->
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
                                    case = result.data,
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

        private fun mensajeDeError(e: Throwable): String =
            when (e) {
                is java.io.IOException -> "Sin conexión a internet. Revisa tu red e inténtalo de nuevo."
                else -> "Ocurrió un error al cargar el caso."
            }
    }
