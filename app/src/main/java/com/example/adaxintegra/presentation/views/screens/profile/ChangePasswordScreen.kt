package com.example.adaxintegra.presentation.views.screens.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.adaxintegra.presentation.viewmodel.ChangePasswordViewModel
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppButton
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.ui.theme.AdaxIntegraTheme
import com.example.adaxintegra.ui.theme.Purple

@Suppress("ktlint:standard:function-naming")
@Composable
fun ChangePasswordScreen(
    onBack: () -> Unit,
    viewModel: ChangePasswordViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var showSuccessDialog by rememberSaveable { mutableStateOf(false) }

    ChangePasswordContent(
        uiState = uiState,
        onBack = onBack,
        onCurrentPasswordChange = viewModel::onCurrentPasswordChange,
        onNewPasswordChange = viewModel::onNewPasswordChange,
        onConfirmPasswordChange = viewModel::onConfirmPasswordChange,
        onToggleCurrentPasswordVisibility = viewModel::toggleCurrentPasswordVisibility,
        onToggleNewPasswordVisibility = viewModel::toggleNewPasswordVisibility,
        onToggleConfirmPasswordVisibility = viewModel::toggleConfirmPasswordVisibility,
        onSubmit = {
            viewModel.submitChangePassword(
                onSuccess = { showSuccessDialog = true },
            )
        },
    )

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = {
                showSuccessDialog = false
                onBack()
            },
            title = {
                Text(
                    text = "Contraseña actualizada",
                    style = AppTextStyle.TitleMedium,
                    fontWeight = FontWeight.Bold,
                )
            },
            text = {
                Text(
                    text = "Tu contraseña ha sido actualizada exitosamente.",
                    style = AppTextStyle.BodyMedium,
                )
            },
            confirmButton = {
                AppButton(
                    text = "Aceptar",
                    onClick = {
                        showSuccessDialog = false
                        onBack()
                    },
                )
            },
        )
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun ChangePasswordContent(
    uiState: ChangePasswordUiState,
    onBack: () -> Unit,
    onCurrentPasswordChange: (String) -> Unit,
    onNewPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onToggleCurrentPasswordVisibility: () -> Unit,
    onToggleNewPasswordVisibility: () -> Unit,
    onToggleConfirmPasswordVisibility: () -> Unit,
    onSubmit: () -> Unit,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(scrollState),
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        // Top Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Volver",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onBack() },
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "Cambiar contraseña",
                    style = AppTextStyle.TitleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = "Seguridad",
                    style = AppTextStyle.LabelMedium,
                    fontWeight = FontWeight.Medium,
                    color = Purple,
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Description
        Text(
            text = "Para proteger tu expediente, confirma primero tu contraseña actual.",
            style = AppTextStyle.BodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Card 1: Contraseña Actual
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Text(
                    text = "CONTRASEÑA ACTUAL",
                    style = AppTextStyle.LabelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(modifier = Modifier.height(8.dp))
                PasswordInputField(
                    value = uiState.currentPassword,
                    onValueChange = onCurrentPasswordChange,
                    placeholder = "Escribe tu contraseña actual",
                    isVisible = uiState.isCurrentPasswordVisible,
                    onToggleVisibility = onToggleCurrentPasswordVisibility,
                    imeAction = ImeAction.Next,
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Card 2: Nueva Contraseña + Confirmar Nueva Contraseña
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                // Field 1: Nueva contraseña
                Text(
                    text = "NUEVA CONTRASEÑA",
                    style = AppTextStyle.LabelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(modifier = Modifier.height(8.dp))
                PasswordInputField(
                    value = uiState.newPassword,
                    onValueChange = onNewPasswordChange,
                    placeholder = "Entre 8 y 24 caracteres",
                    isVisible = uiState.isNewPasswordVisible,
                    onToggleVisibility = onToggleNewPasswordVisibility,
                    imeAction = ImeAction.Next,
                )

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                Spacer(modifier = Modifier.height(12.dp))

                // Field 2: Confirmar nueva contraseña
                Text(
                    text = "CONFIRMAR NUEVA CONTRASEÑA",
                    style = AppTextStyle.LabelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(modifier = Modifier.height(8.dp))
                PasswordInputField(
                    value = uiState.confirmPassword,
                    onValueChange = onConfirmPasswordChange,
                    placeholder = "Escríbela de nuevo",
                    isVisible = uiState.isConfirmPasswordVisible,
                    onToggleVisibility = onToggleConfirmPasswordVisibility,
                    imeAction = ImeAction.Done,
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Requirements Checklist
        Text(
            text = "Tu nueva contraseña debe tener:",
            style = AppTextStyle.LabelMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Spacer(modifier = Modifier.height(8.dp))

        val rules = uiState.rules

        RequirementBulletItem(
            text = "Entre 8 y 24 caracteres",
            isMet = rules.hasValidLength,
        )
        RequirementBulletItem(
            text = "Mayúsculas y minúsculas",
            isMet = rules.hasUpperAndLower,
        )
        RequirementBulletItem(
            text = "Al menos un número",
            isMet = rules.hasNumber,
        )
        RequirementBulletItem(
            text = "Diferente a la contraseña actual",
            isMet = rules.isDifferentFromCurrent,
        )
        RequirementBulletItem(
            text = "Las contraseñas deben coincidir",
            isMet = rules.doPasswordsMatch,
        )
        RequirementBulletItem(
            text = "Sin acentos ni letras especiales (ñ, ç, æ...)",
            isMet = rules.hasNoSpecialAccents,
        )

        if (uiState.errorMessage != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = uiState.errorMessage,
                style = AppTextStyle.BodySmall,
                color = MaterialTheme.colorScheme.error,
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Action Button
        AppButton(
            text = "Cambiar contraseña",
            onClick = onSubmit,
            isLoading = uiState.isLoading,
            enabled = uiState.currentPassword.isNotBlank() && rules.isValid,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
private fun PasswordInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isVisible: Boolean,
    onToggleVisibility: () -> Unit,
    imeAction: ImeAction,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Box(modifier = Modifier.weight(1f)) {
            if (value.isEmpty()) {
                Text(
                    text = placeholder,
                    style = AppTextStyle.BodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                )
            }
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                ),
                cursorBrush = SolidColor(Purple),
                visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = imeAction,
                ),
                modifier = Modifier.fillMaxWidth(),
            )
        }

        IconButton(
            onClick = onToggleVisibility,
            modifier = Modifier.size(32.dp),
        ) {
            Icon(
                imageVector = if (isVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                contentDescription = if (isVisible) "Ocultar contraseña" else "Mostrar contraseña",
                tint = Purple,
            )
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
private fun RequirementBulletItem(
    text: String,
    isMet: Boolean,
) {
    val color = if (isMet) Color(0xFF1B5E20) else MaterialTheme.colorScheme.onSurfaceVariant

    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier.padding(vertical = 2.dp),
    ) {
        Text(
            text = "•  ",
            style = AppTextStyle.BodyMedium,
            fontWeight = FontWeight.Bold,
            color = color,
        )
        Text(
            text = text,
            style = AppTextStyle.BodySmall,
            color = color,
        )
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
private fun ChangePasswordScreenPreview() {
    AdaxIntegraTheme {
        ChangePasswordContent(
            uiState = ChangePasswordUiState(
                currentPassword = "Password123",
                newPassword = "NewPassword1",
                confirmPassword = "NewPassword1",
            ),
            onBack = {},
            onCurrentPasswordChange = {},
            onNewPasswordChange = {},
            onConfirmPasswordChange = {},
            onToggleCurrentPasswordVisibility = {},
            onToggleNewPasswordVisibility = {},
            onToggleConfirmPasswordVisibility = {},
            onSubmit = {},
        )
    }
}
