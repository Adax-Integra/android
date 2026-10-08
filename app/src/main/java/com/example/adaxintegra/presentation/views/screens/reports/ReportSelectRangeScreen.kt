package com.example.adaxintegra.presentation.views.screens.reports

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppButton
import com.example.adaxintegra.presentation.views.designsystem.atoms.ButtonVariant
import com.example.adaxintegra.presentation.views.designsystem.molecules.AppDatePickerDialog
import com.example.adaxintegra.presentation.views.designsystem.organisms.RowOfChips
import com.example.adaxintegra.presentation.views.designsystem.templates.ScreenTemplate
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Suppress("ktlint:standard:function-naming")
@Composable
fun ReportSelectRange(
    onAllCasesClick: () -> Unit,
    onAllRecordsClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    uiState: ReportSelectRangeUiState = ReportSelectRangeUiState(),
    onBackClick: () -> Unit = {},
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDateText by remember { mutableStateOf("") }

    ScreenTemplate(
        title = "Reportes",
        subtitle = "Obten el reporte de los casos",
        error = uiState.error,
        isLoading = uiState.isLoading,
        onBack = onBackClick,
    ) {
        RowOfChips(
            chips = listOf("Mes anterior", "Mes actual", "Trimestre previo"),
            selectedChip = "Mes anterior",
            onChipSelected = {}
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Temp, a button to show the date picker
        AppButton(
            text = if (selectedDateText.isEmpty()) "Seleccionar fecha" else "Fecha: $selectedDateText",
            onClick = { showDatePicker = true },
            variant = ButtonVariant.Outlined,
        )


        if (showDatePicker) {
            AppDatePickerDialog(
                onDateSelected = { millis ->
                    if (millis != null) {
                        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        selectedDateText = formatter.format(Date(millis))
                    }
                },
                onDismiss = { showDatePicker = false }
            )
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
private fun ReportSelectRangePreview() {
    ReportSelectRange(onAllCasesClick = {})
}
