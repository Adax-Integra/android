package com.example.adaxintegra.presentation.views.screens.cases

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.adaxintegra.presentation.viewmodel.ExternalCasesViewModel
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppButton
import com.example.adaxintegra.presentation.views.designsystem.atoms.Spacing
import com.example.adaxintegra.presentation.views.designsystem.organisms.CaseSummaryCard
import com.example.adaxintegra.presentation.views.designsystem.organisms.CasesListHeader
import com.example.adaxintegra.presentation.views.designsystem.templates.ScreenTemplate

// V-04: external user's case list, reached through bottom nav bar option
@Suppress("ktlint:standard:function-naming")
@Composable
fun ExternalCasesScreen(
    viewModel: ExternalCasesViewModel = hiltViewModel(),
    onCaseClick: (String) -> Unit = {},
    onCreateCaseClick: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val spacing = Spacing()

    //Loaded every time the screen appears, so the case created in R-02 shows up
    LaunchedEffect(Unit) {
        viewModel.loadCases()
    }

    ScreenTemplate(
        title = "Casos",
        isLoading = uiState.isLoading,
        error = uiState.error,
        modifier = Modifier.padding(horizontal = spacing.medium),
    ) {
        Column(
            modifier = Modifier.padding(top = spacing.small),
            verticalArrangement = Arrangement.spacedBy(spacing.medium),
        ) {

            CasesListHeader(
                searchQuery = uiState.searchQuery,
                onSearchQueryChange = viewModel::onSearchQueryChange,
                count = uiState.cases.size,
            )

            //R-02: entry point to the "Registrar el caso" screen
            AppButton(
                text = "+ Nuevo caso",
                onClick = onCreateCaseClick,
                modifier = Modifier.fillMaxWidth(),
            )

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(uiState.cases) { caso ->
                    CaseSummaryCard(
                        caseItem = caso,
                        onClick = { onCaseClick(caso.caseId) },
                    )
                }
            }
        }
    }
}
