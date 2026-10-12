package com.example.adaxintegra.presentation.views.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MailOutline
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppButton
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppIcon
import com.example.adaxintegra.presentation.views.designsystem.atoms.IconSize
import com.example.adaxintegra.presentation.views.designsystem.molecules.LabeledTextField
import com.example.adaxintegra.presentation.views.designsystem.organisms.AppHeader
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.adaxintegra.presentation.viewmodel.ForgotPasswordViewModel
import com.example.adaxintegra.presentation.views.designsystem.atoms.AdaxLogo
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text

@Suppress("ktlint:standard:function-naming")
@Composable
fun ForgotPasswordScreen(
    viewModel: ForgotPasswordViewModel = hiltViewModel(),
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color(0xFFF3F3F3),
        topBar = {
            AppHeader(
                title = "Recuperar contraseña",
                onBack = onBack,
                ) },
            ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AdaxLogo(size = 150.dp)

            Spacer(modifier = Modifier.height(24.dp))

            LabeledTextField(
                label = "Correo electrónico",
                value = uiState.email,
                onValueChange = viewModel::onEmailChange,
                placeholder = "tu@correo.com",
                errorMessage = uiState.emailError,
                leadingIcon = {
                    AppIcon(
                        imageVector = Icons.Default.MailOutline,
                        contentDescription = null,
                        size = IconSize.RegularIcon,
                        tint = Color.Gray,
                    )
                              },
                )

            Spacer(modifier = Modifier.height(24.dp))

            AppButton(
                text = "Enviar correo",
                onClick = viewModel::onSendEmailClick,
                modifier = Modifier.fillMaxWidth(),
                isLoading = uiState.isLoading,
            )

            uiState.successMessage?.let { message ->
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = message,
                    style = AppTextStyle.BodySmall,
                    color = Color(0xFF2E7D32),
                )
            }

            uiState.errorMessage?.let { message ->
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = message,
                    style = AppTextStyle.BodySmall,
                    color = Color(0xFF2E7D32),
                )
            }
        }
    }
}

