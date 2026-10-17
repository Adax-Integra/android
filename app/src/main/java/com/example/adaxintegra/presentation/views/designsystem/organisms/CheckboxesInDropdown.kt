package com.example.adaxintegra.presentation.views.designsystem.organisms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppButton
import com.example.adaxintegra.presentation.views.designsystem.atoms.ButtonVariant
import com.example.adaxintegra.presentation.views.designsystem.molecules.MultipleCheckboxes

@Suppress("ktlint:standard:function-naming")
@Composable
fun CheckboxesInDropdown(
    title: String,
    options: List<String>,
    selectedOptions: Set<String>,
    onOptionToggle: (String) -> Unit,
    modifier: Modifier = Modifier,
    initialExpanded: Boolean = false,
    saveButtonText: String = "Guardar",
    saveButtonAction: () -> Unit,
    cancelButtonText: String = "Cancelar",
    cancelButtonAction: () -> Unit,
) {
    InlineDropdown(
        title = title,
        modifier = modifier,
        initialExpanded = initialExpanded,
    ) {
        MultipleCheckboxes(
            options = options,
            selectedOptions = selectedOptions,
            onOptionToggle = onOptionToggle,
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.align(Alignment.End),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            AppButton(
                text = cancelButtonText,
                onClick = cancelButtonAction,
                variant = ButtonVariant.Outlined,
            )

            Spacer(modifier = Modifier.height(12.dp))


            AppButton(
                text = saveButtonText,
                onClick = saveButtonAction,
                variant = ButtonVariant.Primary,
            )
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
fun PreviewCheckboxesInDropdown() {
    val options = listOf(
        "Primera opción",
        "Segunda opción",
        "Opción 3",
        "Esta también es una opción",
        "Así se verían multiples opciones de diferente tamaño",
    )
    val selected = remember {
        mutableStateListOf("Primera opción", "Opción 3")
    }

    Column(modifier = Modifier.padding(16.dp)) {
        CheckboxesInDropdown(
            title = "Dropdown de checkboxes",
            options = options,
            selectedOptions = selected.toSet(),
            onOptionToggle = { option ->
                if (option in selected) {
                    selected.remove(option)
                } else {
                    selected.add(option)
                }
            },
            saveButtonAction = {},
            cancelButtonAction = {},
        )
    }
}
