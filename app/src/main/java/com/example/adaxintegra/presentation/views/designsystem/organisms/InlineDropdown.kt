package com.example.adaxintegra.presentation.views.designsystem.organisms

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.domain.entities.Case
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppButton
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.presentation.views.designsystem.molecules.CaseCard

@Suppress("ktlint:standard:function-naming")
@Composable
fun InlineDropdown(
    title: String,
    modifier: Modifier = Modifier,
    initialExpanded: Boolean = false,
    isExpanded: Boolean? = null,
    onExpandedChange: ((Boolean) -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    var internalExpanded by remember { mutableStateOf(initialExpanded) }
    val expanded = isExpanded ?: internalExpanded

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    val nextState = !expanded
                    if (isExpanded == null) {
                        internalExpanded = nextState
                    }
                    onExpandedChange?.invoke(nextState)
                }
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = title,
                style = AppTextStyle.TitleMedium,
                fontWeight = FontWeight.Bold,
            )

            Icon(
                imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = if (expanded) "Colapsar $title" else "Expandir $title",
            )
        }

        // The content inside the block
        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
            ) {
                content()
            }
        }
    }
}

// The same example but one closed and other opened
@Preview(showBackground = true)
@Composable
fun InlineDropdownPreviewClosed() {
    InlineDropdown(
        title = "Dropdown Title"
    ) {

        Text(
            text = "Hola",
            style = AppTextStyle.BodyLarge,
            fontWeight = FontWeight.Normal,
        )
        AppButton(
            text = "Ejemplo de botón",
            onClick = {},
        )
        CaseCard(
            case = Case(
                caseId = "13221-4231",
                name = "Adriana Velásquez Mondragón",
                violenceTypes = listOf("Sexual", "Psicológica", "Física"),
                state = "Abierto",
                severity = null,
                urgency = "Alta",
                updatedAt = "06-01-2026",
                caseNumber = "CAS-2026-0001",
                internsAssigned = listOf("Alejandra Beatriz Benítez Mondragón", "María Gómez"),
            ),
            modifier = Modifier.padding(12.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun InlineDropdownPreviewOpen() {
    InlineDropdown(
        title = "Dropdown Title",
        initialExpanded = true,
    ) {

        Text(
            text = "Hola",
            style = AppTextStyle.BodyLarge,
            fontWeight = FontWeight.Normal,
        )
        AppButton(
            text = "Ejemplo de botón",
            onClick = {},
        )
        CaseCard(
            case = Case(
                caseId = "13221-4231",
                name = "Adriana Velásquez Mondragón",
                violenceTypes = listOf("Sexual", "Psicológica", "Física"),
                state = "Abierto",
                severity = null,
                urgency = "Alta",
                updatedAt = "06-01-2026",
                caseNumber = "CAS-2026-0001",
                internsAssigned = listOf("Alejandra Beatriz Benítez Mondragón", "María Gómez"),
            ),
            modifier = Modifier.padding(12.dp),
        )
    }
}
