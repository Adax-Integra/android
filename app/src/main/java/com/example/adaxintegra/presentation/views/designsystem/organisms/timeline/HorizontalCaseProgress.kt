package com.example.adaxintegra.presentation.views.designsystem.organisms.timeline

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.domain.model.CaseProgressStep
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppIcon
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppIcons
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.IconSize
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.ui.theme.AdaxIntegraTheme
import com.example.adaxintegra.ui.theme.White

// V-07: the three case steps side by side for the home card (iOS HomeCaseProgressTimeline)
@Suppress("ktlint:standard:function-naming")
@Composable
fun HorizontalCaseProgress(
    caseSteps: List<CaseProgressStep>,
    modifier: Modifier = Modifier,
) {
    val sortedSteps = caseSteps.sortedBy { it.stepNumber }
    val lineColor = MaterialTheme.colorScheme.primary

    Row(modifier = modifier.fillMaxWidth()) {
        sortedSteps.forEachIndexed { index, step ->
            val status = TimelineStatus.from(step.status)

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                // each half line reaches the edge of its column, so neighbours join into one line
                Row(
                    modifier = Modifier.fillMaxWidth().height(44.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    StepLine(
                        color = if (index > 0) lineColor else Color.Transparent,
                        modifier = Modifier.weight(1f),
                    )
                    StepCircle(status = status)
                    StepLine(
                        color = if (index < sortedSteps.lastIndex) lineColor else Color.Transparent,
                        modifier = Modifier.weight(1f),
                    )
                }

                Text(
                    text = caseStepTitle(step.stepNumber),
                    style = AppTextStyle.LabelSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

// each step has its own title. these don't change
fun caseStepTitle(stepNumber: Int): String =
    when (stepNumber) {
        1 -> "Primer contacto"
        2 -> "Evaluación inicial"
        3 -> "Acompañamiento"
        else -> "Etapa $stepNumber"
    }

@Suppress("ktlint:standard:function-naming")
@Composable
private fun StepLine(color: Color, modifier: Modifier = Modifier) {
    Box(modifier = modifier.height(2.dp).background(color))
}

// filled with a check when completed, inner dot when in progress, empty when pending
@Suppress("ktlint:standard:function-naming")
@Composable
private fun StepCircle(status: TimelineStatus, size: Dp = 44.dp) {
    val primary = MaterialTheme.colorScheme.primary

    Box(
        modifier = Modifier
            .size(size)
            .background(if (status == TimelineStatus.Completed) primary else White, CircleShape)
            .border(2.dp, primary, CircleShape)
            .semantics { contentDescription = status.displayText },
        contentAlignment = Alignment.Center,
    ) {
        when (status) {
            TimelineStatus.Completed -> AppIcon(
                imageVector = AppIcons.CheckMark,
                size = IconSize.LargeIcon,
                tint = MaterialTheme.colorScheme.onPrimary,
            )

            TimelineStatus.InProgress -> Box(
                modifier = Modifier.size(size * 0.4f).background(primary, CircleShape),
            )

            TimelineStatus.NotStarted -> Unit
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
fun HorizontalCaseProgressPreview() {
    AdaxIntegraTheme {
        HorizontalCaseProgress(
            caseSteps = listOf(
                CaseProgressStep(stepNumber = 1, status = "completed"),
                CaseProgressStep(stepNumber = 2, status = "in_progress"),
                CaseProgressStep(stepNumber = 3, status = "not_started"),
            ),
        )
    }
}
