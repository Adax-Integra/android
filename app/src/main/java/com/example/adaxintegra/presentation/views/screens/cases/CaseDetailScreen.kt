package com.example.adaxintegra.presentation.views.screens.cases

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.adaxintegra.presentation.views.designsystem.organisms.CaseCloseDialog
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
    var showCloseDialog by remember { mutableStateOf(false) }

    // reloads the selected case whenever its identifier changes
    LaunchedEffect(caseId) {
        viewModel.loadCase(caseId)
    }

    val case = uiState.case

    // normalizes the raw backend state before displaying it in V-11
    val normalizedState = case?.state?.trim()?.lowercase()
    val status = CaseStatusUi.from(normalizedState)

    // Open is handled locally to avoid modifying the shared CaseStatusUi model
    val statusText =
        when (normalizedState) {
            "open" -> "Abierto"
            "cerrado" -> "Cerrado"
            else -> status?.displayText ?: case?.state ?: "Sin estado"
        }

    val statusColor =
        when (normalizedState) {
            "open" -> Color(0xFF34C759)
            "cerrado" -> CaseStatusUi.CLOSED.color
            else -> status?.color ?: IconGrey
        }

    // controls V-11 actions according to the current backend state
    val isCaseClosed = normalizedState in setOf("closed", "cerrado")

    ScreenTemplate(
        title = "Detalle del caso",
        subtitle = "",
        onBack = onBack,
        isLoading = uiState.isLoading,
        error = uiState.error,
    ) {
        if (case != null) {
            CaseDetailContent(
                caseNumber =
                    case.caseNumber
                        ?.takeIf { it.isNotBlank() }
                        ?: "Sin número de caso",
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

                // evidence stays hidden until its backend source is available
                showEvidenceSection = false,

                helpWanted = case.helpWanted,

                // record information returned by the V-11 backend detail response
                userName = case.userName,
                recordId = case.recordId,

                isCaseClosed = isCaseClosed,
                onBack = onBack,
                onEdit = onEdit,
                onRequestClose = {
                    showCloseDialog = true
                },
            )
        }
    }

    // displays the V-11 confirmation interface before requesting the closure
    if (showCloseDialog) {
        CaseCloseDialog(
            onConfirm = {
                showCloseDialog = false

                // closes the case through the existing V-11 backend flow
                viewModel.closeCase(caseId)
                onCloseCase()
            },
            onDismiss = {
                showCloseDialog = false
            },
        )
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
    isCaseClosed: Boolean,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onRequestClose: () -> Unit,
) {
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

        // record information appears only when both backend values are available
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

        // hides the description when no value is returned
        if (!description.isNullOrBlank()) {
            item {
                CaseDescriptionCard(
                    description = description,
                )
            }
        }

        // evidence is prepared for V-11 but stays hidden until its source is integrated
        if (showEvidenceSection) {
            item {
                CaseEvidenceCard()
            }
        }

        // requested help appears only when information is available
        if (!helpWanted.isNullOrBlank()) {
            item {
                CaseHelpCard(
                    helpWanted = helpWanted,
                )
            }
        }

        // actions defined by the V-11 case detail acceptance criteria
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                // close action is available only while the selected case remains open
                if (!isCaseClosed) {
                    AppButton(
                        text = "Cerrar caso",
                        onClick = onRequestClose,
                        modifier = Modifier.weight(1f),
                    )
                }

                // cancel always returns to the previous screen
                AppButton(
                    text = "Cancelar",
                    onClick = onBack,
                    modifier = Modifier.weight(1f),
                    variant = ButtonVariant.Outlined,
                )
            }
        }
    }
}

// mock values are used only to validate the open-case V-11 layout
@Suppress("ktlint:standard:function-naming")
@Preview(
    name = "V-11 Open Case Detail",
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=411dp,height=891dp,dpi=420",
)
@Composable
private fun OpenCaseDetailScreenPreview() {
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

                // preview-only value used to validate the Figma evidence section
                showEvidenceSection = true,

                helpWanted =
                    "Asesoría legal y acompañamiento psicológico",
                userName = "María García López",
                recordId = "EXP-2024-1024",

                // open cases expose the close-case action
                isCaseClosed = false,

                onBack = {},
                onEdit = {},
                onRequestClose = {},
            )
        }
    }
}

// mock values are used to validate how V-11 responds to a closed case
@Suppress("ktlint:standard:function-naming")
@Preview(
    name = "V-11 Closed Case Detail",
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=411dp,height=891dp,dpi=420",
)
@Composable
private fun ClosedCaseDetailScreenPreview() {
    AdaxIntegraTheme {
        ScreenTemplate(
            title = "Detalle del caso",
            subtitle = "Caso 2026-0847-C2",
            onBack = {},
        ) {
            CaseDetailContent(
                caseNumber = "Caso 2026-0847-C2",
                statusText = "Cerrado",
                statusColor = Color(0xFF8E8E93),
                createdAt = "12/09/2026",
                updatedAt = "30/09/2026",
                violenceType = "Violencia psicológica",
                location = "Querétaro",
                hasLawyer = true,
                description =
                    "La usuaria reporta una situación de violencia y requiere seguimiento del caso.",
                showEvidenceSection = true,
                helpWanted =
                    "Asesoría legal y acompañamiento psicológico",
                userName = "María García López",
                recordId = "EXP-2024-1024",

                // closed cases must not expose the close-case action again
                isCaseClosed = true,

                onBack = {},
                onEdit = {},
                onRequestClose = {},
            )
        }
    }
}
