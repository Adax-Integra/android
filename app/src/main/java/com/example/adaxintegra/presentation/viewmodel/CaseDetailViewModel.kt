package com.example.adaxintegra.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.usecases.AssignCollaboratorsToCaseUseCase
import com.example.adaxintegra.domain.usecases.GetAssignableCaseCollaboratorsUseCase
import com.example.adaxintegra.domain.usecases.AssignHelpsToCaseUseCase
import com.example.adaxintegra.domain.usecases.AssignViolenceToCaseUseCase
import com.example.adaxintegra.domain.usecases.CloseCaseUseCase
import com.example.adaxintegra.domain.usecases.GetCaseDetailUseCase
import com.example.adaxintegra.domain.usecases.GetHelpTypesUseCase
import com.example.adaxintegra.domain.usecases.GetViolenceTypesUseCase
import com.example.adaxintegra.domain.usecases.UpdateCaseStepUseCase
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
    private val getAssignableCaseCollaboratorsUseCase: GetAssignableCaseCollaboratorsUseCase,
    private val assignCollaboratorsToCaseUseCase: AssignCollaboratorsToCaseUseCase,
    private val updateCaseStepUseCase: UpdateCaseStepUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CaseDetailUiState())
    val uiState: StateFlow<CaseDetailUiState> = _uiState.asStateFlow()

    //loads the complete information of the selected case
    fun loadCase(caseId: String) {
        loadCaseInternal(caseId, isRefresh = false)
        loadHelpTypes()
        loadViolenceTypes()
    }

    fun loadAssignableCaseCollaborators() {
        val state = _uiState.value

        if (state.isLoadingAssignableCollaborators) return

        if (state.case?.assignmentsLoaded != true) {
            _uiState.update {
                it.copy(assignableCollaboratorsError = "No se pudieron verificar las asignaciones del caso.")
            }
            return
        }

        viewModelScope.launch {
            getAssignableCaseCollaboratorsUseCase().collect { result ->
                _uiState.update { current ->
                    when (result) {
                        is Result.Loading -> current.copy(
                            isLoadingAssignableCollaborators = true,
                            assignableCollaboratorsLoaded = false,
                            assignableCollaboratorsError = null,
                            collaboratorsSaved = false,
                        )
                        is Result.Success -> current.copy(
                            assignableCollaborators = result.data,
                            isLoadingAssignableCollaborators = false,
                            assignableCollaboratorsLoaded = true,
                            assignableCollaboratorsError = null,
                        )
                        is Result.Error -> current.copy(
                            isLoadingAssignableCollaborators = false,
                            assignableCollaboratorsLoaded = false,
                            assignableCollaboratorsError = "No fue posible cargar las colaboradoras.",
                        )
                    }
                }
            }
        }
    }

    fun saveCaseCollaborators(caseId: String, userIds: Set<String>) {
        val state = _uiState.value
        if (state.isSavingCollaborators) return

        val case = state.case
        val isClosed = case?.state?.trim()?.lowercase() in setOf("closed", "cerrado")

        if (case == null || case.caseId != caseId ||
            !case.assignmentsLoaded || !state.assignableCollaboratorsLoaded || isClosed
        ) {
            _uiState.update {
                it.copy(saveCollaboratorsError = "No es posible actualizar las asignaciones de este caso.")
            }
            return
        }

        viewModelScope.launch {
            assignCollaboratorsToCaseUseCase(caseId, userIds.toList()).collect { result ->
                _uiState.update { current ->
                    when (result) {
                        is Result.Loading -> current.copy(
                            isSavingCollaborators = true,
                            saveCollaboratorsError = null,
                            collaboratorsSaved = false,
                        )
                        is Result.Success -> current.copy(
                            isSavingCollaborators = false,
                            saveCollaboratorsError = null,
                            collaboratorsSaved = true,
                        )
                        is Result.Error -> current.copy(
                            isSavingCollaborators = false,
                            saveCollaboratorsError = "No fue posible guardar las asignaciones.",
                            collaboratorsSaved = false,
                        )
                    }
                }

                if (result is Result.Success) {
                    loadCaseInternal(caseId, isRefresh = true)
                }
            }
        }
    }

    fun clearCollaboratorsFeedback() {
        _uiState.update {
            it.copy(
                collaboratorsSaved = false,
                saveCollaboratorsError = null,
            )
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

    //updates case step
    fun updateCaseStep(caseId: String, stepNumber: Int) {
        if (_uiState.value.isUpdatingStep) return

        viewModelScope.launch {
            updateCaseStepUseCase(caseId, stepNumber).collect { result ->
                _uiState.update { state ->
                    when (result) {
                        is Result.Loading -> {
                            state.copy(
                                isUpdatingStep = true,
                                updateStepSuccess = false,
                                updateStepError = null,
                            )
                        }

                        is Result.Success -> {
                            state.copy(
                                case = result.data,
                                isUpdatingStep = false,
                                updateStepSuccess = true,
                                updateStepError = null,
                            )
                        }

                        is Result.Error -> {
                            state.copy(
                                isUpdatingStep = false,
                                updateStepSuccess = false,
                                updateStepError =
                                    "No se pudo actualizar el progreso del caso.",
                            )
                        }
                    }
                }
            }
        }
    }
}
