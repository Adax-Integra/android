package com.example.adaxintegra.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adaxintegra.domain.model.EventScope
import com.example.adaxintegra.domain.usecases.GetEventsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import retrofit2.HttpException
import javax.inject.Inject
import com.example.adaxintegra.domain.common.Result

// E-01: loads the active events for the home carousel (MONTH) or the events screen (ALL)
@HiltViewModel
class EventsViewModel @Inject constructor(
    private val getEventsUseCase: GetEventsUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(EventsUiState())
    val uiState = _uiState.asStateFlow()

    // Remembered so refresh and retry ask for the same events
    private var scope = EventScope.ALL
    private var eventsJob: Job? = null

    // Called by the screen on every entry, so an event created or edited elsewhere shows up
    fun loadEvents(scope: EventScope) {
        this.scope = scope
        loadEvents()
    }

    fun loadEvents(){
        // Cancel the previous request so older results cannot replace new ones
        eventsJob?.cancel()
        eventsJob = viewModelScope.launch {
            getEventsUseCase(scope).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        // The events already shown stay visible while they reload,
                        // so the spinner is only shown when there is nothing yet
                        _uiState.update {
                            it.copy(
                                isLoading = !it.isRefreshing && it.events.isEmpty(),
                                error = null,
                            )
                        }
                    }

                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                events = result.data,
                                isLoading = false,
                                isRefreshing = false,
                                error = null,
                            )
                        }
                    }

                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isRefreshing = false,
                                error = errorMessage(result.exception),
                            )
                        }
                    }
                }
            }
        }
    }
    // Pull to refresh: reloads the events without hiding the current ones
    fun refresh() {
        val state = _uiState.value
        if (state.isLoading || state.isRefreshing) {
            return
        }
        _uiState.update { it.copy(isRefreshing = true) }
        loadEvents()
    }

    private fun errorMessage(e: Throwable): String = when {
        e is HttpException && e.code() == 401 -> "Tu sesión expiró. Vuelve a iniciar sesión."
        e is IOException -> "Sin conexión a internet. Inténtalo de nuevo."
        else -> "No se pudieron cargar los eventos."
    }
}
