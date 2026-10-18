package com.example.adaxintegra.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.usecases.AssignCollaboratorsToCaseUseCase
import com.example.adaxintegra.domain.usecases.AssignHelpsToCaseUseCase
import com.example.adaxintegra.domain.usecases.AssignViolenceToCaseUseCase
import com.example.adaxintegra.domain.usecases.CloseCaseUseCase
import com.example.adaxintegra.domain.usecases.GetCaseDetailUseCase
import com.example.adaxintegra.domain.usecases.GetCollaboratorsUseCase
import com.example.adaxintegra.domain.usecases.GetHelpTypesUseCase
import com.example.adaxintegra.domain.usecases.GetViolenceTypesUseCase
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
    private val getHelpTypesUseCase: GetHelpTypesUseCase,
    private val assignHelpsToCaseUseCase: AssignHelpsToCaseUseCase,
    private val getViolenceTypesUseCase: GetViolenceTypesUseCase,
    private val assignViolenceToCaseUseCase: AssignViolenceToCaseUseCase,
    private val getCollaboratorsUseCase: GetCollaboratorsUseCase,
    private val assignCollaboratorsToCaseUseCase: AssignCollaboratorsToCaseUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CaseDetailUiState())
    val uiState: StateFlow<CaseDetailUiState> = _uiState.asStateFlow()

    //loads the complete information of the selected case
    fun loadCase(caseId: String) {
        loadCaseInternal(caseId, isRefresh = false)
        loadHelpTypes()
        loadViolenceTypes()
        loadAssignableCollaborators()
    }

    private fun loadAssignableCollaborators() {
        viewModelScope.launch {
            getCollaboratorsUseCase().collect { result ->
                when (result) {
                    is Result.Success -> {
                        _uiState.update { it.copy(assignableCollaborators = result.data) }
                    }
                    is Result.Error -> {
                        println("Error loading collaborators: ${result.exception.message}")
                    }
                    else -> {}
                }
            }
        }
    }

    private fun loadHelpTypes() {
        viewModelScope.launch {
            getHelpTypesUseCase().collect { result ->
                when (result) {
                    is Result.Success -> {
                        _uiState.update { it.copy(availableHelps = result.data) }
                    }

                    is Result.Error -> {
                        println("Error loading help types: ${result.exception.message}")
                    }

                    else -> {}
                }
            }
        }
    }

    private fun loadViolenceTypes() {
        viewModelScope.launch {
            getViolenceTypesUseCase().collect { result ->
                when (result) {
                    is Result.Success -> {
                        _uiState.update { it.copy(availableViolences = result.data) }
                    }

                    is Result.Error -> {
                        println("Error loading violence types: ${result.exception.message}")
                    }

                    else -> {}
                }
            }
        }
    }

    //re-fetches case without hiding current content
    fun refresh(caseId: String) {
        val state = _uiState.value
        if (state.isRefreshing || state.isLoading) return
        loadCaseInternal(caseId, isRefresh = true)
    }

    private fun loadCaseInternal(caseId: String, isRefresh: Boolean) {
        viewModelScope.launch {
            getCaseDetailUseCase(caseId).collect { result ->
                _uiState.update { state ->
                    when (result) {
                        is Result.Loading -> {
                            if (isRefresh) {
                                state.copy(isRefreshing = true)
                            } else {
                                state.copy(isLoading = true, error = null)
                            }
                        }

                        is Result.Success -> {
                            state.copy(
                                case = result.data,
                                isLoading = false,
                                isRefreshing = false,
                                error = null,
                            )
                        }

                        is Result.Error -> {
                            if (isRefresh) {
                                state.copy(isRefreshing = false)
                            } else {
                                state.copy(
                                    isLoading = false,
                                    isRefreshing = false,
                                    error = "No fue posible cargar el detalle del caso.",
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun expandHelp() {
        _uiState.update { it.copy(isHelpExpanded = true) }
    }

    private fun collapseHelp() {
        _uiState.update {
            it.copy(isHelpExpanded = false)
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

    fun assignCollaborators(caseId: String, newIds: List<String>) {
        if (newIds.isEmpty()) return
        viewModelScope.launch {
            assignCollaboratorsToCaseUseCase(caseId, newIds).collect { result ->
                _uiState.update { state ->
                    when (result) {
                        is Result.Loading -> {
                            state.copy(
                                isAssigningCollaborators = true,
                                assignCollaboratorsError = null,
                            )
                        }

                        is Result.Success -> {
                            state.copy(
                                isAssigningCollaborators = false,
                                assignCollaboratorsError = null,
                                assignedCollaboratorIds = state.assignedCollaboratorIds + newIds,
                            )
                        }

                        is Result.Error -> {
                            state.copy(
                                isAssigningCollaborators = false,
                                assignCollaboratorsError = "No fue posible guardar las encargadas asignadas.",
                            )
                        }
                    }
                }
            }
        }
    }

    fun assignHelps(caseId: String, helpIds: List<String>) {
        viewModelScope.launch {
            assignHelpsToCaseUseCase(caseId, helpIds).collect { result ->
                _uiState.update { state ->
                    when (result) {
                        is Result.Loading -> {
                            state.copy(
                                isAssigningHelps = true,
                                assignHelpsError = null,
                            )
                        }

                        is Result.Success -> {
                            state.copy(
                                isAssigningHelps = false,
                                assignHelpsError = null,
                            )
                        }

                        is Result.Error -> {
                            state.copy(
                                isAssigningHelps = false,
                                assignHelpsError = "No fue posible guardar la ayuda asignada.",
                            )
                        }
                    }
                }

                // If the request was successful, refresh the case detail
                if (result is Result.Success) {
                    refresh(caseId)
                }
            }
        }
    }

    fun assignViolences(caseId: String, violenceIds: List<String>) {
        viewModelScope.launch {
            assignViolenceToCaseUseCase(caseId, violenceIds).collect { result ->
                _uiState.update { state ->
                    when (result) {
                        is Result.Loading -> {
                            state.copy(
                                isAssigningViolences = true,
                                assignViolencesError = null,
                            )
                        }

                        is Result.Success -> {
                            state.copy(
                                isAssigningViolences = false,
                                assignViolencesError = null,
                            )
                        }

                        is Result.Error -> {
                            state.copy(
                                isAssigningViolences = false,
                                assignViolencesError = "No fue posible guardar la violencia asignada.",
                            )
                        }
                    }
                }

                // If the request was successful, refresh the case detail
                if (result is Result.Success) {
                    refresh(caseId)
                }
            }
        }
    }
}
