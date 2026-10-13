package com.example.adaxintegra.presentation.views.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.R
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.adaxintegra.presentation.viewmodel.PasswordRecoveryCodeEvent
import com.example.adaxintegra.presentation.viewmodel.PasswordRecoveryCodeViewModel
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppButton
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppIcon
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.ButtonVariant
import com.example.adaxintegra.presentation.views.designsystem.atoms.IconSize
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.presentation.views.designsystem.molecules.OtpCodeInput
import com.example.adaxintegra.presentation.views.designsystem.templates.ScreenTemplate
import com.example.adaxintegra.ui.theme.LightPurple
import com.example.adaxintegra.ui.theme.Purple

@Composable
fun PasswordRecoveryCodeScreen(
    email: String,
    onBack: () -> Unit,
    onCodeEntered: (email: String, code: String) -> Unit,
    viewModel: PasswordRecoveryCodeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(email) { viewModel.setEmail(email) }
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is PasswordRecoveryCodeEvent.CodeEntered -> onCodeEntered(event.email, event.code)
            }
        }
    }

    val limitReached = uiState.sendCount >= uiState.maxSends
    val canResend = uiState.cooldownSeconds == 0 &&
        uiState.sendCount < uiState.maxSends

    ScreenTemplate(
        title = "Recuperar contraseña",
        onBack = onBack,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Se envió un código de 6 dígitos.",
                style = AppTextStyle.BodyMedium,
                color = Color.Gray,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(24.dp))

            //top card indicating what email code was sent to
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    AppIcon(
                        imageVector = Icons.Outlined.Mail,
                        contentDescription = null,
                        size = IconSize.LargeIcon,
                        tint = Purple,
                    )
                    Spacer(modifier = Modifier.size(16.dp))

                    Column {
                        Text(
                            text = "CÓDIGO ENVIADO A",
                            style = AppTextStyle.BodySmall,
                            color = Color.Gray,
                            fontWeight = FontWeight.Bold,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = email,
                            style = AppTextStyle.TitleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                        )

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "¿Correo incorrecto? Cambiar correo",
                            style = AppTextStyle.BodySmall,
                            color = LightPurple,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable { onBack() },
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            //card to input code with countdown timer
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    OtpCodeInput(
                        otpCode = uiState.otpCode,
                        onOtpCodeChange = viewModel::onOtpCodeChanged,
                        modifier = Modifier.fillMaxWidth(),
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        val timerText = when {
                            limitReached -> "Límite de reenvíos alcanzado"
                            uiState.cooldownSeconds > 0 ->
                                stringResource(id = R.string.resend_code_in, uiState.cooldownSeconds)
                            else -> "Reenviar código"
                        }

                        //attempted code inputs
                        Text(text = timerText, style = AppTextStyle.BodySmall, color = Color.Gray)
                        Text(
                            text = "Envío ${uiState.sendCount} de ${uiState.maxSends}",
                            style = AppTextStyle.BodySmall,
                            color = Color.Gray,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                //continue to verify code input
                AppButton(
                    text = "Continuar",
                    onClick = viewModel::onContinueClick,
                    variant = ButtonVariant.Primary,
                    enabled = uiState.otpCode.length == 6,
                    modifier = Modifier.fillMaxWidth(),
                )

                //resend code
                AppButton(
                    text = stringResource(id = R.string.did_not_receive_code),
                    onClick = viewModel::onResendClick,
                    variant = ButtonVariant.Outlined,
                    enabled = canResend,
                    isLoading = uiState.isResending,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            uiState.successMessage?.let { message ->
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = message,
                    style = AppTextStyle.BodyMedium,
                    color = Color(0xFF2E7D32),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            uiState.errorMessage?.let { message ->
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = message,
                    style = AppTextStyle.BodyMedium,
                    color = Color.Red,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
