package com.example.adaxintegra.presentation.views.screens

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
import com.example.adaxintegra.presentation.views.designsystem.atoms.AdaxLogo
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppButton
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.ButtonVariant
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.presentation.views.designsystem.molecules.BackIconButton
import com.example.adaxintegra.presentation.views.designsystem.organisms.RegisterForm
import com.example.adaxintegra.ui.theme.AdaxIntegraTheme
import com.example.adaxintegra.ui.theme.Purple

@Suppress("ktlint:standard:function-naming")
@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    onBackClick: () -> Unit,
    onRegisterSuccess: (String?) -> Unit,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsState()

    RegisterScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onRegisterSuccess = onRegisterSuccess,
        onNameChange = { viewModel.onNameChanged(it) },
        onLastnameChange = { viewModel.onLastnameChanged(it) },
        onPhoneChange = { viewModel.onPhoneChanged(it) },
        onEmailChange = { viewModel.onEmailChanged(it) },
        onPasswordChange = { viewModel.onPasswordChanged(it) },
        onConfirmPasswordChange = { viewModel.onConfirmPasswordChanged(it) },
        onTogglePasswordVisibility = { viewModel.togglePasswordVisibility() },
        onToggleConfirmPasswordVisibility = { viewModel.toggleConfirmPasswordVisibility() },
        onRegisterClick = { viewModel.register() },
        onConfirmRegister = { viewModel.onConfirmRegister() },
        onDismissDialog = { viewModel.onDismissDialog() },
        modifier = modifier,
    )
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun RegisterScreen(
    uiState: RegisterUiState,
    onBackClick: () -> Unit,
    onRegisterSuccess: (String?) -> Unit,
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
    onConfirmRegister: () -> Unit = {},
    onDismissDialog: () -> Unit = {},
) {
    LaunchedEffect(uiState.isRegisterSuccess) {
        if (uiState.isRegisterSuccess) {
            onRegisterSuccess(uiState.userRole)
        }
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

        AdaxLogo(size = 90.dp)

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Regístrate a",
            style = AppTextStyle.TitleLarge,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.fillMaxWidth(),
        )

        Text(
            text = "ADAX INTEGRA",
            style = AppTextStyle.TitleLarge,
            fontWeight = FontWeight.Black,
            color = Purple,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(24.dp))

        RegisterForm(
            nameValue = uiState.name,
            onNameChange = onNameChange,
            lastnameValue = uiState.lastname,
            onLastnameChange = onLastnameChange,
            countryCode = uiState.countryCode,
            phoneValue = uiState.phone,
            onPhoneChange = onPhoneChange,
            emailValue = uiState.email,
            onEmailChange = onEmailChange,
            passwordValue = uiState.password,
            onPasswordChange = onPasswordChange,
            confirmPasswordValue = uiState.confirmPassword,
            onConfirmPasswordChange = onConfirmPasswordChange,
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
