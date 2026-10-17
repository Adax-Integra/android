package com.example.adaxintegra.presentation.views.designsystem.molecules

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


// Jetpack Compose implementation of a date picker
// got from https://developer.android.com/develop/ui/compose/components/datepickers
@Suppress("ktlint:standard:function-naming")
@Composable
fun AppDatePickerDialog(
    onDateSelected: (selectedDateMillis: Long?) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    title: String = "Selecciona una fecha",
    initialSelectedDateMillis: Long? = null,
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialSelectedDateMillis ?: System.currentTimeMillis(),
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onDateSelected(datePickerState.selectedDateMillis)
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
        DatePicker(
            state = datePickerState,
            title = {
                Text(
                    text = title,
                    modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 16.dp),
                )
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AppDatePickerDialogPreview() {
    AppDatePickerDialog(
        onDateSelected = {},
        onDismiss = {},
    )
}
