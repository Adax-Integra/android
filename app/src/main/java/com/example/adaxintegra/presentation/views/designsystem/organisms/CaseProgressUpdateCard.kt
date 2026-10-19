package com.example.adaxintegra.presentation.views.designsystem.organisms

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.domain.model.CaseProgressStep
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppButton

@Suppress("ktlint:standard:function-naming")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaseProgressUpdateCard(
    caseSteps: List<CaseProgressStep>,
    isUpdating: Boolean,
    updateSuccess: Boolean,
    updateError: String?,
    isCaseClosed: Boolean,
    onUpdateStep: (Int) -> Unit,
) {
    val sortedSteps = remember(caseSteps) {
        caseSteps.sortedBy { it.stepNumber }
    }

    val currentStep = remember(caseSteps) {
        caseSteps
            .filter { it.status.equals("Completado", ignoreCase = true) }
            .maxOfOrNull { it.stepNumber }
            ?: caseSteps.minOfOrNull { it.stepNumber }
            ?: 1
    }

    var expandedCard by remember { mutableStateOf(false) }
    var expandedMenu by remember { mutableStateOf(false) }
    var selectedStep by remember(caseSteps) {
        mutableIntStateOf(currentStep)
    }

    val selectedStatus = sortedSteps
        .firstOrNull { it.stepNumber == selectedStep }
        ?.status
        ?: "Pendiente"

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        expandedCard = !expandedCard
                        expandedMenu = false
                    },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Actualizar progreso del caso",
                    style = AppTextStyle.TitleMedium,
                    fontWeight = FontWeight.Bold,
                )

                Icon(
                    imageVector = if (expandedCard) {
                        Icons.Default.ExpandLess
                    } else {
                        Icons.Default.ExpandMore
                    },
                    contentDescription = if (expandedCard) {
                        "Contraer"
                    } else {
                        "Expandir"
                    },
                )
            }

            if (expandedCard) {
                if (sortedSteps.isEmpty()) {
                    Text("No hay pasos disponibles para este caso.")
                } else {
                    ExposedDropdownMenuBox(
                        expanded = expandedMenu,
                        onExpandedChange = {
                            if (!isUpdating && !isCaseClosed) {
                                expandedMenu = it
                            }
                        },
                    ) {
                        OutlinedTextField(
                            value = "Paso $selectedStep - $selectedStatus",
                            onValueChange = {},
                            readOnly = true,
                            label = { androidx.compose.material3.Text("Seleccionar paso") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(
                                    expanded = expandedMenu,
                                )
                            },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                            enabled = !isUpdating && !isCaseClosed,
                        )

                        ExposedDropdownMenu(
                            expanded = expandedMenu,
                            onDismissRequest = { expandedMenu = false },
                        ) {
                            sortedSteps.forEach { step ->
                                DropdownMenuItem(
                                    text = {
                                        androidx.compose.material3.Text(
                                            "Paso ${step.stepNumber}",
                                        )
                                    },
                                    onClick = {
                                        selectedStep = step.stepNumber
                                        expandedMenu = false
                                    },
                                )
                            }
                        }
                    }

                    if (isUpdating) {
                        CircularProgressIndicator()
                    }

                    if (updateSuccess) {
                        Text(
                            text = "Progreso del caso actualizado correctamente.",
                            color = Color(0xFF2E7D32),
                        )
                    }

                    if (updateError != null) {
                        Text(
                            text = updateError,
                            color = MaterialTheme.colorScheme.error,
                        )
                    }

                    AppButton(
                        text = "Guardar progreso",
                        onClick = { onUpdateStep(selectedStep) },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isUpdating &&
                            !isCaseClosed &&
                            sortedSteps.isNotEmpty(),
                    )
                }
            }
        }
    }
}
