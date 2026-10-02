package com.example.adaxintegra.presentation.views.screens.cases

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppButton
import com.example.adaxintegra.presentation.views.designsystem.atoms.ButtonVariant
import com.example.adaxintegra.presentation.views.designsystem.organisms.CaseDescriptionCard
import com.example.adaxintegra.presentation.views.designsystem.organisms.CaseDetailCard
import com.example.adaxintegra.presentation.views.designsystem.organisms.CaseHeaderCard
import com.example.adaxintegra.presentation.views.designsystem.organisms.CaseHelpCard
import com.example.adaxintegra.presentation.views.designsystem.organisms.CaseRecordSummaryCard
import com.example.adaxintegra.presentation.views.designsystem.templates.ScreenTemplate

@Suppress("ktlint:standard:function-naming")
@Composable
fun CaseDetailScreen(
    caseNumber: String,
    statusText: String,
    statusColor: Color,
    userName: String,
    recordId: String,
    createdAt: String,
    updatedAt: String,
    violenceType: String,
    location: String,
    hasLawyer: Boolean,
    description: String,
    helpWanted: String,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onCloseCase: () -> Unit,
) {
    var showCloseDialog by remember { mutableStateOf(false) }

    ScreenTemplate(
        title = "Detalle del caso",
        subtitle = caseNumber,
        onBack = onBack,
    ) {
        LazyColumn(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                CaseHeaderCard(
                    caseNumber = caseNumber,
                    statusText = statusText,
                    statusColor = statusColor,
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                )
            }

            item {
                CaseRecordSummaryCard(
                    userName = userName,
                    recordId = recordId,
                )
            }

            item {
                CaseDetailCard(
                    violenceType = violenceType,
                    location = location,
                    hasLawyer = hasLawyer,
                )
            }

            item {
                CaseDescriptionCard(
                    description = description,
                )
            }

            item {
                CaseHelpCard(
                    helpWanted = helpWanted,
                )
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

    if (showCloseDialog) {
        AlertDialog(
            onDismissRequest = { showCloseDialog = false },
            title = {
                androidx.compose.material3.Text(
                    text = "¿Deseas cerrar este caso?",
                )
            },
            text = {
                androidx.compose.material3.Text(
                    text = "El caso dejará de mostrarse como abierto.",
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showCloseDialog = false
                        onCloseCase()
                    },
                ) {
                    androidx.compose.material3.Text("Sí, cerrar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showCloseDialog = false },
                ) {
                    androidx.compose.material3.Text("Cancelar")
                }
            },
        )
    }
}
