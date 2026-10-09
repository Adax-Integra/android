package com.example.adaxintegra.presentation.views.designsystem.molecules

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.domain.entities.Case
import com.example.adaxintegra.ui.theme.AdaxIntegraTheme

private data class CaseStateUi(
    val textColor: Color,
    val bgColor: Color,
    val accentColor: Color,
)

private fun getCaseStateUi(state: String): CaseStateUi {
    return when (state.trim().lowercase()) {
        "abierto" -> CaseStateUi(
            textColor = Color(0xFF176B35),
            bgColor = Color(0xFFE8F5E9),
            accentColor = Color(0xFF176B35),
        )

        "cerrado" -> CaseStateUi(
            textColor = Color(0xFFE85B0F),
            bgColor = Color(0xFFFDECE3),
            accentColor = Color(0xFFE85B0F),
        )

        else -> CaseStateUi(
            textColor = Color(0xFF595563),
            bgColor = Color(0xFFF0EFF2),
            accentColor = Color(0xFF595563),
        )
    }
}

private fun formatListByCharacterLimit(
    items: List<String>,
    maxChars: Int = 25,
    emptyText: String,
): String {
    if (items.isEmpty()) return emptyText

    val selected = mutableListOf<String>()
    var currentLength = 0
    var hasMore = false

    for (item in items) {
        val additionalLength = if (selected.isEmpty()) item.length else item.length + 2
        if (currentLength + additionalLength <= maxChars) {
            selected.add(item)
            currentLength += additionalLength
        } else {
            hasMore = true
            break
        }
    }

    return when {
        selected.isEmpty() -> {
            "${items.first().take(maxChars - 3)}..."
        }
        hasMore -> {
            "${selected.joinToString(", ")}, ..."
        }
        else -> {
            selected.joinToString(", ")
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun CaseCard(
    case: Case,
    modifier: Modifier = Modifier,
) {
    val stateUi = getCaseStateUi(case.state)
    val violenceText = formatListByCharacterLimit(
        items = case.violenceTypes,
        maxChars = 28,
        emptyText = "Sin tipos de violencia registrados",
    )
    val internsText = formatListByCharacterLimit(
        items = case.internsAssigned,
        maxChars = 24,
        emptyText = "Sin asignar",
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            // Left accent line with the color based on the state of the case
            Box(
                modifier = Modifier
                    .width(5.dp)
                    .fillMaxHeight()
                    .background(stateUi.accentColor),
            )
            // Main section
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                // Everything above the divider line
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top,
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "N.º DE CASO",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Text(
                            text = case.caseNumber ?: case.caseId,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = case.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }

                    // Pill with the state of the case
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = stateUi.bgColor,
                    ) {
                        Text(
                            text = case.state.ifBlank { "Sin estado" },
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.labelMedium,
                            color = stateUi.textColor,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                // Middle section: Violencia and Asignada al caso
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Text(
                            text = "Violencia: ",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Text(
                            text = violenceText,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Text(
                            text = "Asignada al caso: ",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Text(
                            text = internsText,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }

                // Section of the last modification
                Text(
                    text = "Última modificación: ${case.updatedAt?.substringBefore('T') ?: "Sin registro"}",
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.End,
                )
            }
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
private fun CaseCardOpenPreview() {
    AdaxIntegraTheme {
        CaseCard(
            case = Case(
                caseId = "13221-4231",
                name = "Adriana Velásquez Mondragón",
                violenceTypes = listOf("Sexual", "Psicológica", "Física"),
                state = "Abierto",
                severity = null,
                urgency = "",
                updatedAt = "06-01-2026",
                caseNumber = "CAS-26-0001",
                internsAssigned = listOf("Alejandra Beatriz Benítez Mondragón", "María Gómez"),
            ),
            modifier = Modifier.padding(12.dp),
        )
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
private fun CaseCardClosedPreview() {
    AdaxIntegraTheme {
        CaseCard(
            case = Case(
                caseId = "13221-4234",
                name = "Ana Martínez Ruiz",
                violenceTypes = listOf("Física"),
                state = "Cerrado",
                severity = null,
                urgency = "",
                updatedAt = "05-01-2026",
                caseNumber = "CAS-27-0516",
                internsAssigned = listOf("Dana Ortíz"),
            ),
            modifier = Modifier.padding(12.dp),
        )
    }
}
