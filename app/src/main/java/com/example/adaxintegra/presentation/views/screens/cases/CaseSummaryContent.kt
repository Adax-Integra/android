package com.example.adaxintegra.presentation.views.screens.cases

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.presentation.views.designsystem.organisms.CaseDescriptionCard
import com.example.adaxintegra.presentation.views.designsystem.organisms.CaseHelpCard

//cards allows external user to view their case info
@Composable
fun CaseSummaryContent (
    description: String?,
    helpWanted: String?,
    modifier: Modifier = Modifier,
) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {

        if (!description.isNullOrBlank()) {
            CaseDescriptionCard(
                description = description,
            )
        }

        if (!helpWanted.isNullOrBlank()) {
            CaseHelpCard(
                helpWanted = helpWanted,
            )
        }
    }
}
