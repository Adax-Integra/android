package com.example.adaxintegra.presentation.views.screens.cases

import androidx.compose.foundation.layout.Column  import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.adaxintegra.presentation.viewmodel.CreateCaseViewModel
import com.example.adaxintegra.presentation.views.designsystem.atoms.Spacing
import com.example.adaxintegra.presentation.views.designsystem.organisms.CreateCaseForm
import com.example.adaxintegra.presentation.views.designsystem.organisms.SuccessDialog
import com.example.adaxintegra.presentation.views.designsystem.templates.ScreenTemplate

// R-02: "Registrar el caso" screen, reached from the external user's case list
@Suppress("ktlint:standard:function-naming")
@Composable
fun CreateCaseScreen(
    onBack: () -> Unit,
    onCaseCreated: () -> Unit,
    viewModel: CreateCaseViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val spacing = Spacing()

    // isLoading is not given to the template: while saving, the form stays on
    // screen and the progress is shown inside "Guardar"
    ScreenTemplate(
        title = "Registrar el caso",
        subtitle = "Cuéntanos tu situación para poder acompañarte",
        onBack = onBack,
        modifier = Modifier.padding(horizontal = spacing.medium),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(top = spacing.small, bottom = spacing.large),
        ) {
            CreateCaseForm(
                writtenDescription = uiState.writtenDescription,
                writtenHelpsWanted = uiState.writtenHelpsWanted,
                hasExternalSupport = uiState.hasExternalSupport,
                fieldErrors = uiState.fieldErrors,
                generalError = uiState.generalError,
                canSave = uiState.canSave,
                isSaving = uiState.isSaving,
                onDescriptionChange = viewModel::onDescriptionChange,
                onHelpsWantedChange = viewModel::onHelpsWantedChange,
                onExternalSupportChange = viewModel::onExternalSupportChange,
                onSave = viewModel::save,
                onCancel = {
                    viewModel.cancel()
                    onBack()
                },
            )
        }
    }

    val successMessage = uiState.successMessage
    if (successMessage != null) {
        SuccessDialog(
            title = "Caso creado",
            message = successMessage,
            onConfirm = {
                viewModel.dismissSuccessMessage()
                onCaseCreated()
            },
        )
    }
}
