package com.example.adaxintegra.presentation.views.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.presentation.viewmodel.LoginViewModel
import com.example.adaxintegra.presentation.views.designsystem.molecules.LoginHeader
import com.example.adaxintegra.presentation.views.designsystem.organisms.LoginForm

@Suppress("ktlint:standard:function-naming")
@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onNavigateToHome: (String?) -> Unit,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isLoginSuccess) {
        if (uiState.isLoginSuccess) {
            onNavigateToHome(uiState.userRole)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF3F3F3))
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        LoginHeader()

        Spacer(modifier = Modifier.height(32.dp))

        LoginForm(
            emailValue = uiState.email,
            onEmailChange = { viewModel.onEmailChanger(it) },
            passwordValue = uiState.password,
            onPasswordChange = { viewModel.onPasswordChanged(it) },
            onLoginClick = { viewModel.login() },
            onRegisterClick = { /* register action*/ },
            onForgotPasswordClick = { /* recover password */ },
            isLoading = uiState.isLoading,
            errorMessage = uiState.error,
        )
    }
}
