package com.example.adaxintegra.presentation.views.screens.cases

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.presentation.views.designsystem.organisms.AppHeader
import com.example.adaxintegra.presentation.views.designsystem.organisms.CaseProgressCard
import com.example.adaxintegra.presentation.views.designsystem.organisms.timeline.CaseProgressTimeline

//case progress screen external view, reached by clicking on a case from case screen
@Composable
fun CaseProgressScreen(
    caseState: String,
    caseId: String,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AppHeader(
            title = "Caso $caseState",
            subtitle = "ID: $caseId",
            onBack = onBack
        )

        //call timeline with cards
        CaseProgressTimeline()
    }
}