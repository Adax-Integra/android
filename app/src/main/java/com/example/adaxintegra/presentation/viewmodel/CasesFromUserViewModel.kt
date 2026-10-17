package com.example.adaxintegra.presentation.viewmodel

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
) : ViewModel() {

    private val _uiState = MutableStateFlow(CasesFromUserUiState())
    val uiState: StateFlow<CasesFromUserUiState> = _uiState.asStateFlow()

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
                                error = "Error",
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
            val result = updateExpedientStatusUseCase(recordId, newStatus.label)
            _uiState.update { state ->
                if (result.isSuccess) {
                    state.copy(
                        currentStatus = newStatus,
                        isUpdatingStatus = false,
                        error = null,
                    )
                } else {
                    state.copy(
                        isUpdatingStatus = false,
                        error = "No se pudo actualizar el estatus",
                    )
                }
            }
        }
    }
}
