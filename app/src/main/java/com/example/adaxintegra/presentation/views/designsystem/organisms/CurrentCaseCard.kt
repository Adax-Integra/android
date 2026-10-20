package com.example.adaxintegra.presentation.views.designsystem.organisms

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.domain.model.Case
import com.example.adaxintegra.domain.model.CaseProgressStep
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppButton
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppIcon
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppIcons
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.presentation.views.designsystem.organisms.timeline.HorizontalCaseProgress
import com.example.adaxintegra.ui.theme.AdaxIntegraTheme
import com.example.adaxintegra.ui.theme.Black
import com.example.adaxintegra.ui.theme.IconGrey
import com.example.adaxintegra.ui.theme.White
import java.util.Date

// NV-01: card of the home that answers "do I have an open case". With a case it opens
// its progress (V-07), without one it offers to register it (R-02)

@Suppress("ktlint:standard:function-naming")
@Composable
fun CurrentCaseCard(
    currentCase: Case?,
    caseSteps: List<CaseProgressStep> = emptyList(),
    onCaseClick: (String) -> Unit,
    onRegisterCaseClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    error: String? = null,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(White)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Tu caso en curso",
                modifier = Modifier.weight(1f),
                style = AppTextStyle.TitleLarge,
                fontWeight = FontWeight.Bold,
                color = Black,
            )

            //V-07: only a card that already has a case can open its progress
            if (currentCase != null) {
                Row(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.08f))
                        .clickable { onCaseClick(currentCase.caseId) }
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        text = "Ver detalles",
                        style = AppTextStyle.LabelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    AppIcon(
                        imageVector = AppIcons.ChevronRight,
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        }

        when {
            isLoading -> {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            }

            error != null -> {
                Text(
                    text = error,
                    style = AppTextStyle.BodyMedium,
                    fontWeight = FontWeight.Normal,
                    color = IconGrey,
                )
            }

            currentCase == null -> {
                Text(
                    text = "Todavía no tienes un caso en curso.",
                    style = AppTextStyle.BodyMedium,
                    fontWeight = FontWeight.Normal,
                    color = IconGrey,
                )

                //NV-01: same entry point as "Mis Casos", so R-02 is one tap from the home
                if (onRegisterCaseClick != null) {
                    AppButton(
                        text = "+ Nuevo caso",
                        onClick = onRegisterCaseClick,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }

            else -> {
                if (caseSteps.isEmpty()) {
                    Text(
                        text = "Este caso todavía no tiene pasos registrados.",
                        style = AppTextStyle.BodyMedium,
                        fontWeight = FontWeight.Normal,
                        color = IconGrey,
                    )
                } else {
                    HorizontalCaseProgress(caseSteps = caseSteps)
                }
            }
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
fun CurrentCaseCardPreviewWithCase() {
    AdaxIntegraTheme {
        CurrentCaseCard(
            currentCase = Case(
                caseId = "6a6s5a65-fa65s5a-ADda5d",
                caseNumber = "CASO-2026-112",
                state = "NUEVO",
                caseSteps = null,
                description = "Descripción de prueba",
                helpWanted = "Asesoría legal",
                hasLawyer = false,
                violenceList = emptyList(),
                helpList = null,
                createdAt = Date(),
                updatedAt = Date(),
            ),
            caseSteps = listOf(
                CaseProgressStep(stepNumber = 1, status = "completed"),
                CaseProgressStep(stepNumber = 2, status = "in_progress"),
                CaseProgressStep(stepNumber = 3, status = "not_started"),
            ),
            onCaseClick = {},
            onRegisterCaseClick = {},
        )
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
fun CurrentCaseCardPreviewEmpty() {
    AdaxIntegraTheme {
        CurrentCaseCard(
            currentCase = null,
            onCaseClick = {},
            onRegisterCaseClick = {},
        )
    }
}
