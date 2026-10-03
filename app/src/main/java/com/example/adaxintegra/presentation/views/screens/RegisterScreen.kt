package com.example.adaxintegra.presentation.views.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mail
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
import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.presentation.viewmodel.RegisterUiState
import com.example.adaxintegra.presentation.viewmodel.RegisterViewModel
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppIcon
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.IconSize
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.presentation.views.designsystem.organisms.RegisterForm
import com.example.adaxintegra.ui.theme.AdaxIntegraTheme
import com.example.adaxintegra.ui.theme.Purple

@Suppress("ktlint:standard:function-naming")
@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    onRegisterSuccess: (String?) -> Unit,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsState()

    RegisterScreen(
        uiState = uiState,
        onRegisterSuccess = onRegisterSuccess,
        onPhoneChange = { viewModel.onPhoneChanged(it) },
        onEmailChange = { viewModel.onEmailChanged(it) },
        onPasswordChange = { viewModel.onPasswordChanged(it) },
        onConfirmPasswordChange = { viewModel.onConfirmPasswordChanged(it) },
        onTogglePasswordVisibility = { viewModel.togglePasswordVisibility() },
        onToggleConfirmPasswordVisibility = { viewModel.toggleConfirmPasswordVisibility() },
        onRegisterClick = { viewModel.register() },
        modifier = modifier,
    )
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun RegisterScreen(
    uiState: RegisterUiState,
    onRegisterSuccess: (String?) -> Unit,
    onPhoneChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onToggleConfirmPasswordVisibility: () -> Unit,
    onRegisterClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LaunchedEffect(uiState.isRegisterSuccess) {
        if (uiState.isRegisterSuccess) {
            onRegisterSuccess(uiState.userRole)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0XFFF3F3F3))
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        AppIcon(
            imageVector = Icons.Default.Mail,
            contentDescription = "Logo",
            size = IconSize.LargeIcon,
            tint = Purple,
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Registrate a",
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
            onRegisterSuccess = {},
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
