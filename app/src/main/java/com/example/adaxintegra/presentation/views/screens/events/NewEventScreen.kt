package com.example.adaxintegra.presentation.views.screens.events

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.presentation.viewmodel.NewEventUiState
import com.example.adaxintegra.presentation.views.designsystem.molecules.AppDatePickerDialog
import com.example.adaxintegra.presentation.views.designsystem.molecules.AppTimePickerDialog
import com.example.adaxintegra.presentation.views.designsystem.molecules.DateField
import com.example.adaxintegra.presentation.views.designsystem.molecules.LabeledDropdownField
import com.example.adaxintegra.presentation.views.designsystem.molecules.LabeledTextArea
import com.example.adaxintegra.presentation.views.designsystem.molecules.LabeledTextField
import com.example.adaxintegra.presentation.views.designsystem.molecules.PhoneField
import com.example.adaxintegra.presentation.views.designsystem.molecules.TimeField
import com.example.adaxintegra.presentation.views.designsystem.templates.ScreenTemplate
import com.example.adaxintegra.ui.theme.AdaxIntegraTheme
import java.time.LocalTime
import java.time.ZoneId
import java.util.Calendar

@Suppress("ktlint:standard:function-naming")
@Composable
fun CreateEvent(
    modifier: Modifier = Modifier,
    uiState: NewEventUiState = NewEventUiState(),
    // Add the functionality for adding a file, photograph
    onBackClick: () -> Unit = {},
    onTitleChange: (String) -> Unit = {},
    onStartDateSelected: (Calendar) -> Unit = {},
    onEndDateSelected: (Calendar) -> Unit = {},
    onStartTimeSelected: (LocalTime) -> Unit = {},
    onEndTimeSelected: (LocalTime) -> Unit = {},
    onModalityChange: (String) -> Unit = {},
    onLocationChange: (String) -> Unit = {},
    onLinkChange: (String) -> Unit = {},
    onPhoneChange: (String) -> Unit = {},
    onCountryCodeChange: (String) -> Unit = {},
    onDescriptionChange: (String) -> Unit = {},
    onNotesChange: (String) -> Unit = {},
) {
    // To know if it needs to show the start date
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    // To know if it needs to show the time pickers
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }

    // Local state variables for dates, times, modality, location, link, phone, description, notes
    var localStartDate by remember { mutableStateOf<Calendar?>(null) }
    var localEndDate by remember { mutableStateOf<Calendar?>(null) }
    var localStartTime by remember { mutableStateOf<LocalTime?>(null) }
    var localEndTime by remember { mutableStateOf<LocalTime?>(null) }

    var localModality by remember { mutableStateOf("") }
    var localLocation by remember { mutableStateOf("") }
    var localLink by remember { mutableStateOf("") }
    var localPhone by remember { mutableStateOf("") }
    var localCountryCode by remember { mutableStateOf("+52") }
    var localDescription by remember { mutableStateOf("") }
    var localNotes by remember { mutableStateOf("") }

    val startDateCalendar = uiState.startDate?.let { instant ->
        Calendar.getInstance().apply { timeInMillis = instant.toEpochMilli() }
    } ?: localStartDate

    val endDateCalendar = uiState.endDate?.let { instant ->
        Calendar.getInstance().apply { timeInMillis = instant.toEpochMilli() }
    } ?: localEndDate

    val startTimeLocal = uiState.startTime?.atZone(ZoneId.systemDefault())?.toLocalTime() ?: localStartTime

    val endTimeLocal = uiState.endTime?.atZone(ZoneId.systemDefault())?.toLocalTime() ?: localEndTime

    val currentModality = uiState.modality.ifEmpty { localModality }
    val currentLocation = uiState.location.ifEmpty { localLocation }
    val currentLink = uiState.link.ifEmpty { localLink }
    val currentPhone = uiState.phone.ifEmpty { localPhone }
    val currentCountryCode = uiState.countryCode.ifEmpty { localCountryCode }
    val currentDescription = uiState.description.ifEmpty { localDescription }
    val currentNotes = uiState.notes.ifEmpty { localNotes }

    ScreenTemplate(
        title = "Nuevo evento",
        error = uiState.error,
        onBack = onBackClick,
        modifier = modifier,
    ) {
        val focusManager = LocalFocusManager.current

        Column(
            modifier = modifier
                .fillMaxSize()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            LabeledTextField(
                label = "Título del evento",
                value = uiState.title,
                onValueChange = onTitleChange,
                placeholder = "Ej. Reunión de equipo",
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                errorMessage = uiState.titleError,
            )

            // Row for the date selectors (Fecha inicio & Fecha fin)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                DateField(
                    label = "Fecha inicio",
                    value = startDateCalendar,
                    errorMessage = uiState.startDateError,
                    placeholder = "Fecha",
                    onClick = { showStartDatePicker = true },
                    modifier = Modifier.weight(1f),
                )
                DateField(
                    label = "Fecha fin",
                    value = endDateCalendar,
                    errorMessage = uiState.endDateError,
                    placeholder = "Fecha",
                    onClick = { showEndDatePicker = true },
                    modifier = Modifier.weight(1f),
                )
            }

            // Row for the time selectors (Hora inicio & Hora fin)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                TimeField(
                    label = "Hora inicio",
                    value = startTimeLocal,
                    errorMessage = uiState.startTimeError,
                    placeholder = "Hora",
                    onClick = { showStartTimePicker = true },
                    modifier = Modifier.weight(1f),
                )
                TimeField(
                    label = "Hora fin",
                    value = endTimeLocal,
                    errorMessage = uiState.endTimeError,
                    placeholder = "Hora",
                    onClick = { showEndTimePicker = true },
                    modifier = Modifier.weight(1f),
                )
            }

            // Dropdown "Modalidad"
            LabeledDropdownField(
                label = "Modalidad",
                selectedOption = currentModality.ifEmpty { null },
                options = listOf("Presencial", "Virtual", "Mixto"),
                onOptionSelected = {
                    localModality = it
                    onModalityChange(it)
                },
                placeholder = "Presencial",
                error = uiState.modalityError,
            )

            // Field "Lugar"
            LabeledTextField(
                label = "Lugar",
                value = currentLocation,
                onValueChange = {
                    localLocation = it
                    onLocationChange(it)
                },
                placeholder = "Ej. Oficinas ADAX",
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                errorMessage = uiState.locationError,
            )

            // Field "Link (opcional)"
            LabeledTextField(
                label = "Link (opcional)",
                value = currentLink,
                onValueChange = {
                    localLink = it
                    onLinkChange(it)
                },
                placeholder = "https://meet.google.com/...",
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                errorMessage = uiState.linkError,
            )

            // Field "Teléfono (opcional)"
            PhoneField(
                label = "Teléfono (opcional)",
                countryCode = currentCountryCode,
                phoneValue = currentPhone,
                onPhoneChange = {
                    localPhone = it
                    onPhoneChange(it)
                },
                onCountryCodeChange = {
                    localCountryCode = it
                    onCountryCodeChange(it)
                },
                placeholder = "55 1234 5678",
                errorMessage = uiState.phoneError,
            )

            // Field "Descripción"
            LabeledTextArea(
                label = "Descripción",
                value = currentDescription,
                onValueChange = {
                    localDescription = it
                    onDescriptionChange(it)
                },
                placeholder = "Describe el evento...",
                maxLength = 500,
                minLines = 4,
                error = uiState.descriptionError,
            )

            // Field "Notas (opcional)"
            LabeledTextArea(
                label = "Notas (opcional)",
                value = currentNotes,
                onValueChange = {
                    localNotes = it
                    onNotesChange(it)
                },
                placeholder = "Escribe notas adicionales...",
                maxLength = 500,
                minLines = 4,
                error = uiState.notesError,
            )
        }

        // Dialog for Start Date
        if (showStartDatePicker) {
            AppDatePickerDialog(
                initialSelectedDateMillis = startDateCalendar?.timeInMillis,
                onDateSelected = { millis ->
                    if (millis != null) {
                        val calendar = Calendar.getInstance().apply { timeInMillis = millis }
                        localStartDate = calendar
                        onStartDateSelected(calendar)
                    }
                },
                onDismiss = { showStartDatePicker = false },
                title = "Selecciona la fecha de inicio",
            )
        }

        // Dialog for End Date
        if (showEndDatePicker) {
            AppDatePickerDialog(
                initialSelectedDateMillis = endDateCalendar?.timeInMillis,
                onDateSelected = { millis ->
                    if (millis != null) {
                        val calendar = Calendar.getInstance().apply { timeInMillis = millis }
                        localEndDate = calendar
                        onEndDateSelected(calendar)
                    }
                },
                onDismiss = { showEndDatePicker = false },
                title = "Selecciona la fecha de fin",
            )
        }

        // Dialog for Start Time
        if (showStartTimePicker) {
            AppTimePickerDialog(
                initialHour = startTimeLocal?.hour ?: Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                initialMinute = startTimeLocal?.minute ?: Calendar.getInstance().get(Calendar.MINUTE),
                onTimeSelected = { hour, minute ->
                    val time = LocalTime.of(hour, minute)
                    localStartTime = time
                    onStartTimeSelected(time)
                },
                onDismiss = { showStartTimePicker = false },
                title = "Selecciona la hora de inicio",
            )
        }

        // Dialog for End Time
        if (showEndTimePicker) {
            AppTimePickerDialog(
                initialHour = endTimeLocal?.hour ?: Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                initialMinute = endTimeLocal?.minute ?: Calendar.getInstance().get(Calendar.MINUTE),
                onTimeSelected = { hour, minute ->
                    val time = LocalTime.of(hour, minute)
                    localEndTime = time
                    onEndTimeSelected(time)
                },
                onDismiss = { showEndTimePicker = false },
                title = "Selecciona la hora de fin",
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CreateEventPreview() {
    AdaxIntegraTheme {
        CreateEvent(
            uiState = NewEventUiState(),
            onBackClick = {},
            onTitleChange = {},
        )
    }
}
