package com.example.adaxintegra.presentation.views.designsystem.organisms.timeline

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.presentation.views.designsystem.organisms.CaseProgressCard

//complete timeline with cards
@Composable
fun CaseProgressTimeline() {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        //step 1
        TimelineItem(
            status = TimelineStatus.Completed,
            isLast = false
        ) {
            CaseProgressCard(
                stepNumber = 1,
                title = "Primer contacto",
                status = "Completado",
                description = "Se estableció el primer contacto y se brindó información sobre los servicios y el proceso de acompañamiento."
            )
        }

        //step 2
        TimelineItem(
            status = TimelineStatus.InProgress,
            isLast = false
        ) {
            CaseProgressCard(
                stepNumber = 2,
                title = "Evaluación inicial",
                status = "En curso",
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
        }

        //step 3
        TimelineItem(
            status = TimelineStatus.NotStarted,
            isLast = true
        ) {
            CaseProgressCard(
                stepNumber = 3,
                title = "Acompañamiento",
                status = "Pendiente",
                description = "Actualmente se brinda acompañamiento según las necesidades identificadas.",
                note = "Sesiones psicologicas semanales."
            )
        }
    }
}