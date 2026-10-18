package com.example.adaxintegra.presentation.views.screens.admin

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

import com.example.adaxintegra.domain.model.ExternalUserProfile
import com.example.adaxintegra.presentation.viewmodel.ExternalUserDetailViewModel
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppButton
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.Spacing
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.presentation.views.designsystem.molecules.LabeledTextField
import com.example.adaxintegra.presentation.views.designsystem.molecules.PhoneField
import com.example.adaxintegra.presentation.views.designsystem.templates.ScreenTemplate
import com.example.adaxintegra.ui.theme.Purple

@Suppress("ktlint:standard:function-naming")
@Composable
fun ExternalUserDetailScreen(
    userId: String,
    onBack: () -> Unit,
    viewModel: ExternalUserDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(userId) {
        viewModel.loadProfile(userId)
    }

    LaunchedEffect(uiState.successMessage) {
        uiState.successMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            viewModel.clearSuccessMessage()
        }
    }

    ScreenTemplate(
        title = if (uiState.isEditing) "Editar datos" else "Datos de la externa",
        onBack = {
            if (uiState.isEditing) viewModel.cancelEditing() else onBack()
        },
        modifier = Modifier.padding(horizontal = Spacing().medium),
    ) {
        when {
            uiState.isLoading && uiState.profile == null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(color = Purple)
                }
            }

            uiState.profile == null -> {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Text(
                        text = uiState.error ?: "No se encontró el perfil.",
                        style = AppTextStyle.BodyMedium,
                    )
                    AppButton(
                        text = "Reintentar",
                        onClick = { viewModel.loadProfile(userId) },
                    )
                }
            }

            uiState.isEditing -> {
                ExternalProfileEditContent(uiState, viewModel)
            }

            else -> {
                ExternalProfileDetailContent(
                    profile = requireNotNull(uiState.profile),
                    onEditClick = viewModel::startEditing,
                )
            }
        }
    }

    if (uiState.showConfirmation) {
        ExternalProfileConfirmationDialog(
            uiState = uiState,
            onDismiss = viewModel::dismissConfirmation,
            onConfirm = viewModel::confirmAndSave,
        )
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
private fun ExternalProfileDetailContent(
    profile: ExternalUserProfile,
    onEditClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = listOfNotNull(profile.name, profile.lastName)
                .joinToString(" ").ifBlank { "Cuenta externa" },
            style = AppTextStyle.BodyMedium,
        )

        Text(text = "Datos de registro", style = AppTextStyle.BodyMedium)
        ExternalProfileInfoRow("Nombre", profile.name)
        ExternalProfileInfoRow("Apellidos", profile.lastName)
        ExternalProfileInfoRow("Fecha de nacimiento", profile.birthDate)

        Text(text = "Datos de contacto", style = AppTextStyle.BodyMedium)
        ExternalProfileInfoRow("Correo electrónico", profile.email)
        ExternalProfileInfoRow("Teléfono", profile.phone)

        Text(text = "Domicilio", style = AppTextStyle.BodyMedium)
        ExternalProfileInfoRow("País", profile.country)
        ExternalProfileInfoRow("Estado", profile.state)
        ExternalProfileInfoRow("Municipio", profile.municipality)

        AppButton(
            text = "Editar datos",
            onClick = onEditClick,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
private fun ExternalProfileInfoRow(
    label: String,
    value: String?,
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(text = label, style = AppTextStyle.BodySmall)
        Text(
            text = value?.takeIf { it.isNotBlank() } ?: "No disponible",
            style = AppTextStyle.BodyMedium,
        )
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
private fun ExternalProfileEditContent(
    uiState: ExternalUserDetailUiState,
    viewModel: ExternalUserDetailViewModel,
) {
    val supportedCodes = listOf("+52", "+57", "+58", "+1")
    val matchedCode = supportedCodes.firstOrNull {
        uiState.phone.startsWith(it)
    }
    val phoneCode = matchedCode ?: "+52"
    val nationalNumber = if (matchedCode != null) {
        uiState.phone.removePrefix(matchedCode)
    } else {
        ""
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = "Datos de contacto",
            style = AppTextStyle.BodyMedium,
        )

        LabeledTextField(
            label = "Correo electrónico",
            value = uiState.email,
            onValueChange = viewModel::onEmailChange,
            enabled = !uiState.isSaving,
        )

        if (uiState.phone.isNotBlank() && matchedCode == null) {
            LabeledTextField(
                label = "Teléfono internacional",
                value = uiState.phone,
                onValueChange = viewModel::onPhoneChange,
                enabled = !uiState.isSaving,
            )
        } else {
            PhoneField(
                countryCode = phoneCode,
                phoneValue = nationalNumber,
                onPhoneChange = { digits ->
                    viewModel.onPhoneChange(
                        phoneCode + digits.filter { it.isDigit() }.take(10)
                    )
                },
                onCountryCodeChange = { newCode ->
                    viewModel.onPhoneChange(newCode + nationalNumber)
                },
                enabled = !uiState.isSaving,
                placeholder = "10 dígitos",
            )
        }

        Text(
            text = "Domicilio",
            style = AppTextStyle.BodyMedium,
        )

        LabeledTextField(
            label = "País",
            value = uiState.country,
            onValueChange = viewModel::onCountryChange,
            enabled = !uiState.isSaving,
        )

        LabeledTextField(
            label = "Estado",
            value = uiState.state,
            onValueChange = viewModel::onStateChange,
            enabled = !uiState.isSaving,
        )

        LabeledTextField(
            label = "Municipio",
            value = uiState.municipality,
            onValueChange = viewModel::onMunicipalityChange,
            enabled = !uiState.isSaving,
        )

        OutlinedTextField(
            value = uiState.reason,
            onValueChange = viewModel::onReasonChange,
            label = { Text(text = "Motivo del cambio") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            maxLines = 5,
            enabled = !uiState.isSaving,
        )

        Text(
            text = "${uiState.reason.length}/500 caracteres",
            style = AppTextStyle.BodySmall,
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Checkbox(
                checked = uiState.consentConfirmed,
                onCheckedChange = viewModel::onConsentChange,
                enabled = !uiState.isSaving,
            )
            Text(
                text = "La externa autorizó este cambio",
                style = AppTextStyle.BodySmall,
            )
        }

        uiState.error?.let { message ->
            Text(
                text = message,
                style = AppTextStyle.BodySmall,
            )
        }

        AppButton(
            text = "Guardar cambios",
            onClick = viewModel::requestConfirmation,
            enabled = !uiState.isSaving,
            isLoading = uiState.isSaving,
            modifier = Modifier.fillMaxWidth(),
        )

        TextButton(
            onClick = viewModel::cancelEditing,
            enabled = !uiState.isSaving,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = "Cancelar")
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
private fun ExternalProfileConfirmationDialog(
    uiState: ExternalUserDetailUiState,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    val profile = uiState.profile ?: return

    val changes = listOf(
        Triple("Correo", profile.email.orEmpty(), uiState.email),
        Triple("Teléfono", profile.phone.orEmpty(), uiState.phone),
        Triple("País", profile.country.orEmpty(), uiState.country),
        Triple("Estado", profile.state.orEmpty(), uiState.state),
        Triple("Municipio", profile.municipality.orEmpty(), uiState.municipality),
    ).filter { (_, old, new) -> old.trim() != new.trim() }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Confirmar cambios") },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                changes.forEach { (field, old, new) ->
                    Text(
                        text = "$field: ${old.ifBlank { "No disponible" }} → $new",
                        style = AppTextStyle.BodySmall,
                    )
                }

                Text(
                    text = "Motivo: ${uiState.reason.trim()}",
                    style = AppTextStyle.BodyMedium,
                )
                Text(
                    text = "Autorización de la externa confirmada.",
                    style = AppTextStyle.BodySmall,
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = "Confirmar y guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Volver a editar")
            }
        },
    )
}
