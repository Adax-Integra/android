package com.example.adaxintegra.presentation.views.screens.home

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.adaxintegra.presentation.util.DateFormatter
import com.example.adaxintegra.presentation.viewmodel.CreateEventViewModel
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppButton
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.ButtonVariant
import com.example.adaxintegra.presentation.views.designsystem.atoms.Spacing
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.presentation.views.designsystem.molecules.LabeledDropdownField
import com.example.adaxintegra.presentation.views.designsystem.molecules.LabeledTextArea
import com.example.adaxintegra.presentation.views.designsystem.molecules.LabeledTextField
import com.example.adaxintegra.presentation.views.designsystem.organisms.SuccessDialog
import com.example.adaxintegra.presentation.views.designsystem.templates.ScreenTemplate
import com.example.adaxintegra.ui.theme.Purple
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun Modifier.dashedBorder(
    color: Color,
    strokeWidth: androidx.compose.ui.unit.Dp,
    dashLength: androidx.compose.ui.unit.Dp = 6.dp,
    gapLength: androidx.compose.ui.unit.Dp = 6.dp,
    cornerRadius: androidx.compose.ui.unit.Dp = 16.dp,
): Modifier = this.drawBehind {
    val stroke = Stroke(
        width = strokeWidth.toPx(),
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(dashLength.toPx(), gapLength.toPx()), 0f)
    )
    drawRoundRect(
        color = color,
        style = stroke,
        cornerRadius = CornerRadius(cornerRadius.toPx(), cornerRadius.toPx())
    )
}

@Composable
fun TimeDropdownInputRow(
    label: String,
    hour: String,
    onHourChange: (String) -> Unit,
    minute: String,
    onMinuteChange: (String) -> Unit,
    amPm: String,
    onAmPmChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var hourExpanded by remember { mutableStateOf(false) }
    var minuteExpanded by remember { mutableStateOf(false) }
    var amPmExpanded by remember { mutableStateOf(false) }

    val locale = remember { Locale.forLanguageTag("es-MX") }
    val hours = remember { (1..12).map { String.format(locale, "%02d", it) } }
    val minutes = remember { (0..59).map { String.format(locale, "%02d", it) } }
    val amPms = remember { listOf("a.m.", "p.m.") }

    Column(modifier = modifier) {
        Text(
            text = label,
            style = AppTextStyle.BodySmall,
            color = Purple,
            fontWeight = FontWeight.Medium,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            border = BorderStroke(1.dp, Color.LightGray),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Hour Dropdown
                Box(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { hourExpanded = true }
                            .padding(vertical = 8.dp, horizontal = 4.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = if (hour.isBlank()) "HH" else hour,
                            style = AppTextStyle.BodyMedium,
                            color = if (hour.isBlank()) Color.Gray else Color.Black,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            tint = Purple,
                            modifier = Modifier.size(16.dp),
                        )
                    }
                    DropdownMenu(
                        expanded = hourExpanded,
                        onDismissRequest = { hourExpanded = false },
                    ) {
                        hours.forEach { h ->
                            DropdownMenuItem(
                                text = { Text(h) },
                                onClick = {
                                    onHourChange(h)
                                    hourExpanded = false
                                }
                            )
                        }
                    }
                }

                Text(text = ":", style = AppTextStyle.BodyMedium, color = Color.Gray)

                // Minute Dropdown
                Box(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { minuteExpanded = true }
                            .padding(vertical = 8.dp, horizontal = 4.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = if (minute.isBlank()) "MM" else minute,
                            style = AppTextStyle.BodyMedium,
                            color = if (minute.isBlank()) Color.Gray else Color.Black,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            tint = Purple,
                            modifier = Modifier.size(16.dp),
                        )
                    }
                    DropdownMenu(
                        expanded = minuteExpanded,
                        onDismissRequest = { minuteExpanded = false },
                    ) {
                        minutes.forEach { m ->
                            DropdownMenuItem(
                                text = { Text(m) },
                                onClick = {
                                    onMinuteChange(m)
                                    minuteExpanded = false
                                }
                            )
                        }
                    }
                }

                // AM/PM Dropdown
                Box(modifier = Modifier.weight(1.2f)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { amPmExpanded = true }
                            .padding(vertical = 8.dp, horizontal = 4.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = amPm,
                            style = AppTextStyle.BodyMedium,
                            color = Purple,
                            fontWeight = FontWeight.Bold,
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            tint = Purple,
                            modifier = Modifier.size(16.dp),
                        )
                    }
                    DropdownMenu(
                        expanded = amPmExpanded,
                        onDismissRequest = { amPmExpanded = false },
                    ) {
                        amPms.forEach { ap ->
                            DropdownMenuItem(
                                text = { Text(ap) },
                                onClick = {
                                    onAmPmChange(ap)
                                    amPmExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEventScreen(
    onBack: () -> Unit,
    onEventCreated: () -> Unit,
    viewModel: CreateEventViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var activeDatePicker by rememberSaveable { mutableStateOf<String?>(null) } // "start" or "end"

    val oneYearAgoMillis = Calendar.getInstance().apply {
        add(Calendar.YEAR, -1)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis

    val fiveYearsFutureMillis = Calendar.getInstance().apply {
        add(Calendar.YEAR, 5)
        set(Calendar.HOUR_OF_DAY, 23)
        set(Calendar.MINUTE, 59)
        set(Calendar.SECOND, 59)
        set(Calendar.MILLISECOND, 999)
    }.timeInMillis

    val datePickerState = rememberDatePickerState(
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis in oneYearAgoMillis..fiveYearsFutureMillis
            }
        }
    )

    if (activeDatePicker != null) {
        DatePickerDialog(
            onDismissRequest = { activeDatePicker = null },
            confirmButton = {
                TextButton(
                    onClick = {
                        val selectedMillis = datePickerState.selectedDateMillis
                        if (selectedMillis != null) {
                            val formattedDate = DateFormatter.dayFromUtcMillis(selectedMillis)
                            if (activeDatePicker == "start") {
                                viewModel.onStartDateChange(formattedDate)
                            } else if (activeDatePicker == "end") {
                                viewModel.onEndDateChange(formattedDate)
                            }
                        }
                        activeDatePicker = null
                    }
                ) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { activeDatePicker = null }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { _ ->
    }

    val spacing = Spacing()
    val canSave = uiState.title.isNotBlank() && uiState.description.isNotBlank() && !uiState.isSaving

    ScreenTemplate(
        title = "Nuevo evento",
        onBack = onBack,
        modifier = Modifier.padding(horizontal = spacing.medium),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(top = spacing.small, bottom = spacing.large),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            LabeledTextField(
                label = "TÍTULO DEL EVENTO",
                value = uiState.title,
                onValueChange = viewModel::onTitleChange,
                placeholder = "Ej. Reunión de equipo - Seguimiento de casos",
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    LabeledTextField(
                        label = "FECHA INICIO",
                        value = uiState.startDate,
                        onValueChange = {},
                        readOnly = true,
                        placeholder = "DD/MM/AAAA",
                        trailingIcon = {
                            IconButton(onClick = { activeDatePicker = "start" }) {
                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = "Seleccionar fecha inicio",
                                )
                            }
                        },
                        modifier = Modifier.clickable { activeDatePicker = "start" },
                    )
                }
                Box(modifier = Modifier.weight(1f)) {
                    LabeledTextField(
                        label = "FECHA FIN",
                        value = uiState.endDate,
                        onValueChange = {},
                        readOnly = true,
                        placeholder = "DD/MM/AAAA",
                        trailingIcon = {
                            IconButton(onClick = { activeDatePicker = "end" }) {
                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = "Seleccionar fecha fin",
                                )
                            }
                        },
                        modifier = Modifier.clickable { activeDatePicker = "end" },
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                TimeDropdownInputRow(
                    label = "HORA INICIO",
                    hour = uiState.startHour,
                    onHourChange = viewModel::onStartHourChange,
                    minute = uiState.startMinute,
                    onMinuteChange = viewModel::onStartMinuteChange,
                    amPm = uiState.startAmPm,
                    onAmPmChange = viewModel::onStartAmPmChange,
                    modifier = Modifier.weight(1f),
                )
                TimeDropdownInputRow(
                    label = "HORA FIN",
                    hour = uiState.endHour,
                    onHourChange = viewModel::onEndHourChange,
                    minute = uiState.endMinute,
                    onMinuteChange = viewModel::onEndMinuteChange,
                    amPm = uiState.endAmPm,
                    onAmPmChange = viewModel::onEndAmPmChange,
                    modifier = Modifier.weight(1f),
                )
            }

            LabeledDropdownField(
                label = "MODALIDAD",
                selectedOption = uiState.modality,
                options = listOf("Presencial", "Virtual", "Mixto"),
                onOptionSelected = viewModel::onModalityChange,
                placeholder = "Selecciona modalidad",
            )

            LabeledTextField(
                label = "LUGAR",
                value = uiState.location,
                onValueChange = viewModel::onLocationChange,
                placeholder = "Ej. Oficinas ADAX",
            )

            LabeledTextField(
                label = "LINK (OPCIONAL)",
                value = uiState.link,
                onValueChange = viewModel::onLinkChange,
                placeholder = "https://meet.google.com/...",
            )

            LabeledTextField(
                label = "TELÉFONO (OPCIONAL)",
                value = uiState.phone,
                onValueChange = viewModel::onPhoneChange,
                placeholder = "55 1234 5678",
                leadingIcon = {
                    Box(
                        modifier = Modifier.padding(start = 16.dp, end = 4.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "+52",
                            style = AppTextStyle.BodyMedium,
                            color = Color.Gray,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            )

            LabeledTextArea(
                label = "DESCRIPCIÓN",
                value = uiState.description,
                onValueChange = viewModel::onDescriptionChange,
                maxLength = 500,
                placeholder = "Detalles del evento",
                minLines = 4,
            )

            LabeledTextArea(
                label = "NOTAS",
                value = uiState.notes,
                onValueChange = viewModel::onNotesChange,
                maxLength = 500,
                placeholder = "Opcional",
                minLines = 3,
            )

            // Image upload section matching the screenshot
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "IMAGEN DEL EVENTO",
                    style = AppTextStyle.BodySmall,
                    color = Purple,
                    fontWeight = FontWeight.Medium,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .dashedBorder(
                            color = MaterialTheme.colorScheme.primary,
                            strokeWidth = 2.dp,
                            cornerRadius = 16.dp
                        )
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                        .clickable {
                            imagePickerLauncher.launch("image/*")
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowUpward,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp),
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Agregar imagen",
                            style = AppTextStyle.BodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "PNG o JPG · máx. 4 MB",
                            style = AppTextStyle.LabelSmall,
                            color = Color.Gray,
                        )
                    }
                }
            }

            if (uiState.error != null) {
                Text(
                    text = uiState.error ?: "",
                    style = AppTextStyle.BodyMedium,
                    color = MaterialTheme.colorScheme.error,
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                AppButton(
                    text = "Guardar evento",
                    onClick = {
                        viewModel.saveEvent(onSuccess = onEventCreated)
                    },
                    modifier = Modifier.weight(1f),
                    enabled = canSave,
                    isLoading = uiState.isSaving,
                )

                AppButton(
                    text = "Cancelar",
                    onClick = onBack,
                    modifier = Modifier.weight(1f),
                    variant = ButtonVariant.Outlined,
                    enabled = !uiState.isSaving,
                )
            }
        }
    }

    if (uiState.successMessage != null) {
        SuccessDialog(
            title = "Evento creado",
            message = uiState.successMessage ?: "El evento ha sido registrado exitosamente.",
            onConfirm = {
                viewModel.dismissSuccess()
                onEventCreated()
            },
        )
    }
}
