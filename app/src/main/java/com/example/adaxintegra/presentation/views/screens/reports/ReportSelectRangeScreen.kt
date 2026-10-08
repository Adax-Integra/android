package com.example.adaxintegra.presentation.views.screens.reports

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.presentation.views.designsystem.molecules.AppDatePickerDialog
import com.example.adaxintegra.presentation.views.designsystem.molecules.DateField
import com.example.adaxintegra.presentation.views.designsystem.organisms.RowOfChips
import com.example.adaxintegra.presentation.views.designsystem.templates.ScreenTemplate
import java.util.Calendar

@Suppress("ktlint:standard:function-naming")
@Composable
fun ReportSelectRange(
    onStartDateSelected: (Calendar) -> Unit = {},
    onEndDateSelected: (Calendar) -> Unit = {},
    onGenerateReportClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    uiState: ReportSelectRangeUiState = ReportSelectRangeUiState(),
    onBackClick: () -> Unit = {},
) {
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    ScreenTemplate(
        title = "Reportes",
        subtitle = "Obten el reporte de los casos",
        error = uiState.error,
        isLoading = uiState.isLoading,
        onBack = onBackClick,
    ) {
        Column() {

            Text(
                text = "Selecciona un rango predeterminado o selecciona uno más específico",
                style = AppTextStyle.BodyMedium,
            )

            Spacer(modifier = Modifier.height(16.dp))

            RowOfChips(
                chips = listOf("Mes anterior", "Mes actual", "Trimestre previo"),
                selectedChip = "Mes anterior",
                onChipSelected = {},
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Start Date Field
            DateField(
                label = "Inicio del reporte",
                value = uiState.startDate,
                errorMessage = uiState.startDateError,
                onClick = { showStartDatePicker = true }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // End Date Field
            DateField(
                label = "Fin del reporte",
                value = uiState.endDate,
                errorMessage = uiState.endDateError,
                onClick = { showEndDatePicker = true },
            )

            Spacer(modifier = Modifier.height(24.dp))

            AppButton(
                text = "Generar Reporte",
                onClick = onGenerateReportClick,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        // Opens the date picker for the start date
        if (showStartDatePicker) {
            AppDatePickerDialog(
                initialSelectedDateMillis = uiState.startDate?.timeInMillis,
                onDateSelected = { millis ->
                    if (millis != null) {
                        val calendar = Calendar.getInstance().apply { timeInMillis = millis }
                        onStartDateSelected(calendar)
                    }
                },
                onDismiss = { showStartDatePicker = false },
                title = "Selecciona la fecha de inicio",
            )
        }

        // Opens the date picker for the end date
        if (showEndDatePicker) {
            AppDatePickerDialog(
                initialSelectedDateMillis = uiState.endDate?.timeInMillis,
                onDateSelected = { millis ->
                    if (millis != null) {
                        val calendar = Calendar.getInstance().apply { timeInMillis = millis }
                        onEndDateSelected(calendar)
                    }
                },
                onDismiss = { showEndDatePicker = false },
                title = "Selecciona la fecha de fin",

                )
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
private fun ReportSelectRangePreview() {
    ReportSelectRange()
}
