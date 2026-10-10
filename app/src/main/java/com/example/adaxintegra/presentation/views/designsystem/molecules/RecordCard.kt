package com.example.adaxintegra.presentation.views.designsystem.molecules

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.domain.entities.Record
import com.example.adaxintegra.presentation.model.RecordStatusUi
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppIcon
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppIcons
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.PillBadge
import com.example.adaxintegra.presentation.views.designsystem.atoms.Spacing
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.ui.theme.AdaxIntegraTheme

@Suppress("ktlint:standard:function-naming")
@Composable
fun RecordCard(
    record: Record,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val spacing = Spacing()

    // Show a fallback when the record has no folio
    val title = record.recordNumber ?.takeIf { it.isNotBlank() } ?: "Sin folio"

    // Translate known statuses and show a fallback for unknown values.
    val statusText = RecordStatusUi.from(record.status) ?.displayText ?: "Estado desconocido"

    val activeCasesText = when (record.activeCasesCount) {
        0 -> "Ningún caso activo"
        1 -> "1 caso activo"
        else -> "${record.activeCasesCount} casos activos"
    }

    // Show the supplied date without assigning a time zone.
    val updatedDate = record.updatedAt ?.takeIf { it.isNotBlank() }?.substringBefore('T') ?.substringBefore(' ') ?: "Sin registro"

    // The screen handles navigation when the card is selected.
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier.padding(spacing.medium),
            verticalArrangement = Arrangement.spacedBy(spacing.small),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(spacing.small),
                verticalAlignment = Alignment.Top,
            ) {
                Text(
                    text = title,
                    modifier = Modifier.weight(1f),
                    style = AppTextStyle.TitleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )

                PillBadge(text = statusText)
            }

            Text(
                text = record.name,
                style = AppTextStyle.BodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )

            // The adjacent text describes each icon.
            Row(
                horizontalArrangement = Arrangement.spacedBy(spacing.small),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AppIcon(
                    imageVector = AppIcons.Folder,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                )

                Text(
                    text = activeCasesText,
                    modifier = Modifier.weight(1f),
                    style = AppTextStyle.BodySmall,
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(spacing.small),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AppIcon(
                    imageVector = AppIcons.Clock,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                Text(
                    text = "Última actualización: $updatedDate",
                    modifier = Modifier.weight(1f),
                    style = AppTextStyle.BodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
private fun RecordCardPreview() {
    val spacing = Spacing()

    // Fictional data allows previews without a backend connection.
    val record = Record(
        recordId = "preview-record",
        userId = "preview-user",
        name = "Ana Pérez",
        recordNumber = "EXP-2026-0001",
        status = "EN_REVISION",
        activeCasesCount = 3,
        updatedAt = "2026-10-06T10:30:00",
    )

    AdaxIntegraTheme(dynamicColor = false) {
        Column(
            modifier = Modifier.padding(spacing.medium),
            verticalArrangement = Arrangement.spacedBy(spacing.medium),
        ) {
            RecordCard(
                record = record,
                onClick = {},
            )

            RecordCard(
                record = record.copy(
                    recordNumber = null,
                    status = "SIN_EMPEZAR",
                    activeCasesCount = 0,
                ),
                onClick = {},
            )
        }
    }
}
