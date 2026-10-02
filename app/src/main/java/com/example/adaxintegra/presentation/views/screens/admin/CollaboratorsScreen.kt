package com.example.adaxintegra.presentation.views.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.adaxintegra.presentation.viewmodel.AddCollaboratorViewModel
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppButton
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.presentation.views.designsystem.organisms.AddCollaboratorDialog
import com.example.adaxintegra.presentation.views.designsystem.organisms.AppHeader
import com.example.adaxintegra.ui.theme.BackgroundGrey
import com.example.adaxintegra.ui.theme.Purple

// G-03: admin's collaborator management screen
// TODO: add the collaborators list here (depends on "Vista de administradora")
@Suppress("ktlint:standard:function-naming")
@Composable
fun CollaboratorsScreen(
    onBack: () -> Unit,
    viewModel : AddCollaboratorViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGrey)
            .padding(horizontal = 16.dp),
    ) {
        AppHeader(
            title = "Gestión de Colaboradoras",
            subtitle = "Administra las cuentas de colaboradoras",
            onBack = onBack,
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Acceptance criteria: the option to add a new collaborator appears
        AppButton(
            text = "Agregar colaboradora",
            onClick = viewModel:: openDialog,
            modifier = Modifier.fillMaxWidth(),
        )

        val successMessage = uiState.successMessage
        if (successMessage != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = successMessage,
                style = AppTextStyle.BodyMedium,
                fontWeight = FontWeight.Medium,
                color = Purple,
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
}
