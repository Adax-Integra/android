package com.example.adaxintegra.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.usecases.CloseCaseUseCase
import com.example.adaxintegra.domain.usecases.GetCaseDetailUseCase
import com.example.adaxintegra.presentation.views.screens.cases.CaseDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// manages the state required by the V-11 case detail screen
@HiltViewModel
class CaseDetailViewModel
@Inject
constructor(
    private val getCaseDetailUseCase: GetCaseDetailUseCase,
    private val closeCaseUseCase: CloseCaseUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CaseDetailUiState())
    val uiState: StateFlow<CaseDetailUiState> = _uiState.asStateFlow()

    // loads the complete information of the selected case
    fun loadCase(caseId: String) {
        viewModelScope.launch {
            getCaseDetailUseCase(caseId).collect { result ->
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
                                isLoading = false,
                                error = "No fue posible cargar el detalle del caso.",
                            )
                        }
                    }
                }
            }
        }
    }

    // closes the selected case and refreshes its information
    fun closeCase(caseId: String) {
        viewModelScope.launch {
            closeCaseUseCase(caseId).collect { result ->
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
                                isLoading = false,
                                error = "No fue posible cerrar el caso.",
                            )
                        }
                    }
                }
            }
        }
    }
}
