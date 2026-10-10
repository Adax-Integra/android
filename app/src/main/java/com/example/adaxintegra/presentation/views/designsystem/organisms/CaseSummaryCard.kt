package com.example.adaxintegra.presentation.views.designsystem.organisms

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
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
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.presentation.views.designsystem.molecules.StatusRow
import com.example.adaxintegra.ui.theme.AdaxIntegraTheme
import com.example.adaxintegra.ui.theme.IconGrey
import java.util.Date

// case card, same design and order as CaseCard in iOS
@Suppress("ktlint:standard:function-naming")
@Composable
fun CaseSummaryCard(
    caseItem: Case,
    onClick: () -> Unit,
) {
    // unknown state from backend is shown as is, in grey
    val status = CaseStatusUi.from(caseItem.state)
    val statusColor = status?.color ?: IconGrey
    val statusText = status?.displayText ?: caseItem.state ?: "Sin estado"
    val violenceText =
        caseItem.violenceList
            .orEmpty()
            .joinToString(", ") { it.type }
            .ifBlank { "Sin tipo registrado" }

    // Theme colors so the card is readable in light and dark mode
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surface)
                .clickable { onClick() },
    ) {
        // color stripe by state
        Box(
            modifier =
                Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .background(statusColor),
        )

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = "ID: ${caseItem.caseId.take(8).uppercase()}",
                style = AppTextStyle.BodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Text(
                text = "Fecha de creación: ${DateFormatter.dateTime(caseItem.createdAt)}",
                style = AppTextStyle.LabelMedium,
                fontWeight = FontWeight.Medium,
                color = IconGrey,
            )

            Text(
                text = violenceText,
                style = AppTextStyle.BodyMedium,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface,
            )

            StatusRow(
                statusText = statusText,
                statusColor = statusColor,
                updatedAt = DateFormatter.relative(caseItem.updatedAt),
            )
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
fun CaseSummaryCardPreview() {
    AdaxIntegraTheme {
        CaseSummaryCard(
            caseItem = Case(
                caseId = "6a6s5a65-fa65s5a-ADda5d", // ID de un caso, generado aleatoriamente
                caseNumber = "CASO-2026-112",
                state = "Querétaro",
                caseSteps = null,
                description = "Descripción de prueba",
                helpWanted = "Asesoría legal",
                hasLawyer = false,
                violenceList = listOf(Violence(type = "Psicológica", severity = 6)),
                helpList = null,
                createdAt = Date(),
                updatedAt = Date(126, 4, 29),
            ),
            onClick = {},
        )
    }
}
