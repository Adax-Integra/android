package com.example.adaxintegra.presentation.views.screens.cases

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.adaxintegra.presentation.model.CaseStatusUi
import com.example.adaxintegra.presentation.util.DateFormatter
import com.example.adaxintegra.presentation.viewmodel.CaseDetailViewModel
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppButton
import com.example.adaxintegra.presentation.views.designsystem.atoms.ButtonVariant
import com.example.adaxintegra.presentation.views.designsystem.organisms.CaseDescriptionCard
import com.example.adaxintegra.presentation.views.designsystem.organisms.CaseDetailCard
import com.example.adaxintegra.presentation.views.designsystem.organisms.CaseHeaderCard
import com.example.adaxintegra.presentation.views.designsystem.organisms.CaseHelpCard
import com.example.adaxintegra.presentation.views.designsystem.templates.ScreenTemplate
import com.example.adaxintegra.ui.theme.IconGrey

@Suppress("ktlint:standard:function-naming")
@Composable
fun CaseDetailScreen(
    caseId: String,
    onBack: () -> Unit,
    onEdit: () -> Unit = {},
    onCloseCase: () -> Unit = {},
    viewModel: CaseDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showCloseDialog by remember { mutableStateOf(false) }

    LaunchedEffect(caseId) {
        viewModel.loadCase(caseId)
    }

    val case = uiState.case
    val status = CaseStatusUi.from(case?.state?.lowercase())
    val statusText = status?.displayText ?: case?.state ?: "Sin estado"
    val statusColor = status?.color ?: IconGrey

    ScreenTemplate(
        title = "Detalle del caso",
        subtitle = case?.caseNumber ?: caseId,
        onBack = onBack,
        isLoading = uiState.isLoading,
        error = uiState.error,
    ) {
        if (case != null) {
            LazyColumn(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                item {
                    CaseHeaderCard(
                        caseNumber = case.caseNumber ?: case.caseId,
                        statusText = statusText,
                        statusColor = statusColor,
                        createdAt = DateFormatter.dateTime(case.createdAt),
                        updatedAt = DateFormatter.dateTime(case.updatedAt),
                    )
                }

                item {
                    CaseDetailCard(
                        violenceType =
                            case.violenceList
                                .orEmpty()
                                .joinToString(", ") { it.type }
                                .ifBlank { "Sin tipo registrado" },
                        hasLawyer = case.hasLawyer ?: false,
                    )
                }

                if (!case.description.isNullOrBlank()) {
                    item {
                        CaseDescriptionCard(
                            description = case.description,
                        )
                    }
                }

                if (!case.helpWanted.isNullOrBlank()) {
                    item {
                        CaseHelpCard(
                            helpWanted = case.helpWanted,
                        )
                    }
                }

                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        AppButton(
                            text = "Editar",
                            onClick = onEdit,
                            modifier = Modifier.fillMaxWidth(),
                            variant = ButtonVariant.Outlined,
                        )

                        AppButton(
                            text = "Cerrar caso",
                            onClick = { showCloseDialog = true },
                            modifier = Modifier.fillMaxWidth(),
                        )

                        AppButton(
                            text = "Cancelar",
                            onClick = onBack,
                            modifier = Modifier.fillMaxWidth(),
                            variant = ButtonVariant.Outlined,
                        )
                    }
                }
            }
        }
    }

    if (showCloseDialog) {
        AlertDialog(
            onDismissRequest = {
                showCloseDialog = false
            },
            title = {
                Text("¿Deseas cerrar este caso?")
            },
            text = {
                Text("El caso dejará de mostrarse como abierto.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showCloseDialog = false
                        onCloseCase()
                    },
                ) {
                    Text("Sí, cerrar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showCloseDialog = false
                    },
                ) {
                    Text("Cancelar")
                }
            },
        )
    }
}
