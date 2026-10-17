package com.example.adaxintegra.presentation.views.designsystem.molecules

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text

@Suppress("ktlint:standard:function-naming")
@Composable
fun MultipleCheckboxes(
    options: List<String>,
    selectedOptions: Set<String>,
    onOptionToggle: (String) -> Unit,
    modifier: Modifier = Modifier,
) {

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        options.forEach { option ->
            val isChecked = option in selectedOptions

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOptionToggle(option) }
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = { onOptionToggle(option) },
                )
                Text(
                    text = option,
                    style = AppTextStyle.BodyMedium,
                )
            }
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
fun PreviewMultipleCheckboxes() {
    val options = listOf(
        "Estas son las diferentes opciones",
        "Puedes seleccionar multiples cosas",
        "La lista puede contener n opciones",
        "Puedes seleccionar cualquier parte de la lista",
    )
    val selected = remember {
        mutableStateListOf(
            "Puedes seleccionar multiples cosas",
            "Puedes seleccionar cualquier parte de la lista",
        )
    }

    Column(modifier = Modifier.padding(16.dp)) {
        MultipleCheckboxes(
            options = options,
            selectedOptions = selected.toSet(),
            onOptionToggle = { option ->
                if (option in selected) {
                    selected.remove(option)
                } else {
                    selected.add(option)
                }
            },
        )
    }
}
