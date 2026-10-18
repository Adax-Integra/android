package com.example.adaxintegra.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.model.EventScope
import com.example.adaxintegra.domain.usecases.GetEventsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

// E-01: loads the event opened from the carousel or the events screen.
// The backend has no endpoint for a single event, so it is searched in the active ones
@HiltViewModel
class EventDetailViewModel @Inject constructor(
    private val getEventsUseCase: GetEventsUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(EventDetailUiState())
    val uiState = _uiState.asStateFlow()

    private var loadJob: Job? = null

    // Saved so "Reintentar" can repeat the same request
    private var currentEventId: String? = null

    fun loadEvent(eventId: String) {
        currentEventId = eventId

        // Cancel the previous request so older results cannot replace new ones
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            getEventsUseCase(EventScope.ALL).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.update { it.copy(isLoading = true, error = null, isUnavailable = false) }
                    }

                    is Result.Success -> {
                        val event = result.data.find { it.id == eventId }
                        _uiState.update {
                            it.copy(
                                event = event,
                                isLoading = false,
                                // The event was disabled or already ended while it was open
                                error = if (event == null) "Este evento ya no está disponible." else null,
                                isUnavailable = event == null,
                            )
                        }
                    }

                    is Result.Error -> {
                        _uiState.update {
                            it.copy(isLoading = false, error = errorMessage(result.exception))
                        }
                    }
                }
            }
        }
    }

    fun retry() {
        currentEventId?.let { loadEvent(it) }
    }

    private fun errorMessage(e: Throwable): String = when {
        e is HttpException && e.code() == 401 -> "Tu sesión expiró. Vuelve a iniciar sesión."
        e is IOException -> "Sin conexión a internet. Inténtalo de nuevo."
        else -> "No se pudo cargar el evento."
    }
}
