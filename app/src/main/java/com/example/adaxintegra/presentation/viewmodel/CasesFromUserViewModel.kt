package com.example.adaxintegra.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.entities.ExpedientStatus
import com.example.adaxintegra.domain.usecases.GetCasesFromUserUseCase
import com.example.adaxintegra.domain.usecases.UpdateExpedientStatusUseCase
import com.example.adaxintegra.presentation.mapper.toUiEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CasesFromUserViewModel @Inject constructor(
    private val getCasesFromUserUseCase: GetCasesFromUserUseCase,
    private val updateExpedientStatusUseCase: UpdateExpedientStatusUseCase,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CasesFromUserUiState())
    val uiState: StateFlow<CasesFromUserUiState> = _uiState.asStateFlow()

    init {
        val statusArg = savedStateHandle.get<String>("status")
        if (!statusArg.isNullOrBlank()) {
            val mappedStatus = ExpedientStatus.entries.find {
                it.name.equals(statusArg, ignoreCase = true) || it.code.equals(statusArg, ignoreCase = true)
            }
            if (mappedStatus != null) {
                _uiState.update { it.copy(currentStatus = mappedStatus) }
            }
        }
    }

    fun loadCasesFromUser(userId: String) {
        viewModelScope.launch {
            getCasesFromUserUseCase(userId).collect { result ->
                _uiState.update { state ->
                    when (result) {
                        is Result.Loading -> {
                            state.copy(
                                isLoading = true,
                                error = null,
                            )
                        }

                        is Result.Success -> {
                            val backendCases = result.data
                            val uiCases = backendCases.map { it.toUiEntity() }
                            state.copy(
                                cases = uiCases,
                                isLoading = false,
                                error = null,
                            )
                        }

                        is Result.Error -> {
                            state.copy(
                                error = "Error al cargar casos",
                                isLoading = false,
                            )
                        }
                    }
                }
            }
        }
    }

    fun updateStatus(recordId: String, newStatus: ExpedientStatus) {
        viewModelScope.launch {
            _uiState.update { it.copy(isUpdatingStatus = true) }

            val result = updateExpedientStatusUseCase(recordId = recordId, status = newStatus.code)

            result.onSuccess {
                savedStateHandle["status"] = newStatus.code
                savedStateHandle["initialStatus"] = newStatus.code

                _uiState.update { currentState ->
                    currentState.copy(
                        isUpdatingStatus = false,
                        currentStatus = newStatus,
                        error = null,
                    )
                }
            }.onFailure { error ->
                val rawMessage = error.message ?: ""

                val friendlyErrorMessage = if (rawMessage.contains("409") || rawMessage.contains("Conflict", ignoreCase = true)) {
                    "Transición de estatus no permitida. No es posible realizar este cambio."
                } else {
                    rawMessage.ifBlank { "No se pudo actualizar el estatus" }
                }

                _uiState.update { currentState ->
                    currentState.copy(
                        isUpdatingStatus = false,
                        isLoading = false,
                        error = friendlyErrorMessage,
                    )
                }
            }
        }
    }
}
