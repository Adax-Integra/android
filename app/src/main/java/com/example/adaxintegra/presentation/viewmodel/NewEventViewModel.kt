package com.example.adaxintegra.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adaxintegra.domain.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class NewEventViewModel @Inject constructor(
    private val eventRepository: EventRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(NewEventUiState())
    val uiState: StateFlow<NewEventUiState> = _uiState.asStateFlow()

    fun onTitleChange(title: String) {
        _uiState.update {
            it.copy(
                title = title,
                titleError = null,
                error = null,
            )
        }
    }

    fun setStartDate(calendar: Calendar) {
        _uiState.update {
            it.copy(
                startDate = calendar,
                startDateError = null,
                endDateError = null,
                error = null,
            )
        }
    }

    fun setEndDate(calendar: Calendar) {
        _uiState.update {
            it.copy(
                endDate = calendar,
                endDateError = null,
                error = null,
            )
        }
    }

    fun setStartTime(time: LocalTime) {
        _uiState.update {
            it.copy(
                startTime = time,
                startTimeError = null,
                endTimeError = null,
                error = null,
            )
        }
    }

    fun setEndTime(time: LocalTime) {
        _uiState.update {
            it.copy(
                endTime = time,
                endTimeError = null,
                error = null,
            )
        }
    }

    fun onModalityChange(modality: String) {
        _uiState.update {
            it.copy(
                modality = modality,
                modalityError = null,
                error = null,
            )
        }
    }

    fun onLocationChange(location: String) {
        _uiState.update {
            it.copy(
                location = location,
                locationError = null,
                error = null,
            )
        }
    }

    fun onLinkChange(link: String) {
        _uiState.update {
            it.copy(
                link = link,
                linkError = null,
                error = null,
            )
        }
    }

    fun onPhoneChange(phone: String) {
        _uiState.update {
            it.copy(
                phone = phone,
                phoneError = null,
                error = null,
            )
        }
    }

    fun onCountryCodeChange(countryCode: String) {
        _uiState.update {
            it.copy(
                countryCode = countryCode,
                phoneError = null,
                error = null,
            )
        }
    }

    fun onDescriptionChange(description: String) {
        _uiState.update {
            it.copy(
                description = description,
                descriptionError = null,
                error = null,
            )
        }
    }

    fun onNotesChange(notes: String) {
        _uiState.update {
            it.copy(
                notes = notes,
                notesError = null,
                error = null,
            )
        }
    }

    fun createEvent() {
        val state = _uiState.value

        var hasError = false
        var titleError: String? = null
        var startDateError: String? = null
        var endDateError: String? = null
        var startTimeError: String? = null
        var endTimeError: String? = null
        var modalityError: String? = null
        var descriptionError: String? = null

        if (state.title.isBlank()) {
            titleError = "Ingresa el título del evento"
            hasError = true
        }

        if (state.startDate == null) {
            startDateError = "Selecciona la fecha de inicio"
            hasError = true
        }

        if (state.endDate == null) {
            endDateError = "Selecciona la fecha de fin"
            hasError = true
        }

        if (state.startTime == null) {
            startTimeError = "Selecciona la hora de inicio"
            hasError = true
        }

        if (state.endTime == null) {
            endTimeError = "Selecciona la hora de fin"
            hasError = true
        }

        if (state.modality.isBlank()) {
            modalityError = "Selecciona una modalidad"
            hasError = true
        }

        if (state.description.isBlank()) {
            descriptionError = "Ingresa la descripción del evento"
            hasError = true
        }

        // Check date and time order (Event cannot end before it starts)
        val startDate = state.startDate
        val endDate = state.endDate
        val startTime = state.startTime
        val endTime = state.endTime

        if (startDate != null && endDate != null) {
            val startZero = (startDate.clone() as Calendar).apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            val endZero = (endDate.clone() as Calendar).apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            if (startZero.after(endZero)) {
                endDateError = "La fecha de fin debe ser posterior a la fecha de inicio"
                hasError = true
            } else if (startZero == endZero && startTime != null && endTime != null) {
                if (!endTime.isAfter(startTime)) {
                    endTimeError = "La hora de fin debe ser posterior a la hora de inicio"
                    hasError = true
                }
            }
        }

        if (hasError) {
            _uiState.update {
                it.copy(
                    titleError = titleError,
                    startDateError = startDateError,
                    endDateError = endDateError,
                    startTimeError = startTimeError,
                    endTimeError = endTimeError,
                    modalityError = modalityError,
                    descriptionError = descriptionError,
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isSuccess = true,
                        error = null,
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Error al crear el evento",
                    )
                }
            }
        }
    }

    fun dismissSuccess() {
        _uiState.update { it.copy(isSuccess = false) }
    }
}
