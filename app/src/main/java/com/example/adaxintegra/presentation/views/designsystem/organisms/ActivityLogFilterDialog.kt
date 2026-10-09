package com.example.adaxintegra.presentation.views.designsystem.organisms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
// convert between that value and the local date of the phone
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

// Text of a date field, ex. "08/10/2026"
private fun dayText(dayMillis: Long?): String {
    if (dayMillis == null) return "Seleccionar"
    return DateFormatter.day(buildLocalDate(dayMillis, 0, 0, 0))
}

// V-06: "Filtrar" dialog. The admin chooses a range of dates (from / to).
// Whole days are included: "Desde" starts at 00:00 and "Hasta" ends at 23:59
@Suppress("ktlint:standard:function-naming")
@Composable
fun ActivityLogFilterDialog(
    initialFrom: Date?,
    initialTo: Date?,
    onApply: (from: Date, to: Date) -> Unit,
    onClear: () -> Unit,
    onDismiss: () -> Unit,
) {
    var fromDayMillis by remember { mutableStateOf<Long?>(initialFrom?.let { toPickerMillis(it) }) }
    var toDayMillis by remember { mutableStateOf<Long?>(initialTo?.let { toPickerMillis(it) }) }

    var showFromPicker by remember { mutableStateOf(false) }
    var showToPicker by remember { mutableStateOf(false) }

    val fromDay = fromDayMillis
    val toDay = toDayMillis

    // "Hasta" can be the same day as "Desde", but not an earlier one,
    // so the backend never receives an invalid range
    val isRangeValid = fromDay == null || toDay == null || toDay >= fromDay
    val canApply = fromDay != null && toDay != null && isRangeValid

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
                    text = "Filtrar por fechas",
                    style = AppTextStyle.TitleMedium,
                    fontWeight = FontWeight.Bold,
                )

                HorizontalDivider()

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    FilterField(
                        label = "DESDE",
                        value = dayText(fromDay),
                        onClick = { showFromPicker = true },
                        modifier = Modifier.weight(1f),
                    )
                    FilterField(
                        label = "HASTA",
                        value = dayText(toDay),
                        onClick = { showToPicker = true },
                        modifier = Modifier.weight(1f),
                    )
                }

                if (!isRangeValid) {
                    Text(
                        text = "La fecha final debe ser igual o posterior a la inicial.",
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
                            if (fromDay != null && toDay != null) {
                                onApply(
                                    buildLocalDate(fromDay, 0, 0, 0),
                                    buildLocalDate(toDay, 23, 59, 59),
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

    if (showFromPicker) {
        DateDialog(
            initialDayMillis = fromDayMillis,
            onConfirm = { selected ->
                fromDayMillis = selected
                showFromPicker = false
            },
            onDismiss = { showFromPicker = false },
        )
    }

    if (showToPicker) {
        DateDialog(
            initialDayMillis = toDayMillis,
            onConfirm = { selected ->
                toDayMillis = selected
                showToPicker = false
            },
            onDismiss = { showToPicker = false },
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

// Calendar dialog to choose one day
@Suppress("ktlint:standard:function-naming")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateDialog(
    initialDayMillis: Long?,
    onConfirm: (dayMillis: Long) -> Unit,
    onDismiss: () -> Unit,
) {
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initialDayMillis)

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    val selected = datePickerState.selectedDateMillis
                    if (selected != null) {
                        onConfirm(selected)
                    } else {
                        onDismiss()
                    }
                },
            ) {
                Text(text = "Aceptar", color = Purple, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Cancelar", color = Purple)
            }
        },
    ) {
        DatePicker(state = datePickerState)
    }
}
