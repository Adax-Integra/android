package com.example.adaxintegra.presentation.views.designsystem.organisms

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.domain.model.Case
import com.example.adaxintegra.domain.model.Violence
import com.example.adaxintegra.presentation.model.CaseStatusUi
import com.example.adaxintegra.presentation.util.DateFormatter
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppButton
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.presentation.views.designsystem.molecules.StatusRow
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
    onCaseClick: (String) -> Unit,
    onRegisterCaseClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    error: String? = null,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(White)
            .then(
                //Only a card that already has a case is worth tapping
                if (currentCase != null) {
                    Modifier.clickable { onCaseClick(currentCase.caseId) }
                } else {
                    Modifier
                },
            ).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "Tu caso en curso",
            style = AppTextStyle.TitleLarge,
            fontWeight = FontWeight.Bold,
            color = Black,
        )

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
                AppButton(
                    text = "+ Nuevo caso",
                    onClick = onRegisterCaseClick,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            else -> {
                //Same three lines CaseSummaryCard shows in V-04, so the case reads the same
                val status = CaseStatusUi.from(currentCase.state)

                Text(
                    text = "ID: ${currentCase.caseId.take(8).uppercase()}",
                    style = AppTextStyle.BodyLarge,
                    fontWeight = FontWeight.SemiBold,
                )

                Text(
                    text = currentCase.violenceList
                        .orEmpty()
                        .joinToString(", ") { it.type }
                        .ifBlank { "Sin tipo registrado" },
                    style = AppTextStyle.BodyMedium,
                    fontWeight = FontWeight.Normal,
                )

                StatusRow(
                    statusText = status?.displayText ?: currentCase.state ?: "Sin estado",
                    statusColor = status?.color ?: IconGrey,
                    updatedAt = DateFormatter.relative(currentCase.updatedAt),
                )
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
                violenceList = listOf(Violence(type = "Psicológica", severity = 6)),
                helpList = null,
                createdAt = Date(),
                updatedAt = Date(),
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
