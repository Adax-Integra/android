package com.example.adaxintegra.presentation.views.screens.cases

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.adaxintegra.domain.entities.Case
import com.example.adaxintegra.presentation.viewmodel.CasesFromUserUiState
import com.example.adaxintegra.presentation.viewmodel.ExternalCasesViewModel
import com.example.adaxintegra.presentation.views.designsystem.molecules.CaseCard
import com.example.adaxintegra.presentation.views.designsystem.templates.ScreenTemplate

@Suppress("ktlint:standard:function-naming")
@Composable
fun RecordFromUser(
    viewModel: ExternalCasesViewModel = hiltViewModel(),
    uiState: CasesFromUserUiState,
    onBackClick: () -> Unit = {},
    onCaseClick: (String) -> Unit = {},
) {
    val userName = uiState.cases.firstOrNull()?.name ?: "Usuario"

    ScreenTemplate(
        title = "Casos de $userName",
        error = uiState.error,
        isLoading = uiState.isLoading,
        onBack = onBackClick,
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            items(
                items = uiState.cases,
                key = { it.caseId },
            ) { case ->
                CaseCard(
                    case = case,
                    modifier = Modifier.clickable { onCaseClick(case.caseId) },
                )
            }
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
private fun RecordFromUserPreview() {
    val mockCases = Case.getMockData()
    RecordFromUser(
        uiState = CasesFromUserUiState(
            cases = mockCases,
        ),
    )
}
