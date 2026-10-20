package com.example.adaxintegra.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.usecases.GetCollaboratorDetailUseCase
import com.example.adaxintegra.domain.usecases.UpdateCollaboratorStatusUseCase
import com.example.adaxintegra.domain.usecases.UpdateCollaboratorProfileUseCase
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
// G-04: activates or deactivates the account from the detail
@HiltViewModel
class CollaboratorDetailViewModel @Inject constructor(
    private val getCollaboratorDetailUseCase: GetCollaboratorDetailUseCase,
    private val updateCollaboratorStatusUseCase: UpdateCollaboratorStatusUseCase,
    private val updateCollaboratorProfileUseCase: UpdateCollaboratorProfileUseCase,
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

    // G-04: opens the confirmation before changing the status
    fun openStatusDialog() {
        _uiState.update { it.copy(showStatusDialog = true, statusError = null) }
    }

    // G-04: the dialog cannot be closed while the request is running
    fun dismissStatusDialog() {
        if (_uiState.value.isUpdatingStatus) return
        _uiState.update { it.copy(showStatusDialog = false, statusError = null) }
    }

    // G-04: an active account is deactivated and an inactive one is activated
    fun updateStatus() {
        val collaborator = _uiState.value.collaborator ?: return
        if (_uiState.value.isUpdatingStatus) return

        val newStatus = !collaborator.isActive

        viewModelScope.launch {
            updateCollaboratorStatusUseCase(collaborator.userId, newStatus).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.update {
                            it.copy(isUpdatingStatus = true, statusError = null)
                        }
                    }

                    is Result.Success -> {
                        // The status saved by the backend is shown without loading the detail again
                        _uiState.update { state ->
                            state.copy(
                                collaborator = state.collaborator?.copy(isActive = result.data),
                                isUpdatingStatus = false,
                                showStatusDialog = false,
                            )
                        }
                    }

                    is Result.Error -> {
                        val exception = result.exception

                        // 409: the account already had that status, so the detail is loaded again
                        if (exception is HttpException && exception.code() == 409) {
                            _uiState.update {
                                it.copy(isUpdatingStatus = false, showStatusDialog = false)
                            }
                            retry()
                        } else {
                            _uiState.update {
                                it.copy(
                                    isUpdatingStatus = false,
                                    statusError = statusErrorMessage(exception),
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // G-18: preserve the form on error; close only after backend success
    fun openEditDialog() {
        if (_uiState.value.collaborator == null) return
        _uiState.update {
            it.copy(showEditDialog = true, profileError = null)
        }
    }

    fun dismissEditDialog() {
        if (_uiState.value.isUpdatingProfile) return
        _uiState.update {
            it.copy(showEditDialog = false, profileError = null)
        }
    }

    fun clearProfileUpdated() {
        _uiState.update { it.copy(profileUpdated = false) }
    }

    fun saveProfile(name: String?, lastName: String?, email: String?) {
        val collaborator = _uiState.value.collaborator ?: return
        if (_uiState.value.isUpdatingProfile) return
        if (name == null && lastName == null && email == null) return

        viewModelScope.launch {
            updateCollaboratorProfileUseCase(
                userId = collaborator.userId,
                name = name,
                lastName = lastName,
                email = email,
            ).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.update {
                            it.copy(isUpdatingProfile = true, profileError = null)
                        }
                    }
                    is Result.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                collaborator = state.collaborator?.copy(
                                    name = name ?: collaborator.name,
                                    lastName = lastName ?: collaborator.lastName,
                                    email = email ?: collaborator.email,
                                ),
                                showEditDialog = false,
                                isUpdatingProfile = false,
                                profileError = null,
                                profileUpdated = true,
                            )
                        }
                        // Refresh G-10 with the saved backend values
                        loadCollaborator(collaborator.userId)
                    }
                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                isUpdatingProfile = false,
                                profileError = profileErrorMessage(result.exception),
                            )
                        }
                    }
                }
            }
        }
    }

    private fun profileErrorMessage(e: Throwable): String = when {
        e is HttpException && e.code() == 400 ->
            "Revisa los datos ingresados e inténtalo de nuevo."
        e is HttpException && e.code() == 401 ->
            "Tu sesión expiró. Vuelve a iniciar sesión."
        e is HttpException && e.code() == 403 ->
            "Solo una administradora puede editar esta cuenta."
        e is HttpException && e.code() == 404 ->
            "No se encontró la colaboradora."
        e is HttpException && e.code() == 409 ->
            "El correo ya está registrado en otra cuenta."
        e is IOException ->
            "Sin conexión a internet. Tus datos siguen en el formulario."
        else -> "No se pudieron guardar los cambios. Inténtalo de nuevo."
    }

    private fun errorMessage(e: Throwable): String = when {
        e is HttpException && e.code() == 401 -> "Tu sesión expiró. Vuelve a iniciar sesión."
        e is HttpException && e.code() == 403 -> "No tienes permiso para ver esta información."
        e is HttpException && e.code() == 404 -> "No se encontró la colaboradora."
        e is IOException -> "Sin conexión a internet. Inténtalo de nuevo."
        else -> "Ocurrió un error al cargar la colaboradora."
    }

    private fun statusErrorMessage(e: Throwable): String = when {
        e is HttpException && e.code() == 401 -> "Tu sesión expiró. Vuelve a iniciar sesión."
        e is HttpException && e.code() == 403 -> "No tienes permiso para cambiar el estatus."
        e is HttpException && e.code() == 404 -> "No se encontró la colaboradora."
        e is IOException -> "Sin conexión a internet. Inténtalo de nuevo."
        else -> "No se pudo cambiar el estatus. Inténtalo de nuevo."
    }
}
