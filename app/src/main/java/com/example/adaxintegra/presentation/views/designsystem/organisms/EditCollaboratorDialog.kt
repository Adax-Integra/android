package com.example.adaxintegra.presentation.views.designsystem.organisms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.domain.model.CollaboratorDetail
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppButton
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.ButtonVariant
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.presentation.views.designsystem.molecules.LabeledTextField

@Suppress("ktlint:standard:function-naming")
@Composable
fun EditCollaboratorDialog(
    collaborator: CollaboratorDetail,
    isSaving: Boolean,
    error: String?,
    onSave: (String?, String?, String?) -> Unit,
    onCancel: () -> Unit,
) {
    var name by rememberSaveable(collaborator.userId) {
        mutableStateOf(collaborator.name)
    }
    var lastName by rememberSaveable(collaborator.userId) {
        mutableStateOf(collaborator.lastName)
    }
    var email by rememberSaveable(collaborator.userId) {
        mutableStateOf(collaborator.email.orEmpty())
    }
    var confirming by rememberSaveable(collaborator.userId) {
        mutableStateOf(false)
    }

    val newName = name.trim()
    val newLastName = lastName.trim()
    val newEmail = email.trim().lowercase()
    val oldEmail = collaborator.email.orEmpty().trim().lowercase()

    val nameChanged = newName != collaborator.name
    val lastNameChanged = newLastName != collaborator.lastName
    val emailChanged = newEmail != oldEmail

    val nameError = when {
        newName.isEmpty() -> "El nombre es obligatorio."
        newName.length > 50 -> "Máximo 50 caracteres."
        else -> null
    }
    val lastNameError = when {
        newLastName.isEmpty() -> "Los apellidos son obligatorios."
        newLastName.length > 50 -> "Máximo 50 caracteres."
        else -> null
    }
    val emailError = when {
        emailChanged && !Regex("""^[^\s@]+@[^\s@]+\.[^\s@]+$""").matches(newEmail) ->
            "Ingresa un correo válido."

        newEmail.length > 128 -> "Máximo 128 caracteres."
        else -> null
    }

    val canSave = (nameChanged || lastNameChanged || emailChanged) &&
        nameError == null && lastNameError == null &&
        emailError == null && !isSaving

    if (!confirming) {
        AlertDialog(
            onDismissRequest = {
                if (!isSaving) onCancel()
            },
            containerColor = MaterialTheme.colorScheme.surface,
            title = {
                Text(
                    text = "Editar colaboradora",
                    style = AppTextStyle.TitleMedium,
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 450.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    LabeledTextField(
                        label = "NOMBRE(S)",
                        value = name,
                        onValueChange = { name = it },
                        errorMessage = nameError,
                        enabled = !isSaving,
                    )
                    LabeledTextField(
                        label = "APELLIDOS",
                        value = lastName,
                        onValueChange = { lastName = it },
                        errorMessage = lastNameError,
                        enabled = !isSaving,
                    )
                    LabeledTextField(
                        label = "CORREO ELECTRÓNICO",
                        value = email,
                        onValueChange = { email = it },
                        errorMessage = emailError,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                        ),
                        enabled = !isSaving,
                    )
                    if (error != null) {
                        Text(
                            text = error,
                            style = AppTextStyle.BodySmall,
                            color = MaterialTheme.colorScheme.error,
                        )
                    }
                }
            },
            confirmButton = {
                AppButton(
                    text = "Guardar",
                    onClick = { confirming = true },
                    enabled = canSave,
                )
            },
            dismissButton = {
                AppButton(
                    text = "Cancelar",
                    onClick = onCancel,
                    variant = ButtonVariant.Outlined,
                    enabled = !isSaving,
                )
            },
        )
    } else {
        AlertDialog(
            onDismissRequest = {
                if (!isSaving) confirming = false
            },
            containerColor = MaterialTheme.colorScheme.surface,
            title = {
                Text(
                    text = "Confirmar cambios",
                    style = AppTextStyle.TitleMedium,
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    if (nameChanged) {
                        Text("Nombre: ${collaborator.name} → $newName")
                    }
                    if (lastNameChanged) {
                        Text("Apellidos: ${collaborator.lastName} → $newLastName")
                    }
                    if (emailChanged) {
                        Text("Correo: $oldEmail → $newEmail")
                    }
                    if (error != null) {
                        Text(
                            text = error,
                            style = AppTextStyle.BodySmall,
                            color = MaterialTheme.colorScheme.error,
                        )
                    }
                }
            },
            confirmButton = {
                AppButton(
                    text = "Confirmar",
                    onClick = {
                        onSave(
                            newName.takeIf { nameChanged },
                            newLastName.takeIf { lastNameChanged },
                            newEmail.takeIf { emailChanged },
                        )
                    },
                    enabled = !isSaving,
                    isLoading = isSaving,
                )
            },
            dismissButton = {
                AppButton(
                    text = "Volver",
                    onClick = { confirming = false },
                    variant = ButtonVariant.Outlined,
                    enabled = !isSaving,
                )
            },
        )
    }
}
