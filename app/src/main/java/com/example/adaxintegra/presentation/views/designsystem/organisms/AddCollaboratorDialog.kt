package com.example.adaxintegra.presentation.views.designsystem.organisms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppButton
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.ButtonVariant
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.presentation.views.designsystem.molecules.LabeledTextField
import com.example.adaxintegra.ui.theme.AdaxIntegraTheme
import com.example.adaxintegra.ui.theme.White

// G-03: "Agregar Colaboradora" modal form
@Suppress("ktlint:standard:function-naming")
@Composable
fun AddCollaboratorDialog(
    name: String,
    lastName: String,
    email: String,
    password: String,
    phone: String,
    fieldErrors: Map<String, String>,
    generalError: String?,
    canSave: Boolean,
    isSaving: Boolean,
    onNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit,
) {
    Dialog(onDismissRequest = onCancel) {
        AddCollaboratorForm(
            name = name,
            lastName = lastName,
            email = email,
            password = password,
            phone = phone,
            fieldErrors = fieldErrors,
            generalError = generalError,
            canSave = canSave,
            isSaving = isSaving,
            onNameChange = onNameChange,
            onLastNameChange = onLastNameChange,
            onEmailChange = onEmailChange,
            onPasswordChange = onPasswordChange,
            onPhoneChange = onPhoneChange,
            onSave = onSave,
            onCancel = onCancel,
        )
    }
}

// Card with the form (separated from the Dialog so it can be previewed)
@Suppress("ktlint:standard:function-naming")
@Composable
private fun AddCollaboratorForm(
    name: String,
    lastName: String,
    email: String,
    password: String,
    phone: String,
    fieldErrors: Map<String, String>,
    generalError: String?,
    canSave: Boolean,
    isSaving: Boolean,
    onNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit,
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = White),
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            Text(
                text = "Agregar Colaboradora",
                style = AppTextStyle.TitleMedium,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            FormField(
                label = "NOMBRE(S)",
                value = name,
                onValueChange = onNameChange,
                error = fieldErrors["name"],
            )

            FormField(
                label = "APELLIDOS",
                value = lastName,
                onValueChange = onLastNameChange,
                error = fieldErrors["lastName"],
            )

            FormField(
                label = "EMAIL",
                value = email,
                onValueChange = onEmailChange,
                error = fieldErrors["email"],
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            )

            FormField(
                label = "CONTRASEÑA",
                value = password,
                onValueChange = onPasswordChange,
                error = fieldErrors["password"],
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation(),
            )

            FormField(
                label = "TELÉFONO",
                value = phone,
                onValueChange = onPhoneChange,
                error = fieldErrors["phone"],
                placeholder = "10 dígitos",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            )

            if (generalError != null) {
                Text(
                    text = generalError,
                    style = AppTextStyle.BodySmall,
                    color = MaterialTheme.colorScheme.error,
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                AppButton(
                    text = "Guardar",
                    onClick = onSave,
                    modifier = Modifier.weight(1f),
                    enabled = canSave,
                    isLoading = isSaving,
                )
                AppButton(
                    text = "Cancelar",
                    onClick = onCancel,
                    modifier = Modifier.weight(1f),
                    variant = ButtonVariant.Outlined,
                    enabled = !isSaving,
                )
            }
        }
    }
}

// One labeled field with its error message below
@Suppress("ktlint:standard:function-naming")
@Composable
private fun FormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    error: String?,
    placeholder: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    LabeledTextField(
        label = label,
        value = value,
        onValueChange = onValueChange,
        placeholder = placeholder,
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
    )
    if (error != null) {
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = error,
            style = AppTextStyle.LabelSmall,
            color = MaterialTheme.colorScheme.error,
        )
    }
    Spacer(modifier = Modifier.height(12.dp))
}

@Suppress("ktlint:standard:function-naming")
// Preview: empty form ("Guardar" disabled)
@Preview(showBackground = true)
@Composable
fun AddCollaboratorFormEmptyPreview() {
    AdaxIntegraTheme {
        AddCollaboratorForm(
            name = "",
            lastName = "",
            email = "",
            password = "",
            phone = "",
            fieldErrors = emptyMap(),
            generalError = null,
            canSave = false,
            isSaving = false,
            onNameChange = {},
            onLastNameChange = {},
            onEmailChange = {},
            onPasswordChange = {},
            onPhoneChange = {},
            onSave = {},
            onCancel = {},
        )
    }
}

@Suppress("ktlint:standard:function-naming")
// Preview: form with validation errors
@Preview(showBackground = true)
@Composable
fun AddCollaboratorFormErrorsPreview() {
    AdaxIntegraTheme {
        AddCollaboratorForm(
            name = "Ana",
            lastName = "López",
            email = "ana@correo",
            password = "123",
            phone = "44212",
            fieldErrors = mapOf(
                "email" to "Ingresa un correo válido.",
                "password" to "La contraseña debe tener al menos 8 caracteres.",
                "phone" to "El teléfono debe tener 10 dígitos.",
            ),
            generalError = null,
            canSave = true,
            isSaving = false,
            onNameChange = {},
            onLastNameChange = {},
            onEmailChange = {},
            onPasswordChange = {},
            onPhoneChange = {},
            onSave = {},
            onCancel = {},
        )
    }
}
