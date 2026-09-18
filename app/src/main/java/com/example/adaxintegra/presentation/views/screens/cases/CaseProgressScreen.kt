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
        CaseProgressCard(
            stepNumber = 1,
            title = "Primer contacto",
            status = "Completado",
            description = "Se estableció el primer contacto y se brindó información sobre los servicios y el proceso de acompañamiento."
        )

        CaseProgressCard(
            stepNumber = 2,
            title = "Evaluación inicial",
            status = "Completado",
            description = "Se realizó la evaluación inicial para conocer su situación y necesidades específicas.",
            note = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
                    "sed do eiusmod tempor incididunt ut labore et dolore magna " +
                    "aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco " +
                    "laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure " +
                    "dolor in reprehenderit in voluptate velit esse cillum dolore eu " +
                    "fugiat nulla pariatur. Excepteur sint occaecat cupidatat non " +
                    "proident, sunt in culpa qui officia deserunt mollit anim id est " +
                    "laborum."
        )

        CaseProgressCard(
            stepNumber = 3,
            title = "Acompañamiento",
            status = "En curso",
            description = "Actualmente se brinda acompañamiento según las necesidades identificadas.",
            note = "Sesiones psicologicas semanales."
        )
    }
}