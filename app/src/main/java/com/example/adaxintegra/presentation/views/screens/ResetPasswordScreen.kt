package com.example.adaxintegra.presentation.views.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.adaxintegra.presentation.viewmodel.ResetPasswordViewModel
import com.example.adaxintegra.presentation.views.designsystem.atoms.AdaxLogo
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppButton
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppIcon
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.IconSize
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.presentation.views.designsystem.molecules.LabeledTextField
import com.example.adaxintegra.presentation.views.designsystem.organisms.AppHeader
import kotlinx.coroutines.delay

@Suppress("ktlint:standard:function-naming")
@Composable
fun ResetPasswordScreen(
    viewModel: ResetPasswordViewModel = hiltViewModel(),
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    onResetPasswordSuccess: () -> Unit,
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var passwordVisible by remember { mutableStateOf(value = false) }
    var confirmPasswordVisible by remember { mutableStateOf(value = false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color(0xFFF3F3F3),
        topBar = {
            AppHeader(
                title = "Cambiar contraseña",
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
                label = "Nueva contraseña",
                value = uiState.password,
                onValueChange = viewModel::onPasswordChange,
                leadingIcon = {
                    AppIcon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        size = IconSize.RegularIcon,
                        tint = Color.Gray,
                    )
                },
                trailingIcon = {
                    val image = if (passwordVisible) {
                    Icons.Default.Visibility
                    } else {
                    Icons.Default.VisibilityOff
                    }

                    IconButton(
                    onClick = {
                    passwordVisible = !passwordVisible
                    }
                    ) {
                        AppIcon(
                            imageVector = image,
                            contentDescription = null,
                            size = IconSize.RegularIcon,
                            tint = Color.Gray,
                        )
                    }
                },
                visualTransformation =
                if (passwordVisible) {
                VisualTransformation.None
                } else {
                PasswordVisualTransformation()
                },
                keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
                ),
            )

            Spacer(modifier = Modifier.height(24.dp))

            LabeledTextField(
                label = "Confirmar contraseña",
                value = uiState.confirmPassword,
                onValueChange = viewModel::onConfirmPasswordChange,
                leadingIcon = {
                    AppIcon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        size = IconSize.RegularIcon,
                        tint = Color.Gray,
                    )
                },
                trailingIcon = {
                    val image = if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        AppIcon(
                            imageVector = image,
                            contentDescription = null,
                            size = IconSize.RegularIcon,
                            tint = Color.Gray,
                        )
                    }
                },
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            )

            Spacer(modifier = Modifier.height(24.dp))

            AppButton(
                text = "Cambiar contraseña",
                onClick = {
                    viewModel.onResetPasswordClick()
                },
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

            //redirect to login after success message appears
            LaunchedEffect(uiState.successMessage) {
                if (uiState.successMessage != null) {
                    delay(2000)
                    onResetPasswordSuccess()
                }
            }

            uiState.errorMessage?.let { message ->
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = message,
                    style = AppTextStyle.BodySmall,
                    color = Color.Red,
                )
            }
        }
    }
}

