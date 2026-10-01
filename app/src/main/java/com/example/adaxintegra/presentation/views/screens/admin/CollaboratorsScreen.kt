package com.example.adaxintegra.presentation.views.screens.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.adaxintegra.presentation.viewmodel.AddCollaboratorViewModel
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppButton
import com.example.adaxintegra.presentation.views.designsystem.atoms.Spacing
import com.example.adaxintegra.presentation.views.designsystem.organisms.AddCollaboratorDialog
import com.example.adaxintegra.presentation.views.designsystem.organisms.SuccessDialog
import com.example.adaxintegra.presentation.views.designsystem.templates.ScreenTemplate

// G-03: admin's collaborator management screen, opened from the profile
// TODO: add the collaborators list here (depends on "Vista de administradora")
@Suppress("ktlint:standard:function-naming")
@Composable
fun CollaboratorsScreen(
    onBack: () -> Unit,
    viewModel: AddCollaboratorViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val spacing = Spacing()

    // Same template as the other screens, so it follows the light/dark theme
    ScreenTemplate(
        title = "Gestión de Colaboradoras",
        subtitle = "Administra las cuentas de colaboradoras",
        onBack = onBack,
        modifier = Modifier.padding(horizontal = spacing.medium),
    ) {
        Column(
            modifier = Modifier.padding(top = spacing.small),
            verticalArrangement = Arrangement.spacedBy(spacing.medium),
        ) {
            // Acceptance criteria: the option to add a new collaborator appears
            AppButton(
                text = "Agregar colaboradora",
                onClick = viewModel::openDialog,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }

    // Acceptance criteria: clicking the option opens the form
    if (uiState.isDialogOpen) {
        AddCollaboratorDialog(
            name = uiState.name,
            lastName = uiState.lastName,
            email = uiState.email,
            password = uiState.password,
            phone = uiState.phone,
            fieldErrors = uiState.fieldErrors,
            generalError = uiState.generalError,
            canSave = uiState.canSave,
            isSaving = uiState.isSaving,
            onNameChange = viewModel::onNameChange,
            onLastNameChange = viewModel::onLastNameChange,
            onEmailChange = viewModel::onEmailChange,
            onPasswordChange = viewModel::onPasswordChange,
            onPhoneChange = viewModel::onPhoneChange,
            onSave = viewModel::save,
            onCancel = viewModel::cancel,
        )
    }

    // Confirmation after the account is created
    val successMessage = uiState.successMessage
    if (successMessage != null) {
        SuccessDialog(
            title = "Registro completo",
            message = successMessage,
            onConfirm = viewModel::dismissSuccessMessage,
        )
    }
}
