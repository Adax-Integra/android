package com.example.adaxintegra.presentation.views.screens.admin

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.adaxintegra.presentation.model.DocumentPreviewState
import com.example.adaxintegra.presentation.viewmodel.RecordDocumentsViewModel
import com.example.adaxintegra.presentation.viewmodel.VerificationReviewViewModel
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppButton
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.ButtonVariant
import com.example.adaxintegra.presentation.views.designsystem.atoms.SectionTitle
import com.example.adaxintegra.presentation.views.designsystem.atoms.Spacing
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.presentation.views.designsystem.molecules.DocumentPreviewCard
import com.example.adaxintegra.presentation.views.designsystem.molecules.FormActions
import com.example.adaxintegra.presentation.views.designsystem.molecules.InfoBanner
import com.example.adaxintegra.presentation.views.designsystem.molecules.LabeledTextArea
import com.example.adaxintegra.presentation.views.designsystem.templates.ScreenTemplate
import com.example.adaxintegra.presentation.views.screens.records.RecordDocumentsUiState
import com.example.adaxintegra.ui.theme.AdaxIntegraTheme
import com.example.adaxintegra.ui.theme.Purple

// B-02: staff reviews the documents of the external user and approves or rejects
// the account. onDecision receives the status the backend saved
@Suppress("ktlint:standard:function-naming")
@Composable
fun VerificationReviewScreen(
    userId: String,
    onBack: () -> Unit,
    onDecision: (String) -> Unit,
    viewModel: VerificationReviewViewModel = hiltViewModel(),
    documentsViewModel: RecordDocumentsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val documents by documentsViewModel.uiState.collectAsStateWithLifecycle()

    // Same previews as the documents of the record (V-13), nothing is saved on the phone
    LaunchedEffect(userId) {
        documentsViewModel.loadDocuments(userId)
    }

    LaunchedEffect(uiState.decidedStatus) {
        uiState.decidedStatus?.let { onDecision(it.value) }
    }

    // The decision can not be left halfway
    BackHandler(enabled = uiState.isSubmitting) {}

    Box(modifier = Modifier.fillMaxSize()) {
        VerificationReviewContent(
            uiState = uiState,
            documents = documents,
            onBack = onBack,
            onRefresh = documentsViewModel::refresh,
            onApprove = { viewModel.approve(userId) },
            onStartRejecting = viewModel::startRejecting,
            onReasonChange = viewModel::onReasonChange,
            onConfirmReject = { viewModel.reject(userId) },
            onCancelReject = viewModel::cancelRejecting,
        )

        // The backdrop absorbs the taps while the decision is sent
        if (uiState.isSubmitting) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.15f))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {},
                    ),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(color = Purple)
            }
        }
    }

    uiState.errorMessage?.let { message ->
        AlertDialog(
            onDismissRequest = viewModel::onErrorDismissed,
            title = {
                Text(
                    text = "Aviso",
                    style = AppTextStyle.TitleMedium,
                    fontWeight = FontWeight.Bold,
                )
            },
            text = { Text(text = message) },
            confirmButton = {
                TextButton(onClick = viewModel::onErrorDismissed) {
                    Text(
                        text = "Aceptar",
                        color = Purple,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            },
        )
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
private fun VerificationReviewContent(
    uiState: VerificationReviewUiState,
    documents: RecordDocumentsUiState,
    onBack: () -> Unit,
    onRefresh: () -> Unit,
    onApprove: () -> Unit,
    onStartRejecting: () -> Unit,
    onReasonChange: (String) -> Unit,
    onConfirmReject: () -> Unit,
    onCancelReject: () -> Unit,
) {
    val spacing = Spacing()

    // Signed URLs expire, pulling down asks the backend for new ones
    ScreenTemplate(
        title = "Revisar verificación",
        subtitle = "Gestión de usuarios",
        onBack = onBack,
        isLoading = documents.isLoading,
        error = documents.error,
        isRefreshing = documents.isRefreshing,
        onRefresh = onRefresh,
        modifier = Modifier.padding(horizontal = spacing.medium),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(vertical = spacing.small),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            InfoBanner(
                text = "Revisa los comprobantes de la externa y aprueba o rechaza su verificación. " +
                    "La decisión queda en la bitácora y se le avisa por correo.",
            )

            SectionTitle(text = "Documentos")
            DocumentPreviewCard(
                title = "Comprobante de identidad",
                state = documents.identityDocument,
                onRetry = onRefresh,
            )
            DocumentPreviewCard(
                title = "Comprobante de domicilio",
                state = documents.proofOfAddress,
                onRetry = onRefresh,
            )

            if (uiState.isRejecting) {
                LabeledTextArea(
                    label = "Motivo del rechazo (opcional)",
                    value = uiState.rejectReason,
                    onValueChange = onReasonChange,
                    maxLength = MAX_REASON_LENGTH,
                    placeholder = "Describe por qué se rechazan los documentos...",
                )
                FormActions(
                    onPrimary = onConfirmReject,
                    onSecondary = onCancelReject,
                    primaryTitle = "Confirmar rechazo",
                    isPrimaryEnabled = !uiState.isSubmitting,
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    AppButton(
                        text = "Aprobar verificación",
                        onClick = onApprove,
                        enabled = !uiState.isSubmitting,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    AppButton(
                        text = "Rechazar",
                        onClick = onStartRejecting,
                        variant = ButtonVariant.Outlined,
                        enabled = !uiState.isSubmitting,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
private fun VerificationReviewContentPreview() {
    AdaxIntegraTheme {
        VerificationReviewContent(
            uiState = VerificationReviewUiState(),
            documents = RecordDocumentsUiState(
                identityDocument = DocumentPreviewState.Loading,
                proofOfAddress = DocumentPreviewState.Empty,
            ),
            onBack = {},
            onRefresh = {},
            onApprove = {},
            onStartRejecting = {},
            onReasonChange = {},
            onConfirmReject = {},
            onCancelReject = {},
        )
    }
}
