package com.example.adaxintegra.presentation.views.designsystem.organisms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.adaxintegra.presentation.util.DateFormatter
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppButton
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.ButtonVariant
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.ui.theme.IconGrey
import com.example.adaxintegra.ui.theme.Purple
import java.util.Calendar
import java.util.Date
import java.util.TimeZone

// The DatePicker works with the day at midnight in UTC; these helpers
// convert between that value and the local date and time of the phone
private fun buildLocalDate(dayMillis: Long, hour: Int, minute: Int, second: Int): Date {
    val utcDay = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    utcDay.timeInMillis = dayMillis

    val local = Calendar.getInstance()
    local.clear()
    local.set(
        utcDay.get(Calendar.YEAR),
        utcDay.get(Calendar.MONTH),
        utcDay.get(Calendar.DAY_OF_MONTH),
        hour,
        minute,
        second,
    )
    return local.time
}

private fun toPickerMillis(date: Date): Long {
    val local = Calendar.getInstance()
    local.time = date

    val utcDay = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    utcDay.clear()
    utcDay.set(
        local.get(Calendar.YEAR),
        local.get(Calendar.MONTH),
        local.get(Calendar.DAY_OF_MONTH),
    )
    return utcDay.timeInMillis
}

private fun hourOf(date: Date?, defaultHour: Int): Int {
    if (date == null) return defaultHour
    val calendar = Calendar.getInstance()
    calendar.time = date
    return calendar.get(Calendar.HOUR_OF_DAY)
}

private fun minuteOf(date: Date?, defaultMinute: Int): Int {
    if (date == null) return defaultMinute
    val calendar = Calendar.getInstance()
    calendar.time = date
    return calendar.get(Calendar.MINUTE)
}

// ex. 9 -> "09"
private fun twoDigits(value: Int): String = if (value < 10) "0$value" else "$value"

// V-06: "Filtrar" dialog. Acceptance criteria: search by date and time.
// The admin chooses one day and a range of hours (from / to)
@Suppress("ktlint:standard:function-naming")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityLogFilterDialog(
    initialFrom: Date?,
    initialTo: Date?,
    onApply: (from: Date, to: Date) -> Unit,
    onClear: () -> Unit,
    onDismiss: () -> Unit,
) {
    var dayMillis by remember { mutableStateOf<Long?>(initialFrom?.let { toPickerMillis(it) }) }
    var fromHour by remember { mutableIntStateOf(hourOf(initialFrom, 0)) }
    var fromMinute by remember { mutableIntStateOf(minuteOf(initialFrom, 0)) }
    var toHour by remember { mutableIntStateOf(hourOf(initialTo, 23)) }
    var toMinute by remember { mutableIntStateOf(minuteOf(initialTo, 59)) }

    var showDatePicker by remember { mutableStateOf(false) }
    var showFromTime by remember { mutableStateOf(false) }
    var showToTime by remember { mutableStateOf(false) }

    // "Hasta" must be later than "Desde", so the backend never receives an invalid range
    val isRangeValid = toHour * 60 + toMinute > fromHour * 60 + fromMinute
    val canApply = dayMillis != null && isRangeValid

    val selectedDay = dayMillis
    val dayText = if (selectedDay != null) {
        DateFormatter.day(buildLocalDate(selectedDay, 0, 0, 0))
    } else {
        "Seleccionar fecha"
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
            ),
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    text = "Filtrar por fecha y hora",
                    style = AppTextStyle.TitleMedium,
                    fontWeight = FontWeight.Bold,
                )

                HorizontalDivider()

                FilterField(
                    label = "FECHA",
                    value = dayText,
                    onClick = { showDatePicker = true },
                )

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    FilterField(
                        label = "DESDE",
                        value = "${twoDigits(fromHour)}:${twoDigits(fromMinute)}",
                        onClick = { showFromTime = true },
                        modifier = Modifier.weight(1f),
                    )
                    FilterField(
                        label = "HASTA",
                        value = "${twoDigits(toHour)}:${twoDigits(toMinute)}",
                        onClick = { showToTime = true },
                        modifier = Modifier.weight(1f),
                    )
                }

                if (!isRangeValid) {
                    Text(
                        text = "La hora final debe ser posterior a la inicial.",
                        style = AppTextStyle.LabelSmall,
                        color = MaterialTheme.colorScheme.error,
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    AppButton(
                        text = "Aplicar",
                        onClick = {
                            val day = dayMillis
                            if (day != null) {
                                onApply(
                                    buildLocalDate(day, fromHour, fromMinute, 0),
                                    buildLocalDate(day, toHour, toMinute, 59),
                                )
                            }
                        },
                        modifier = Modifier.weight(1f),
                        enabled = canApply,
                    )
                    AppButton(
                        text = "Cancelar",
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        variant = ButtonVariant.Outlined,
                    )
                }

                // Only when a filter is already active
                if (initialFrom != null || initialTo != null) {
                    TextButton(
                        onClick = onClear,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                    ) {
                        Text(text = "Quitar filtro", color = Purple, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = dayMillis)

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        dayMillis = datePickerState.selectedDateMillis
                        showDatePicker = false
                    },
                ) {
                    Text(text = "Aceptar", color = Purple, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text(text = "Cancelar", color = Purple)
                }
            },
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showFromTime) {
        TimeDialog(
            title = "Desde",
            initialHour = fromHour,
            initialMinute = fromMinute,
            onConfirm = { hour, minute ->
                fromHour = hour
                fromMinute = minute
                showFromTime = false
            },
            onDismiss = { showFromTime = false },
        )
    }

    if (showToTime) {
        TimeDialog(
            title = "Hasta",
            initialHour = toHour,
            initialMinute = toMinute,
            onConfirm = { hour, minute ->
                toHour = hour
                toMinute = minute
                showToTime = false
            },
            onDismiss = { showToTime = false },
        )
    }
}

// Label with a button that shows the selected value
@Suppress("ktlint:standard:function-naming")
@Composable
private fun FilterField(
    label: String,
    value: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = label,
            style = AppTextStyle.LabelSmall,
            fontWeight = FontWeight.Medium,
            color = IconGrey,
        )
        OutlinedButton(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
        ) {
            Text(
                text = value,
                style = AppTextStyle.BodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

// Dialog to choose an hour in 24 hour format
@Suppress("ktlint:standard:function-naming")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimeDialog(
    title: String,
    initialHour: Int,
    initialMinute: Int,
    onConfirm: (hour: Int, minute: Int) -> Unit,
    onDismiss: () -> Unit,
) {
    val timeState = rememberTimePickerState(
        initialHour = initialHour,
        initialMinute = initialMinute,
        is24Hour = true,
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = title, style = AppTextStyle.TitleMedium, fontWeight = FontWeight.Bold)
        },
        text = {
            TimeInput(state = timeState)
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(timeState.hour, timeState.minute) }) {
                Text(text = "Aceptar", color = Purple, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Cancelar", color = Purple)
            }
        },
    )
}
