package com.example.adaxintegra.presentation.views.designsystem.organisms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    editButtonText: String = "Editar",
    saveButtonText: String = "Guardar",
    saveButtonAction: () -> Unit,
    cancelButtonText: String = "Cancelar",
    cancelButtonAction: () -> Unit,
) {
    var isEditing by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
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
                    enabled = isEditing,
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.align(Alignment.End),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    if (isEditing) {
                        AppButton(
                            text = cancelButtonText,
                            onClick = {
                                isEditing = false
                                cancelButtonAction()
                            },
                            variant = ButtonVariant.Outlined,
                        )

                        AppButton(
                            text = saveButtonText,
                            onClick = {
                                isEditing = false
                                saveButtonAction()
                            },
                            variant = ButtonVariant.Primary,
                        )
                    } else {
                        AppButton(
                            text = editButtonText,
                            onClick = { isEditing = true },
                            variant = ButtonVariant.Outlined,
                        )
                    }
                }
            }
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
