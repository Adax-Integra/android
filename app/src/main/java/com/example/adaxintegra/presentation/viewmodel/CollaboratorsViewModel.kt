package com.example.adaxintegra.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.usecases.GetCollaboratorsUseCase
import com.example.adaxintegra.presentation.model.CollaboratorStatusFilter
import com.example.adaxintegra.presentation.views.screens.admin.CollaboratorsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

// G-06: the admin consults the list of collaborator accounts
@HiltViewModel
class CollaboratorsViewModel @Inject constructor(
    private val getCollaboratorsUseCase: GetCollaboratorsUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CollaboratorsUiState())
    val uiState: StateFlow<CollaboratorsUiState> = _uiState.asStateFlow()

    // Request in progress, so a reload cancels the previous one
    private var loadJob: Job? = null

    init {
        loadCollaborators()
    }

    // Also called after a new collaborator is created (G-03)
    fun loadCollaborators() {
        loadJob?.cancel()

        loadJob = viewModelScope.launch {
            getCollaboratorsUseCase().collect { result ->
                _uiState.update { current ->
                    when (result) {
                        is Result.Loading -> current.copy(isLoading = true, error = null)

                        is Result.Success -> current.copy(
                            collaborators = result.data,
                            isLoading = false,
                        )

                        is Result.Error -> current.copy(
                            isLoading = false,
                            error = errorMessage(result.exception),
                        )
                    }
                }
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun applyStatusFilter(filter: CollaboratorStatusFilter) {
        _uiState.update { it.copy(statusFilter = filter) }
    }

    fun clearStatusFilter() {
        applyStatusFilter(CollaboratorStatusFilter.ALL)
    }

    private fun errorMessage(e: Throwable): String = when {
        e is HttpException && e.code() == 401 -> "Tu sesión expiró. Vuelve a iniciar sesión."
        e is HttpException && e.code() == 403 -> "No tienes permiso para ver las colaboradoras."
        e is IOException -> "Sin conexión a internet. Inténtalo de nuevo."
        else -> "Ocurrió un error al cargar las colaboradoras."
    }
}
