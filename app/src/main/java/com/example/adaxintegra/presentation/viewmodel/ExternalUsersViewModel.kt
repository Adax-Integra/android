package com.example.adaxintegra.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.usecases.GetExternalUsersUseCase
import com.example.adaxintegra.presentation.model.CollaboratorStatusFilter
import com.example.adaxintegra.presentation.views.screens.admin.ExternalUsersUiState
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

// G-16: the admin consults the list of external accounts
@HiltViewModel
class ExternalUsersViewModel @Inject constructor(
    private val getExternalUsersUseCase: GetExternalUsersUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ExternalUsersUiState())
    val uiState: StateFlow<ExternalUsersUiState> = _uiState.asStateFlow()

    // Request in progress, so a new one cancels the previous one
    private var loadJob: Job? = null

    // Loads the current page with the search and the filter selected
    fun loadExternalUsers() {
        loadJob?.cancel()

        val request = _uiState.value

        // The old page is removed while the new one is loading
        _uiState.update {
            it.copy(users = emptyList(), isLoading = true, error = null)
        }

        loadJob = viewModelScope.launch {
            getExternalUsersUseCase(
                page = request.page,
                search = request.searchQuery.trim(),
                status = statusValue(request.statusFilter),
            ).collect { result ->
                _uiState.update { current ->
                    when (result) {
                        is Result.Loading -> current.copy(isLoading = true, error = null)

                        // Keeps the order and the pages calculated by the backend
                        is Result.Success -> current.copy(
                            users = result.data.users,
                            total = result.data.total,
                            page = result.data.page,
                            limit = result.data.limit,
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

    // A new search starts again from the first page
    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query, page = 1) }
        loadExternalUsers()
    }

    fun applyStatusFilter(filter: CollaboratorStatusFilter) {
        _uiState.update { it.copy(statusFilter = filter, page = 1) }
        loadExternalUsers()
    }

    fun clearStatusFilter() {
        applyStatusFilter(CollaboratorStatusFilter.ALL)
    }

    fun nextPage() {
        val state = _uiState.value
        if (!state.isLoading && state.canGoNext) {
            _uiState.update { it.copy(page = it.page + 1) }
            loadExternalUsers()
        }
    }

    fun previousPage() {
        val state = _uiState.value
        if (!state.isLoading && state.canGoBack) {
            _uiState.update { it.copy(page = it.page - 1) }
            loadExternalUsers()
        }
    }

    // The backend shows only active accounts when no status is sent, so "all" is always sent
    private fun statusValue(filter: CollaboratorStatusFilter): String = when (filter) {
        CollaboratorStatusFilter.ALL -> "all"
        CollaboratorStatusFilter.ACTIVE -> "active"
        CollaboratorStatusFilter.INACTIVE -> "inactive"
    }

    private fun errorMessage(e: Throwable): String = when {
        e is HttpException && e.code() == 400 -> "La búsqueda no es válida. Revisa el texto e inténtalo de nuevo."
        e is HttpException && e.code() == 401 -> "Tu sesión expiró. Vuelve a iniciar sesión."
        e is HttpException && e.code() == 403 -> "No tienes permiso para ver las cuentas externas."
        e is IOException -> "Sin conexión a internet. Inténtalo de nuevo."
        else -> "Ocurrió un error al cargar las cuentas externas."
    }
}
