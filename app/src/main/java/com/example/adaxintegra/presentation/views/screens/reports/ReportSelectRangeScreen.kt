package com.example.adaxintegra.presentation.views.screens.reports

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.adaxintegra.presentation.views.designsystem.templates.ScreenTemplate

@Suppress("ktlint:standard:function-naming")
@Composable
fun ReportSelectRange(
    onAllCasesClick: () -> Unit,
    onAllRecordsClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    uiState: ReportSelectRangeUiState = ReportSelectRangeUiState(),
    onBackClick: () -> Unit = {},
    ) {
    ScreenTemplate(
        title = "Reportes",
        subtitle = "Obten el reporte de los casos",
        error = uiState . error,
        isLoading = uiState.isLoading,
        onBack = onBackClick,
    ) {

    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
private fun ReportSelectRangePreview() {
    ReportSelectRange(onAllCasesClick = {})
}
