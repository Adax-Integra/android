package com.example.adaxintegra.presentation.views.screens.cases

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.adaxintegra.presentation.viewmodel.CaseProgressViewModel
import com.example.adaxintegra.presentation.views.designsystem.organisms.AppHeader
import com.example.adaxintegra.presentation.views.designsystem.organisms.timeline.CaseProgressTimeline

//case progress screen external view, reached by clicking on a case from case screen
@Composable
fun CaseProgressScreen(
    caseState: String,
    caseId: String,
    onBack: () -> Unit,
    viewModel: CaseProgressViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(caseId) {
        viewModel.loadCase(caseId)
    }

    val case = uiState.case

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AppHeader(
            title = "Caso $caseState",
            subtitle = "ID: ${case?.caseNumber ?: caseId}",
            onBack = onBack
        )
        when {
            uiState.isLoading -> {
                CircularProgressIndicator()
            }

            uiState.error != null -> {
                Text(text = uiState.error!!,)
            }

            case != null -> {
                CaseProgressTimeline(
                    caseSteps = case.caseSteps
                )
            }
        }
    }
}