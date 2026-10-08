package com.example.adaxintegra.presentation.views.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.presentation.viewmodel.RegisterUiState
import com.example.adaxintegra.presentation.viewmodel.RegisterViewModel
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppButton
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.ButtonVariant
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.presentation.views.designsystem.molecules.BackIconButton
import com.example.adaxintegra.presentation.views.designsystem.organisms.RegisterForm
import com.example.adaxintegra.presentation.views.designsystem.organisms.UnsavedChangesDialog
import com.example.adaxintegra.ui.theme.AdaxIntegraTheme

/**
 * // G-09-Register: User Registration Screen with form input, field validations, and unsaved changes back handler.
 */
@Suppress("ktlint:standard:function-naming")
@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    onBackClick: () -> Unit,
    onRegisterSuccess: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsState()

    // // G-09-Register: Intercept physical/gesture back button to check for unsaved form changes
    BackHandler {
        viewModel.onBackPressed(onBackClick)
    }

    RegisterScreen(
        uiState = uiState,
        onBackClick = { viewModel.onBackPressed(onBackClick) },
        onRegisterSuccess = { email ->
            viewModel.resetRegisterSuccess()
            onRegisterSuccess(email)
        },
        onNameChange = { viewModel.onNameChanged(it) },
        onLastnameChange = { viewModel.onLastnameChanged(it) },
        onCountryCodeChange = { viewModel.onCountryCodeChanged(it) },
        onPhoneChange = { viewModel.onPhoneChanged(it) },
        onEmailChange = { viewModel.onEmailChanged(it) },
        onPasswordChange = { viewModel.onPasswordChanged(it) },
        onConfirmPasswordChange = { viewModel.onConfirmPasswordChanged(it) },
        onTogglePasswordVisibility = { viewModel.togglePasswordVisibility() },
        onToggleConfirmPasswordVisibility = { viewModel.toggleConfirmPasswordVisibility() },
        onRegisterClick = { viewModel.register() },
        onConfirmRegister = { viewModel.onConfirmRegister() },
        onDismissDialog = { viewModel.onDismissDialog() },
        onDismissUnsavedChanges = { viewModel.dismissUnsavedChangesDialog() },
        modifier = modifier,
    )
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun RegisterScreen(
    uiState: RegisterUiState,
    onBackClick: () -> Unit,
    onRegisterSuccess: (String) -> Unit,
    onNameChange: (String) -> Unit,
    onLastnameChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onToggleConfirmPasswordVisibility: () -> Unit,
    onRegisterClick: () -> Unit,
    modifier: Modifier = Modifier,
    onCountryCodeChange: (String) -> Unit = {},
    onConfirmRegister: () -> Unit = {},
    onDismissDialog: () -> Unit = {},
    onDismissUnsavedChanges: () -> Unit = {},
) {
    LaunchedEffect(uiState.isRegisterSuccess) {
        if (uiState.isRegisterSuccess) {
            onRegisterSuccess(uiState.email)
        }
    }

    // // G-09-Register: Unsaved changes warning dialog
    if (uiState.showUnsavedChangesDialog) {
        UnsavedChangesDialog(
            onConfirm = {
                onDismissUnsavedChanges()
                onBackClick()
            },
            onDismiss = onDismissUnsavedChanges,
        )
    }

    if (uiState.showConfirmationDialog) {
        AlertDialog(
            onDismissRequest = onDismissDialog,
            title = {
                Text(
                    text = "Confirmar registro",
                    style = AppTextStyle.TitleMedium,
                    fontWeight = FontWeight.Bold,
                )
            },
            text = {
                Text(
                    text = "¿Los datos ingresados son correctos?",
                    style = AppTextStyle.BodyMedium,
                    fontWeight = FontWeight.Normal,
                )
            },
            confirmButton = {
                AppButton(
                    text = "Aceptar",
                    onClick = onConfirmRegister,
                )
            },
            dismissButton = {
                AppButton(
                    text = "Cancelar",
                    onClick = onDismissDialog,
                    variant = ButtonVariant.Outlined,
                )
            },
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF3F3F3))
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState())
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterStart,
        ) {
            BackIconButton(onClick = onBackClick)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Crear cuenta",
            style = AppTextStyle.TitleLarge,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Ingresa tus datos para registrarte",
            style = AppTextStyle.BodySmall,
            color = Color.Gray,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(24.dp))

        RegisterForm(
            nameValue = uiState.name,
            onNameChange = onNameChange,
            nameError = uiState.nameError,
            lastnameValue = uiState.lastname,
            onLastnameChange = onLastnameChange,
            lastnameError = uiState.lastnameError,
            countryCode = uiState.countryCode,
            onCountryCodeChange = onCountryCodeChange,
            phoneValue = uiState.phone,
            onPhoneChange = onPhoneChange,
            phoneError = uiState.phoneError,
            emailValue = uiState.email,
            onEmailChange = onEmailChange,
            emailError = uiState.emailError,
            passwordValue = uiState.password,
            onPasswordChange = onPasswordChange,
            passwordError = uiState.passwordError,
            confirmPasswordValue = uiState.confirmPassword,
            onConfirmPasswordChange = onConfirmPasswordChange,
            confirmPasswordError = uiState.confirmPasswordError,
            isPasswordVisible = uiState.isPasswordVisible,
            onTogglePasswordVisibility = onTogglePasswordVisibility,
            isConfirmPasswordVisible = uiState.isConfirmPasswordVisible,
            onToggleConfirmPasswordVisibility = onToggleConfirmPasswordVisibility,
            onRegisterClick = onRegisterClick,
            isLoading = uiState.isLoading,
            errorMessage = uiState.error,
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
private fun RegisterScreenPreview() {
    AdaxIntegraTheme {
        RegisterScreen(
            uiState = RegisterUiState(),
            onBackClick = {},
            onRegisterSuccess = {},
            onNameChange = {},
            onLastnameChange = {},
            onPhoneChange = {},
            onEmailChange = {},
            onPasswordChange = {},
            onConfirmPasswordChange = {},
            onTogglePasswordVisibility = {},
            onToggleConfirmPasswordVisibility = {},
            onRegisterClick = {},
        )
    }
}
