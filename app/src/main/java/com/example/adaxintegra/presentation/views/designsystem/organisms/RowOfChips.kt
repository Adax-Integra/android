package com.example.adaxintegra.presentation.views.designsystem.organisms

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.presentation.views.designsystem.atoms.ChipClickable

@Suppress("ktlint:standard:function-naming")
@Composable
fun RowOfChips(
    chips: List<String>,
    selectedChip: String,
    onChipSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.horizontalScroll(rememberScrollState()),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        chips.forEach { chip ->
            ChipClickable(
                text = chip,
                selected = chip == selectedChip,
                onClick = { onChipSelected(chip) },
            )
        }
    }
}

@Preview
@Composable
fun RowOfChipsPreview() {
    RowOfChips(
        chips = listOf("Todos", "Este mes", "Urgencia", "No urgente"),
        selectedChip = "Todos",
        onChipSelected = {},
    )
}

