package com.example.adaxintegra.presentation.views.screens.cases

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.adaxintegra.domain.entities.Case
import com.example.adaxintegra.domain.entities.ExpedientStatus
import com.example.adaxintegra.presentation.viewmodel.CasesFromUserUiState
import com.example.adaxintegra.presentation.viewmodel.CasesFromUserViewModel
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppButton
import com.example.adaxintegra.presentation.views.designsystem.molecules.CaseCard
import com.example.adaxintegra.presentation.views.designsystem.molecules.ExpedientStatusCard
import com.example.adaxintegra.presentation.views.designsystem.organisms.ChangeStatusBottomSheet
import com.example.adaxintegra.presentation.views.designsystem.templates.ScreenTemplate

@Suppress("ktlint:standard:function-naming")
@Composable
fun RecordFromUser(
    userId: String,
    viewModel: CasesFromUserViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onCaseClick: (String) -> Unit = {},
    onCreateCaseClick: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(userId) {
        viewModel.loadCasesFromUser(userId)
    }

    RecordFromUserContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onCaseClick = onCaseClick,
        onCreateCaseClick = onCreateCaseClick,
        onStatusChange = { newStatus ->
            viewModel.updateStatus(recordId = userId, newStatus = newStatus)
        }
    )
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun RecordFromUserContent(
    uiState: CasesFromUserUiState,
    onBackClick: () -> Unit = {},
    onCaseClick: (String) -> Unit = {},
    onCreateCaseClick: () -> Unit = {},
    onStatusChange: (ExpedientStatus) -> Unit = {}
) {
    // Right now the name doesn't get through correctly because it is a hard coded UserID
    // When implementing V-05 it should pass the name correctly
    val userName = uiState.cases.firstOrNull()?.name ?: "Usuario"
    var currentStatus by remember { mutableStateOf(ExpedientStatus.FOLLOWING) }
    var showBottomSheet by remember { mutableStateOf(false) }

    ScreenTemplate(
        title = "Casos de $userName",
        error = uiState.error,
        isLoading = uiState.isLoading,
        onBack = onBackClick,
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            item {
                ExpedientStatusCard(
                    currentStatus = currentStatus,
                    onEditClick = { showBottomSheet = true }
                )
            }

            items(
                items = uiState.cases,
                key = { it.caseId },
            ) { case ->
                CaseCard(
                    case = case,
                    modifier = Modifier.clickable { onCaseClick(case.caseId) },
                )
                AppButton(
                    text = "Registrar nuevo caso",
                    onClick = onCreateCaseClick,
                    modifier = Modifier.fillMaxWidth(),
                )

                if (uiState.cases.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "Este usuario no tiene ningún caso",
                            textAlign = TextAlign.Center,
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                    ) {
                        items(
                            items = uiState.cases,
                            key = { it.caseId },
                        ) { case ->
                            CaseCard(
                                case = case,
                                modifier = Modifier.clickable { onCaseClick(case.caseId) },
                            )
                        }
                    }
                }
            }}
            if (showBottomSheet) {
                ChangeStatusBottomSheet(
                    userName = userName,
                    currentStatus = currentStatus,
                    onDismiss = { showBottomSheet = false },
                    onSaveStatus = { selectedStatus ->
                        currentStatus = selectedStatus
                        onStatusChange(selectedStatus)
                        showBottomSheet = false
                    }
                )
            }
        }
    }


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExpedientStatusCard(
    currentStatus: String,
    onSaveStatus: (String) -> Unit,
) {
    val statusOptions = listOf("Abierto", "En proceso", "Resuelto", "SIN EVALUAR")
    var expanded by remember { mutableStateOf(false) }
    var selectedStatus by remember { mutableStateOf(currentStatus) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Text(
                text = "Estatus del expediente",
                style = MaterialTheme.typography.titleMedium,
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Dropdown de estatus
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
            ) {
                OutlinedTextField(
                    value = selectedStatus,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Estatus") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    statusOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                selectedStatus = option
                                expanded = false
                            },
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Botones de Cancelar y Guardar (Criterios de Aceptación)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                OutlinedButton(
                    onClick = {
                        // TODO 2: Al cancelar, restauramos la selección al estatus original
                        selectedStatus = currentStatus
                    },
                    modifier = Modifier.weight(1f),
                ) {
                    Text("Cancelar")
                }

                Button(
                    onClick = {
                        // TODO 3: Al guardar, emitimos el estatus seleccionado
                        onSaveStatus(selectedStatus)
                    },
                    modifier = Modifier.weight(1f),
                ) {
                    Text("Guardar")
                }
            }
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
private fun RecordFromUserPreview() {
    val mockCases = Case.getMockData()
    RecordFromUserContent(
        uiState = CasesFromUserUiState(
            cases = mockCases,
        ),
    )
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
private fun RecordFromUserEmptyPreview() {
    RecordFromUserContent(
        uiState = CasesFromUserUiState(
            cases = emptyList(),
        ),
    )
}

