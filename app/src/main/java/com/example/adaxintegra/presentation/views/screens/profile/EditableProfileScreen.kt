package com.example.adaxintegra.presentation.views.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.adaxintegra.presentation.viewmodel.EditableProfileViewModel
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppButton
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppIcon
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppIcons
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.IconSize
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.presentation.views.designsystem.molecules.LabeledTextField
import com.example.adaxintegra.presentation.views.designsystem.templates.ScreenTemplate

@Composable
fun EditableProfileScreen (
    onBack: () -> Unit,
    viewModel: EditableProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ScreenTemplate(
        title = "Tu Perfil",
        onBack = {
            if (uiState.isEditing) {
                viewModel.requestDiscardConfirmation()
            } else {
                onBack()
            }
        },
        isLoading = uiState.isLoading,
        error = uiState.error.takeIf {
            !uiState.isEditing && !uiState.isSaving
        },
        isRefreshing = uiState.isRefreshing,
        onRefresh = viewModel::refreshEditableProfile,
        trailingAction = {
            IconButton(
                onClick = {
                    if (uiState.isEditing) {
                        viewModel.requestDiscardConfirmation()
                    } else {
                        viewModel.startEditing()
                    }
                },
                enabled = !uiState.isSaving
            ) {
                AppIcon(
                    imageVector = if (uiState.isEditing) {
                        AppIcons.Close
                    } else {
                        AppIcons.Edit
                    },
                    contentDescription = if (uiState.isEditing) {
                        "Cancelar edición"
                    } else {
                        "Editar perfil"
                    },
                    size = IconSize.LargeIcon
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {

                if (uiState.error != null && uiState.isEditing) {
                    Text(
                        text = uiState.error!!,
                        style = AppTextStyle.BodyMedium,
                        fontWeight = FontWeight.Normal,
                        color = Color.Red
                    )
                }

                if (uiState.successMessage != null) {
                    Text(
                        text = uiState.successMessage!!,
                        style = AppTextStyle.BodyMedium,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF2E7D32)
                    )
                }

                LabeledTextField(
                    label = "Nombre",
                    value = uiState.name,
                    onValueChange = viewModel::onNameChange,
                    enabled = uiState.isEditing && !uiState.isSaving
                )

                LabeledTextField(
                    label = "Apellidos",
                    value = uiState.lastName,
                    onValueChange = viewModel::onLastNameChange,
                    enabled = uiState.isEditing && !uiState.isSaving
                )

                LabeledTextField(
                    label = "Teléfono celular",
                    value = uiState.phone,
                    onValueChange = viewModel::onPhoneChange,
                    enabled = uiState.isEditing && !uiState.isSaving
                )

                if (uiState.hasAddressFields) {
                    LabeledTextField(
                        label = "País",
                        value = uiState.country,
                        onValueChange = viewModel::onCountryChange,
                        enabled = uiState.isEditing && !uiState.isSaving
                    )

                    LabeledTextField(
                        label = "Estado",
                        value = uiState.state,
                        onValueChange = viewModel::onStateChange,
                        enabled = uiState.isEditing && !uiState.isSaving
                    )

                    LabeledTextField(
                        label = "Municipio",
                        value = uiState.municipality,
                        onValueChange = viewModel::onMunicipalityChange,
                        enabled = uiState.isEditing && !uiState.isSaving
                    )
                }
            }
            //button outside scrollable area
            if (uiState.isEditing) {
                AppButton(
                    onClick = viewModel::requestSaveConfirmation,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(top = 8.dp, bottom = 16.dp),
                    enabled = !uiState.isSaving,
                    text = "Guardar cambios",
                )
            }
        }
    }

//save confirmation
if (uiState.showSaveConfirmation) {
    ConfirmationDialog(
        onConfirm = viewModel::confirmSave,
        onDismiss = viewModel::dismissSaveConfirmation
    )
}

//discard confirmation
if (uiState.showDiscardConfirmation) {
    DiscardConfirmationDialog(
        onConfirm = viewModel::confirmDiscard,
        onDismiss = viewModel::dismissDiscardConfirmation
    )
}
}

//confirmation pop ups
@Composable
fun ConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Confirmar cambios",
                style = AppTextStyle.TitleMedium,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = "¿Deseas guardar los datos ingresados?",
                style = AppTextStyle.BodyMedium,
                fontWeight = FontWeight.Normal
            )
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(
                    text = "Confirmar",
                    style = AppTextStyle.LabelMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text(
                    text = "Cancelar",
                    style = AppTextStyle.LabelMedium,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    )
}

@Composable
fun DiscardConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "¿Descartar cambios?",
                style = AppTextStyle.TitleMedium,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = "Los cambios que no hayas guardado se perderán. ¿Deseas salir de la edición?",
                style = AppTextStyle.BodyMedium,
                fontWeight = FontWeight.Normal
            )
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(
                    text = "Descartar",
                    style = AppTextStyle.LabelMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text(
                    text = "Seguir editando",
                    style = AppTextStyle.LabelMedium,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    )
}
