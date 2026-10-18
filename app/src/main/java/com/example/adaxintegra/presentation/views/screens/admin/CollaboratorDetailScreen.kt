package com.example.adaxintegra.presentation.views.screens.admin

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.adaxintegra.presentation.viewmodel.CollaboratorDetailViewModel
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppButton
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.ButtonVariant
import com.example.adaxintegra.presentation.views.designsystem.atoms.Spacing
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.presentation.views.designsystem.organisms.CollaboratorDetailContent
import com.example.adaxintegra.presentation.views.designsystem.organisms.EditCollaboratorDialog
import com.example.adaxintegra.presentation.views.designsystem.templates.ScreenTemplate
import com.example.adaxintegra.ui.theme.Purple

// G-10: detail of a collaborator, opened from the G-06 list (admin only).
// Loads the data and decides what to show, the content draws it
// G-04: asks for confirmation before activating or deactivating the account
@Suppress("ktlint:standard:function-naming")
@Composable
fun CollaboratorDetailScreen(
    userId: String,
    onBack: () -> Unit,
    viewModel: CollaboratorDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val spacing = Spacing()
    val context = LocalContext.current

    // Same clipboard manager used in OtpCodeInput
    @Suppress("DEPRECATION")
    val clipboardManager = LocalClipboardManager.current

    val collaborator = uiState.collaborator
    val errorMessage = uiState.error

    // Reloads the collaborator whenever the selected one changes
    LaunchedEffect(userId) {
        viewModel.loadCollaborator(userId)
    }

    // G-18: show success only after the backend confirms the update
    LaunchedEffect(uiState.profileUpdated) {
        if (uiState.profileUpdated) {
            Toast.makeText(
                context,
                "Datos actualizados correctamente",
                Toast.LENGTH_SHORT,
            ).show()
            viewModel.clearProfileUpdated()
        }
    }

    // Copies the text and confirms it with a short message
    val copyText: (String) -> Unit = { text ->
        clipboardManager.setText(AnnotatedString(text))
        Toast.makeText(context, "Copiado", Toast.LENGTH_SHORT).show()
    }

    ScreenTemplate(
        title = "Detalle de colaboradora",
        onBack = onBack,
        modifier = Modifier.padding(horizontal = spacing.medium),
    ) {
        when {
            uiState.isLoading && collaborator == null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(color = Purple)
                }
            }

            errorMessage != null && collaborator == null -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(spacing.medium, Alignment.CenterVertically),
                ) {
                    Text(
                        text = errorMessage,
                        style = AppTextStyle.BodyMedium,
                        textAlign = TextAlign.Center,
                    )
                    AppButton(text = "Reintentar", onClick = viewModel::retry)
                }
            }

            collaborator != null -> {
                CollaboratorDetailContent(
                    collaborator = collaborator,
                    onCopy = copyText,
                    onChangeStatusClick = viewModel::openStatusDialog,
                    onEditClick = viewModel::openEditDialog,
                )
            }
        }
    }

    // G-18: reuse the existing G-10 screen and the new edit dialog
    if (uiState.showEditDialog && collaborator != null) {
        EditCollaboratorDialog(
            collaborator = collaborator,
            isSaving = uiState.isUpdatingProfile,
            error = uiState.profileError,
            onSave = viewModel::saveProfile,
            onCancel = viewModel::dismissEditDialog,
        )
    }

    // G-04: confirmation before changing the status, the text depends on the current one
    if (uiState.showStatusDialog && collaborator != null) {
        val fullName = "${collaborator.name} ${collaborator.lastName}".trim()
        val isActive = collaborator.isActive

        val title = if (isActive) "Desactivar cuenta" else "Reactivar cuenta"
        val message = if (isActive) {
            "¿Seguro que quieres desactivar la cuenta de $fullName? " +
                "Ya no podrá iniciar sesión ni recibir nuevas asignaciones."
        } else {
            "¿Quieres reactivar la cuenta de $fullName? " +
                "Podrá volver a iniciar sesión y recibir nuevas asignaciones."
        }
        val confirmText = if (isActive) "Desactivar" else "Reactivar"

        AlertDialog(
            onDismissRequest = viewModel::dismissStatusDialog,
            title = {
                Text(
                    text = title,
                    style = AppTextStyle.TitleMedium,
                    fontWeight = FontWeight.Bold,
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(spacing.small)) {
                    Text(text = message)

                    // Error from the request, the admin can try again
                    val statusError = uiState.statusError
                    if (statusError != null) {
                        Text(
                            text = statusError,
                            style = AppTextStyle.BodySmall,
                            color = MaterialTheme.colorScheme.error,
                        )
                    }
                }
            },
            confirmButton = {
                AppButton(
                    text = confirmText,
                    onClick = viewModel::updateStatus,
                    isLoading = uiState.isUpdatingStatus,
                )
            },
            dismissButton = {
                AppButton(
                    text = "Cancelar",
                    onClick = viewModel::dismissStatusDialog,
                    variant = ButtonVariant.Outlined,
                    enabled = !uiState.isUpdatingStatus,
                )
            },
        )
    }
}
