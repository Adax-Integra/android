package com.example.adaxintegra.presentation.views.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
    onRegisterClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF3F3F3))
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState())
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        LoginHeader()

        Spacer(modifier = Modifier.height(32.dp))

        LoginForm(
            emailValue = uiState.email,
            onEmailChange = { viewModel.onEmailChanger(it) },
            passwordValue = uiState.password,
            onPasswordChange = { viewModel.onPasswordChanged(it) },
            onLoginClick = { viewModel.login() },
            onRegisterClick = onRegisterClick,
            onForgotPasswordClick = onForgotPasswordClick,
            isLoading = uiState.isLoading,
            errorMessage = uiState.error,
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}
