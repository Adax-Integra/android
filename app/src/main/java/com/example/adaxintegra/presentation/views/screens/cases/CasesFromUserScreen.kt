package com.example.adaxintegra.presentation.views.screens.cases

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.adaxintegra.domain.entities.Case
import com.example.adaxintegra.presentation.viewmodel.CasesFromUserUiState
import com.example.adaxintegra.presentation.viewmodel.CasesFromUserViewModel
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppButton
import com.example.adaxintegra.presentation.views.designsystem.molecules.CaseCard
import com.example.adaxintegra.presentation.views.designsystem.organisms.MenuOptionCard
import com.example.adaxintegra.presentation.views.designsystem.templates.ScreenTemplate

@Suppress("ktlint:standard:function-naming")
@Composable
fun RecordFromUser(
    userId: String,
    viewModel: CasesFromUserViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onCaseClick: (String) -> Unit = {},
    onCreateCaseClick: () -> Unit = {},
    onDocumentsClick: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(userId) {
        viewModel.loadCasesFromUser(userId)
    }

    RecordFromUserContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onCaseClick = onCaseClick,
        onCreateCaseClick = onCreateCaseClick,
        onDocumentsClick = onDocumentsClick,
    )
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun RecordFromUserContent(
    uiState: CasesFromUserUiState,
    onBackClick: () -> Unit = {},
    onCaseClick: (String) -> Unit = {},
    onCreateCaseClick: () -> Unit = {},
    onDocumentsClick: () -> Unit = {},
) {
    val userName = uiState.cases.firstOrNull()?.name ?: "Usuario"

    ScreenTemplate(
        title = "Casos de $userName",
        error = uiState.error,
        isLoading = uiState.isLoading,
        onBack = onBackClick,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            // V-13: identity documents the external user uploaded in R-06
            MenuOptionCard(
                icon = Icons.Outlined.Badge,
                title = "Documentos",
                subtitle = "INE y comprobante de domicilio",
                onClick = onDocumentsClick,
            )

            AppButton(
                text = "Registrar nuevo caso",
                onClick = onCreateCaseClick,
                modifier = Modifier.fillMaxWidth(),
            )

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
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
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
private fun RecordFromUserPreview() {
    val mockCases = Case.getMockData()
    RecordFromUserContent(
        uiState = CasesFromUserUiState(
            cases = mockCases,
        ),
    )
}
