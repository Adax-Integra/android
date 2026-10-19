package com.example.adaxintegra.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.model.Event
import com.example.adaxintegra.domain.usecases.CreateEventUseCase
import com.example.adaxintegra.domain.usecases.UploadEventImageUseCase
import com.example.adaxintegra.presentation.util.DocumentImageEncoder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class NewEventViewModel @Inject constructor(
    private val createEventUseCase: CreateEventUseCase,
    private val uploadEventImageUseCase: UploadEventImageUseCase,
    private val documentImageEncoder: DocumentImageEncoder,
) : ViewModel() {

    private val _uiState = MutableStateFlow(NewEventUiState())
    val uiState: StateFlow<NewEventUiState> = _uiState.asStateFlow()

    fun onTitleChange(title: String) {
        _uiState.update {
            it.copy(
                title = title.take(150),
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
                locationError = null,
                linkError = null,
                error = null,
            )
        }
    }

    fun onLocationChange(location: String) {
        _uiState.update {
            it.copy(
                location = location.take(200),
                locationError = null,
                error = null,
            )
        }
    }

    fun onLinkChange(link: String) {
        _uiState.update {
            it.copy(
                link = link.take(500),
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
                description = description.take(2000),
                descriptionError = null,
                error = null,
            )
        }
    }

    fun onNotesChange(notes: String) {
        _uiState.update {
            it.copy(
                notes = notes.take(1000),
                notesError = null,
                error = null,
            )
        }
    }

    fun onToastShown() {
        _uiState.update { it.copy(toastMessage = null) }
    }

    fun onImagePicked(uri: Uri) {
        viewModelScope.launch {
            _uiState.update { it.copy(isImageUploading = true, imageUploadError = null, error = null) }
            try {
                val encodedFile = documentImageEncoder.encode(uri, "imagen-evento.jpg")
                _uiState.update {
                    it.copy(
                        selectedImageFile = encodedFile,
                        selectedImageName = "imagen-evento.jpg",
                        isImageUploading = false,
                        imageUploadError = null,
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isImageUploading = false,
                        imageUploadError = e.message ?: "Error al procesar la imagen",
                        error = e.message ?: "Error al procesar la imagen",
                    )
                }
            }
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
        var locationError: String? = null
        var linkError: String? = null
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
        } else {
            if (state.modality.equals(
                    "Presencial",
                    ignoreCase = true
                ) && state.location.isBlank()
            ) {
                locationError = "Ingresa el lugar para modalidad presencial"
                hasError = true
            }
            if (state.modality.equals("Virtual", ignoreCase = true) && state.link.isBlank()) {
                linkError = "Ingresa el enlace para modalidad virtual"
                hasError = true
            }
        }

        if (state.link.isNotBlank()) {
            val cleanLink = state.link.trim()
            if (!cleanLink.startsWith("http://", ignoreCase = true) &&
                !cleanLink.startsWith("https://", ignoreCase = true)
            ) {
                linkError = "El enlace debe comenzar con http:// o https://"
                hasError = true
            }
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
                    locationError = locationError,
                    linkError = linkError,
                    descriptionError = descriptionError,
                )
            }
            return
        }

        val startInstant = toInstant(startDate!!, startTime!!)
        val endInstant = toInstant(endDate!!, endTime!!)

        val formattedPhone = if (state.phone.isNotBlank()) {
            val cleanPhone = state.phone.trim().filter { it.isDigit() }
            "${state.countryCode}$cleanPhone"
        } else {
            null
        }

        val event = Event(
            id = "",
            title = state.title.trim(),
            description = state.description.trim(),
            notes = state.notes.trim().ifBlank { null },
            startsAt = startInstant,
            endsAt = endInstant,
            modality = state.modality.trim(),
            location = state.location.trim().ifBlank { null },
            link = state.link.trim().ifBlank { null },
            phone = formattedPhone,
            imageUrl = null,
        )

        viewModelScope.launch {
            createEventUseCase(event).collect { result ->
                when (result) {
                    is Result.Loading -> _uiState.update { it.copy(isLoading = true, error = null) }
                    is Result.Success -> {
                        val createdEvent = result.data
                        val imageFile = _uiState.value.selectedImageFile

                        if (imageFile != null && createdEvent.id.isNotBlank()) {
                            uploadEventImageUseCase(
                                createdEvent.id,
                                imageFile
                            ).collect { imgResult ->
                                _uiState.update { current ->
                                    when (imgResult) {
                                        is Result.Loading -> current.copy(
                                            isImageUploading = true,
                                            isLoading = true
                                        )

                                        is Result.Success -> current.copy(
                                            isLoading = false,
                                            isImageUploading = false,
                                            isSuccess = true,
                                            error = null,
                                        )

                                        is Result.Error -> current.copy(
                                            isLoading = false,
                                            isImageUploading = false,
                                            error = parseErrorMessage(imgResult.exception).generalError,
                                        )
                                    }
                                }
                            }
                        } else {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    isSuccess = true,
                                    error = null,
                                )
                            }
                        }
                    }

                    is Result.Error -> {
                        val parsed = parseErrorMessage(result.exception)
                        _uiState.update { current ->
                            current.copy(
                                isLoading = false,
                                error = parsed.generalError,
                                linkError = parsed.fieldErrors["link"] ?: current.linkError,
                                locationError = parsed.fieldErrors["location"]
                                    ?: current.locationError,
                            )
                        }
                    }
                }
            }
        }
    }

    fun dismissSuccess() {
        _uiState.update { it.copy(isSuccess = false) }
    }

    private fun toInstant(calendar: Calendar, time: LocalTime): Instant {
        val localDate = LocalDate.of(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH) + 1,
            calendar.get(Calendar.DAY_OF_MONTH),
        )
        val localDateTime = LocalDateTime.of(localDate, time)
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant()
    }

    private data class ParsedError(
        val generalError: String?,
        val fieldErrors: Map<String, String> = emptyMap(),
    )

    private fun parseErrorMessage(e: Throwable): ParsedError {
        if (e is HttpException) {
            val errorBody = e.response()?.errorBody()?.string() ?: ""
            if (e.code() == 400 || errorBody.contains("http:// or https://")) {
                if (errorBody.contains("link must start with") || errorBody.contains("http:// or https://")) {
                    return ParsedError(
                        generalError = null,
                        fieldErrors = mapOf("link" to "El enlace debe comenzar con http:// o https://"),
                    )
                }
                if (errorBody.contains("location is required")) {
                    return ParsedError(
                        generalError = null,
                        fieldErrors = mapOf("location" to "Ingresa el lugar para modalidad presencial"),
                    )
                }
                return ParsedError(generalError = "Solicitud inválida. Revisa los datos ingresados.")
            }
            val msg = when (e.code()) {
                401 -> "Tu sesión expiró. Vuelve a iniciar sesión."
                403 -> "No tienes permiso para registrar este evento."
                404 -> "El evento no existe o se encuentra deshabilitado."
                429 -> "Has superado el límite de 20 eventos por hora desde esta IP."
                500 -> "Error interno en el servidor. Inténtalo de nuevo más tarde."
                else -> e.message() ?: "Ocurrió un error al registrar el evento"
            }
            return ParsedError(generalError = msg)
        }
        val msg = when (e) {
            is IOException -> "Sin conexión a internet. Inténtalo de nuevo."
            else -> e.message ?: "Ocurrió un error al registrar el evento"
        }
        return ParsedError(generalError = msg)
    }
}
