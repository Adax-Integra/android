package com.example.adaxintegra.presentation.views.designsystem.molecules

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.util.Calendar

// Jetpack Compose implementation of a time picker
// got from https://developer.android.com/develop/ui/compose/components/time-pickers
@OptIn(ExperimentalMaterial3Api::class)
@Suppress("ktlint:standard:function-naming")
@Composable
fun AppTimePickerDialog(
    onTimeSelected: (hour: Int, minute: Int) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    title: String = "Selecciona una hora",
    initialHour: Int = Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
    initialMinute: Int = Calendar.getInstance().get(Calendar.MINUTE),
    is24Hour: Boolean = false,
) {
    val timePickerState = rememberTimePickerState(
        initialHour = initialHour,
        initialMinute = initialMinute,
        is24Hour = is24Hour,
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onTimeSelected(timePickerState.hour, timePickerState.minute)
                    onDismiss()
                },
            ) {
                Text("Aceptar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        },
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.padding(top = 24.dp, start = 24.dp, end = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (title.isNotEmpty()) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                )
            }
            TimePicker(
                state = timePickerState,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AppTimePickerDialogPreview() {
    AppTimePickerDialog(
        onTimeSelected = { _, _ -> },
        onDismiss = {},
    )
}
