package com.example.adaxintegra.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.model.NewEvent
import com.example.adaxintegra.domain.repository.AuthRepository
import com.example.adaxintegra.domain.usecases.CreateEventUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateEventViewModel @Inject constructor(
    private val createEventUseCase: CreateEventUseCase,
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CreateEventUiState())
    val uiState: StateFlow<CreateEventUiState> = _uiState.asStateFlow()

    fun onTitleChange(v: String) = _uiState.update { it.copy(title = v, error = null) }
    fun onStartDateChange(v: String) = _uiState.update { it.copy(startDate = v, error = null) }
    fun onEndDateChange(v: String) = _uiState.update { it.copy(endDate = v, error = null) }
    fun onStartHourChange(v: String) = _uiState.update { it.copy(startHour = v, error = null) }
    fun onStartMinuteChange(v: String) = _uiState.update { it.copy(startMinute = v, error = null) }
    fun onStartAmPmChange(v: String) = _uiState.update { it.copy(startAmPm = v, error = null) }
    fun onEndHourChange(v: String) = _uiState.update { it.copy(endHour = v, error = null) }
    fun onEndMinuteChange(v: String) = _uiState.update { it.copy(endMinute = v, error = null) }
    fun onEndAmPmChange(v: String) = _uiState.update { it.copy(endAmPm = v, error = null) }
    fun onModalityChange(v: String) = _uiState.update { it.copy(modality = v, error = null) }
    fun onLocationChange(v: String) = _uiState.update { it.copy(location = v, error = null) }
    fun onLinkChange(v: String) = _uiState.update { it.copy(link = v, error = null) }
    fun onPhoneChange(v: String) = _uiState.update { it.copy(phone = v, error = null) }
    fun onDescriptionChange(v: String) = _uiState.update { it.copy(description = v, error = null) }
    fun onNotesChange(v: String) = _uiState.update { it.copy(notes = v, error = null) }
    fun onImageSelected(uri: Uri?) = _uiState.update { it.copy(imageUri = uri, error = null) }

    fun dismissSuccess() = _uiState.update { it.copy(successMessage = null) }

    fun saveEvent(onSuccess: () -> Unit) {
        val state = _uiState.value
        if (state.title.isBlank() || state.description.isBlank()) {
            _uiState.update { it.copy(error = "El título y la descripción son obligatorios.") }
            return
        }

        val token = authRepository.session.value?.token
        if (token.isNullOrBlank()) {
            _uiState.update { it.copy(error = "No hay una sesión activa.") }
            return
        }

        val startTime = if (state.startHour.isNotBlank() && state.startMinute.isNotBlank()) {
            "${state.startHour}:${state.startMinute} ${state.startAmPm}"
        } else {
            "10:00 a.m."
        }

        val endTime = if (state.endHour.isNotBlank() && state.endMinute.isNotBlank()) {
            "${state.endHour}:${state.endMinute} ${state.endAmPm}"
        } else {
            "11:30 a.m."
        }

        val newEvent = NewEvent(
            title = state.title.trim(),
            startDate = state.startDate.ifBlank { "01/01/2026" },
            endDate = state.endDate.ifBlank { "01/01/2026" },
            startTime = startTime,
            endTime = endTime,
            modality = state.modality,
            location = state.location.ifBlank { "Oficinas ADAX" },
            link = state.link.takeIf { it.isNotBlank() },
            phone = state.phone.takeIf { it.isNotBlank() }?.let { "+52 $it" },
            description = state.description.trim(),
            notes = state.notes.takeIf { it.isNotBlank() },
        )

        viewModelScope.launch {
            createEventUseCase(token, newEvent, state.imageUri).collect { result ->
                when (result) {
                    is Result.Loading -> _uiState.update { it.copy(isSaving = true, error = null) }
                    is Result.Success -> {
                        _uiState.update {
                            it.copy(isSaving = false, successMessage = "El evento ha sido registrado exitosamente.")
                        }
                    }
                    is Result.Error -> {
                        _uiState.update {
                            it.copy(isSaving = false, error = "Ocurrió un error al registrar el evento o subir la imagen. Inténtalo de nuevo.")
                        }
                    }
                }
            }
        }
    }
}
