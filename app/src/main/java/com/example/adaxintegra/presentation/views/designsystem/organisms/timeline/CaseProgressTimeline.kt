package com.example.adaxintegra.presentation.views.designsystem.organisms.timeline

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.domain.model.CaseProgressStep
import com.example.adaxintegra.presentation.views.designsystem.organisms.CaseProgressCard

//complete timeline with cards
@Composable
fun CaseProgressTimeline(
    caseSteps: List<CaseProgressStep>
) {
    val sortedSteps = caseSteps.sortedBy { it.stepNumber }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        //loop through the data
        sortedSteps.forEachIndexed { index, step ->

            //three status to identify
            val timelineStatus = when (
                step.status.trim().lowercase()
            ) {
                "completed", "complete", "completado", "completada" ->
                    TimelineStatus.Completed

                "in_progress", "in progress", "en curso" ->
                    TimelineStatus.InProgress

                "not_started", "not started", "pending",
                "pendiente", "no iniciado", "no iniciada" ->
                    TimelineStatus.NotStarted

                else -> TimelineStatus.NotStarted
            }

            //each step has its own title. these dont change
            val title = when (step.stepNumber) {
                1 -> "Primer contacto"
                2 -> "Evaluación inicial"
                3 -> "Acompañamiento"
                else -> "Etapa ${step.stepNumber}"
            }

            //each step has its description
            val description = when (step.stepNumber) {
                1 -> "Se estableció el primer contacto y se brindó información sobre los servicios y el proceso de acompañamiento."
                2 -> "Se realizó la evaluación inicial para conocer su situación y necesidades específicas."
                3 -> "Actualmente se brinda acompañamiento según las necesidades identificadas."
                else -> "Consulta los detalles de esta etapa con el equipo de acompañamiento."
            }

            //actual displayed status that defines the timeline node as well
            val displayStatus = when (timelineStatus) {
                TimelineStatus.Completed -> "Completado"
                TimelineStatus.InProgress -> "En curso"
                TimelineStatus.NotStarted -> "Pendiente"
            }

            TimelineItem(
                status = timelineStatus,
                isLast = index == sortedSteps.lastIndex
            ) {
                CaseProgressCard(
                    stepNumber = step.stepNumber,
                    title = title,
                    status = displayStatus,
                    description = description
                )
            }
        }
    }
}