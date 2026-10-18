package com.example.adaxintegra.presentation.views.screens.events

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.adaxintegra.presentation.viewmodel.NewEventUiState
import com.example.adaxintegra.presentation.viewmodel.NewEventViewModel
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppButton
import com.example.adaxintegra.presentation.views.designsystem.molecules.AppDatePickerDialog
import com.example.adaxintegra.presentation.views.designsystem.molecules.AppTimePickerDialog
import com.example.adaxintegra.presentation.views.designsystem.molecules.DateField
import com.example.adaxintegra.presentation.views.designsystem.molecules.LabeledDropdownField
import com.example.adaxintegra.presentation.views.designsystem.molecules.LabeledTextArea
import com.example.adaxintegra.presentation.views.designsystem.molecules.LabeledTextField
import com.example.adaxintegra.presentation.views.designsystem.molecules.PhoneField
import com.example.adaxintegra.presentation.views.designsystem.molecules.TimeField
import com.example.adaxintegra.presentation.views.designsystem.organisms.SuccessDialog
import com.example.adaxintegra.presentation.views.designsystem.templates.ScreenTemplate
import com.example.adaxintegra.ui.theme.AdaxIntegraTheme
import java.time.LocalTime
import java.util.Calendar
import java.util.TimeZone

@Suppress("ktlint:standard:function-naming")
@Composable
fun NewEventScreen(
    onBackClick: () -> Unit,
    onEventCreated: () -> Unit = {},
    viewModel: NewEventViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CreateEvent(
        uiState = uiState,
        onBackClick = onBackClick,
        onTitleChange = viewModel::onTitleChange,
        onStartDateSelected = viewModel::setStartDate,
        onEndDateSelected = viewModel::setEndDate,
        onStartTimeSelected = viewModel::setStartTime,
        onEndTimeSelected = viewModel::setEndTime,
        onModalityChange = viewModel::onModalityChange,
        onLocationChange = viewModel::onLocationChange,
        onLinkChange = viewModel::onLinkChange,
        onPhoneChange = viewModel::onPhoneChange,
        onCountryCodeChange = viewModel::onCountryCodeChange,
        onDescriptionChange = viewModel::onDescriptionChange,
        onNotesChange = viewModel::onNotesChange,
        onCreateEventClick = viewModel::createEvent,
    )

    if (uiState.isSuccess) {
        SuccessDialog(
            title = "Evento creado",
            message = "El evento fue registrado con éxito.",
            onConfirm = {
                viewModel.dismissSuccess()
                onEventCreated()
            },
        )
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun CreateEvent(
    modifier: Modifier = Modifier,
    uiState: NewEventUiState = NewEventUiState(),
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
    onCreateEventClick: () -> Unit = {},
) {
    // To know if it needs to show the start date
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    // To know if it needs to show the time pickers
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    ScreenTemplate(
        title = "Nuevo evento",
        error = uiState.error,
        isLoading = uiState.isLoading,
        onBack = onBackClick,
        modifier = modifier,
    ) {
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
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Next,
                ),
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
                    value = uiState.startDate,
                    errorMessage = uiState.startDateError,
                    placeholder = "Fecha",
                    onClick = { showStartDatePicker = true },
                    modifier = Modifier.weight(1f),
                )
                DateField(
                    label = "Fecha fin",
                    value = uiState.endDate,
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
                    value = uiState.startTime,
                    errorMessage = uiState.startTimeError,
                    placeholder = "Hora",
                    onClick = { showStartTimePicker = true },
                    modifier = Modifier.weight(1f),
                )
                TimeField(
                    label = "Hora fin",
                    value = uiState.endTime,
                    errorMessage = uiState.endTimeError,
                    placeholder = "Hora",
                    onClick = { showEndTimePicker = true },
                    modifier = Modifier.weight(1f),
                )
            }

            // Dropdown "Modalidad"
            LabeledDropdownField(
                label = "Modalidad",
                selectedOption = uiState.modality.ifEmpty { null },
                options = listOf("Presencial", "Virtual", "Mixto"),
                onOptionSelected = onModalityChange,
                placeholder = "Presencial",
                error = uiState.modalityError,
            )

            // Field "Lugar"
            LabeledTextField(
                label = "Lugar",
                value = uiState.location,
                onValueChange = onLocationChange,
                placeholder = "Ej. Oficinas ADAX",
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                errorMessage = uiState.locationError,
            )

            // Field "Link (opcional)"
            LabeledTextField(
                label = "Link (opcional)",
                value = uiState.link,
                onValueChange = onLinkChange,
                placeholder = "https://meet.google.com/...",
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                errorMessage = uiState.linkError,
            )

            // Field "Teléfono (opcional)"
            PhoneField(
                label = "Teléfono (opcional)",
                countryCode = uiState.countryCode,
                phoneValue = uiState.phone,
                onPhoneChange = onPhoneChange,
                onCountryCodeChange = onCountryCodeChange,
                placeholder = "55 1234 5678",
                errorMessage = uiState.phoneError,
            )

            // Field "Descripción"
            LabeledTextArea(
                label = "Descripción",
                value = uiState.description,
                onValueChange = onDescriptionChange,
                placeholder = "Describe el evento...",
                maxLength = 500,
                minLines = 4,
                error = uiState.descriptionError,
            )

            // Field "Notas (opcional)"
            LabeledTextArea(
                label = "Notas (opcional)",
                value = uiState.notes,
                onValueChange = onNotesChange,
                placeholder = "Escribe notas adicionales...",
                maxLength = 500,
                minLines = 4,
                error = uiState.notesError,
            )

            Spacer(modifier = Modifier.height(8.dp))

            AppButton(
                text = "Crear evento",
                onClick = onCreateEventClick,
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        // Dialog for Start Date
        if (showStartDatePicker) {
            AppDatePickerDialog(
                initialSelectedDateMillis = uiState.startDate?.toUtcPickerMillis(),
                onDateSelected = { millis ->
                    if (millis != null) {
                        onStartDateSelected(utcMillisToLocalCalendar(millis))
                    }
                },
                onDismiss = { showStartDatePicker = false },
                title = "Selecciona la fecha de inicio",
            )
        }

        // Dialog for End Date
        if (showEndDatePicker) {
            AppDatePickerDialog(
                initialSelectedDateMillis = uiState.endDate?.toUtcPickerMillis(),
                onDateSelected = { millis ->
                    if (millis != null) {
                        onEndDateSelected(utcMillisToLocalCalendar(millis))
                    }
                },
                onDismiss = { showEndDatePicker = false },
                title = "Selecciona la fecha de fin",
            )
        }

        // Dialog for Start Time
        if (showStartTimePicker) {
            AppTimePickerDialog(
                initialHour = uiState.startTime?.hour ?: Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                initialMinute = uiState.startTime?.minute ?: Calendar.getInstance().get(Calendar.MINUTE),
                onTimeSelected = { hour, minute ->
                    val time = LocalTime.of(hour, minute)
                    onStartTimeSelected(time)
                },
                onDismiss = { showStartTimePicker = false },
                title = "Selecciona la hora de inicio",
            )
        }

        // Dialog for End Time
        if (showEndTimePicker) {
            AppTimePickerDialog(
                initialHour = uiState.endTime?.hour ?: Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                initialMinute = uiState.endTime?.minute ?: Calendar.getInstance().get(Calendar.MINUTE),
                onTimeSelected = { hour, minute ->
                    val time = LocalTime.of(hour, minute)
                    onEndTimeSelected(time)
                },
                onDismiss = { showEndTimePicker = false },
                title = "Selecciona la hora de fin",
            )
        }
    }
}

private fun utcMillisToLocalCalendar(utcMillis: Long): Calendar {
    val utcCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
        timeInMillis = utcMillis
    }
    return Calendar.getInstance().apply {
        set(
            utcCalendar.get(Calendar.YEAR),
            utcCalendar.get(Calendar.MONTH),
            utcCalendar.get(Calendar.DAY_OF_MONTH),
            0, 0, 0
        )
        set(Calendar.MILLISECOND, 0)
    }
}

private fun Calendar.toUtcPickerMillis(): Long {
    val utcCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
        clear()
        set(
            this@toUtcPickerMillis.get(Calendar.YEAR),
            this@toUtcPickerMillis.get(Calendar.MONTH),
            this@toUtcPickerMillis.get(Calendar.DAY_OF_MONTH),
            0, 0, 0
        )
    }
    return utcCalendar.timeInMillis
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
