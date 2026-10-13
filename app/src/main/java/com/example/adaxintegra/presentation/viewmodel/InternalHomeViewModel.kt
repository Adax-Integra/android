package com.example.adaxintegra.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.usecases.GetStaleCasesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

// NV-04: number of stale cases shown in the internal home
private const val STALE_CASES_LIMIT = 5

@HiltViewModel
class InternalHomeViewModel @Inject constructor(
    private val getStaleCasesUseCase: GetStaleCasesUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(InternalHomeUiState())
    val uiState = _uiState.asStateFlow()

    private var loadJob: Job? = null

    // Called by the screen on every entry, so a case updated elsewhere leaves the list.
    fun loadStaleCases() {
        // Cancel the previous request so older results cannot replace new ones.
        loadJob?.cancel()

        loadJob = viewModelScope.launch {
            getStaleCasesUseCase(limit = STALE_CASES_LIMIT).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.update {
                            it.copy(isLoading = true, error = null)
                        }
                    }

                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                staleCases = result.data,
                                isLoading = false,
                                error = null,
                            )
                        }
                    }

                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = errorMessage(result.exception),
                            )
                        }
                    }
                }
            }
        }
    }

    private fun errorMessage(e: Throwable): String = when {
        e is HttpException && e.code() == 401 -> "Tu sesión expiró. Vuelve a iniciar sesión."
        e is IOException -> "Sin conexión a internet. Inténtalo de nuevo."
        else -> "No se pudieron cargar los expedientes."
    }
}
