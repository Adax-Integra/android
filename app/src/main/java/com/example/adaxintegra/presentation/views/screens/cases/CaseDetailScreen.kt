package com.example.adaxintegra.presentation.views.screens.cases

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
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
import com.example.adaxintegra.presentation.views.designsystem.organisms.CaseEvidenceCard
import com.example.adaxintegra.presentation.views.designsystem.organisms.CaseHeaderCard
import com.example.adaxintegra.presentation.views.designsystem.organisms.CaseHelpCard
import com.example.adaxintegra.presentation.views.designsystem.organisms.CaseRecordSummaryCard
import com.example.adaxintegra.presentation.views.designsystem.templates.ScreenTemplate
import com.example.adaxintegra.ui.theme.AdaxIntegraTheme
import com.example.adaxintegra.ui.theme.IconGrey

// connects the V-11 case detail UI with the case information loaded by its ViewModel
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

    // reloads the case information whenever the selected case changes
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
            CaseDetailContent(
                caseNumber = case.caseNumber ?: case.caseId,
                statusText = statusText,
                statusColor = statusColor,
                createdAt = DateFormatter.dateTime(case.createdAt),
                updatedAt = DateFormatter.dateTime(case.updatedAt),
                violenceType =
                    case.violenceList
                        .orEmpty()
                        .joinToString(", ") { it.type }
                        .ifBlank { "Sin tipo registrado" },
                location = null,
                hasLawyer = case.hasLawyer ?: false,
                description = case.description,

                // evidence remains hidden until its backend source is integrated
                showEvidenceSection = false,

                helpWanted = case.helpWanted,
                userName = null,
                recordId = null,
                onBack = onBack,
                onEdit = onEdit,
                onCloseCase = onCloseCase,
            )
        }
    }
}

// renders the V-11 content independently from the ViewModel
// this keeps presentation separate from data-loading responsibilities
@Suppress("ktlint:standard:function-naming")
@Composable
private fun CaseDetailContent(
    caseNumber: String,
    statusText: String,
    statusColor: Color,
    createdAt: String,
    updatedAt: String,
    violenceType: String,
    location: String?,
    hasLawyer: Boolean,
    description: String?,
    showEvidenceSection: Boolean,
    helpWanted: String?,
    userName: String?,
    recordId: String?,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onCloseCase: () -> Unit,
) {
    var showCloseDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        // displays the selected case number and its current status
        item {
            CaseHeaderCard(
                caseNumber = caseNumber,
                statusText = statusText,
                statusColor = statusColor,
            )
        }

        // record information is displayed only when both values are available
        if (!userName.isNullOrBlank() && !recordId.isNullOrBlank()) {
            item {
                CaseRecordSummaryCard(
                    userName = userName,
                    recordId = recordId,
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                    onEdit = onEdit,
                )
            }
        }

        // displays the main information associated with the selected case
        item {
            CaseDetailCard(
                violenceType = violenceType,
                location = location,
                hasLawyer = hasLawyer,
            )
        }

        // hides the description when the backend does not provide one
        if (!description.isNullOrBlank()) {
            item {
                CaseDescriptionCard(
                    description = description,
                )
            }
        }

        // evidence is prepared for V-11 but shown only when its data source is available
        if (showEvidenceSection) {
            item {
                CaseEvidenceCard()
            }
        }

        // displays requested help only when information is available
        if (!helpWanted.isNullOrBlank()) {
            item {
                CaseHelpCard(
                    helpWanted = helpWanted,
                )
            }
        }

        // contains the actions defined for the V-11 case detail view
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                AppButton(
                    text = "Cerrar caso",
                    onClick = {
                        showCloseDialog = true
                    },
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

    // asks for confirmation before requesting the case closure
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

// mock values are used only to validate the V-11 layout without depending on the backend
@Suppress("ktlint:standard:function-naming")
@Preview(
    name = "V-11 Case Detail",
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=411dp,height=891dp,dpi=420",
)
@Composable
private fun CaseDetailScreenPreview() {
    AdaxIntegraTheme {
        ScreenTemplate(
            title = "Detalle del caso",
            subtitle = "Caso 2026-0847-C2",
            onBack = {},
        ) {
            CaseDetailContent(
                caseNumber = "Caso 2026-0847-C2",
                statusText = "Abierto",
                statusColor = Color(0xFF34C759),
                createdAt = "12/09/2026",
                updatedAt = "30/09/2026",
                violenceType = "Violencia psicológica",
                location = "Querétaro",
                hasLawyer = true,
                description =
                    "La usuaria reporta una situación de violencia y requiere seguimiento del caso.",

                // preview-only flag used to compare the V-11 layout with Figma
                showEvidenceSection = true,

                helpWanted =
                    "Asesoría legal y acompañamiento psicológico",
                userName = "María García López",
                recordId = "EXP-2024-1024",
                onBack = {},
                onEdit = {},
                onCloseCase = {},
            )
        }
    }
}
