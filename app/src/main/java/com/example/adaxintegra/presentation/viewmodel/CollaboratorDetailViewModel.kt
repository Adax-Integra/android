package com.example.adaxintegra.presentation.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.usecases.GetCollaboratorDetailUseCase
import com.example.adaxintegra.presentation.views.screens.admin.CollaboratorDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

// G-10: loads the detail of the collaborator selected in the G-06 list
@HiltViewModel
class CollaboratorDetailViewModel @Inject constructor(
    private val getCollaboratorDetailUseCase: GetCollaboratorDetailUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CollaboratorDetailUiState())
    val uiState = _uiState.asStateFlow()

    private var loadJob: Job? = null

    // Saved so "Reintentar" can repeat the same request
    private var currentUserId: String? = null

    fun loadCollaborator(userId: String) {
        currentUserId = userId

        // Cancel the previous request so older results cannot replace new ones
        loadJob?.cancel()

        loadJob = viewModelScope.launch {
            getCollaboratorDetailUseCase(userId).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.update {
                            it.copy(isLoading = true, error = null)
                        }
                    }

                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                collaborator = result.data,
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

    fun retry() {
        val userId = currentUserId ?: return
        loadCollaborator(userId)
    }

    private fun errorMessage(e: Throwable): String = when {
        e is HttpException && e.code() == 401 -> "Tu sesión expiró. Vuelve a iniciar sesión."
        e is HttpException && e.code() == 403 -> "No tienes permiso para ver esta información."
        e is HttpException && e.code() == 404 -> "No se encontró la colaboradora."
        e is IOException -> "Sin conexión a internet. Inténtalo de nuevo."
        else -> "Ocurrió un error al cargar la colaboradora."
    }
}
