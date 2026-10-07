package com.example.adaxintegra.presentation.views.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.R
import com.example.adaxintegra.presentation.viewmodel.PendingVerificationViewModel
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppButton
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppIcon
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.ButtonVariant
import com.example.adaxintegra.presentation.views.designsystem.atoms.IconSize
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.presentation.views.designsystem.molecules.BackIconButton
import com.example.adaxintegra.presentation.views.designsystem.molecules.OtpCodeInput
import com.example.adaxintegra.ui.theme.Purple

/**
 * // G-09-VerifyOTP: Screen enabling 6-digit OTP code verification for user account registration.
 * Matches exact UI specifications: masked email card, 6 OTP digit boxes, cooldown timer, and resend attempts.
 */
@Suppress("ktlint:standard:function-naming")
@Composable
fun PendingVerificationScreen(
    email: String,
    viewModel: PendingVerificationViewModel,
    onBackToLogin: () -> Unit,
    modifier: Modifier = Modifier,
    onVerificationSuccess: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()

    // // G-09-VerifyOTP: Initialize target email address in ViewModel
    LaunchedEffect(email) {
        viewModel.setEmail(email)
    }

    // // G-09-VerifyOTP: Handle successful OTP verification navigation
    LaunchedEffect(uiState.isVerifiedSuccess) {
        if (uiState.isVerifiedSuccess) {
            onVerificationSuccess()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF3F3F3))
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // // G-09-VerifyOTP: Top bar with back button and screen title
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BackIconButton(onClick = onBackToLogin)
            Spacer(modifier = Modifier.size(12.dp))
            Text(
                text = stringResource(id = R.string.verify_account_title),
                style = AppTextStyle.TitleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // // G-09-VerifyOTP: Subtitle description
        Text(
            text = stringResource(id = R.string.verify_account_subtitle),
            style = AppTextStyle.BodyMedium,
            color = Color.Gray,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(24.dp))

        // // G-09-VerifyOTP: Card 1 - Email notification target card
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
                        text = stringResource(id = R.string.code_sent_to),
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
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // // G-09-VerifyOTP: Card 2 - 6-Digit OTP Code Input and cooldown status
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
                // // G-09-VerifyOTP: 6-Box OTP Input component
                OtpCodeInput(
                    otpCode = uiState.otpCode,
                    onOtpCodeChange = { viewModel.onOtpCodeChanged(it) },
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(16.dp))

                // // G-09-VerifyOTP: Timer subtext row showing remaining cooldown and resend attempts
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    val cooldownText = if (uiState.cooldownSeconds > 0) {
                        stringResource(id = R.string.resend_code_in, uiState.cooldownSeconds)
                    } else {
                        "Reenviar código"
                    }

                    Text(
                        text = cooldownText,
                        style = AppTextStyle.BodySmall,
                        color = Color.Gray,
                    )

                    Text(
                        text = stringResource(
                            id = R.string.resend_attempt,
                            uiState.resendAttempts,
                            uiState.maxResendAttempts,
                        ),
                        style = AppTextStyle.BodySmall,
                        color = Color.Gray,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // // G-09-VerifyOTP: Action Buttons Row / Column
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            AppButton(
                text = stringResource(id = R.string.verify_button),
                onClick = { viewModel.verifyCode() },
                variant = ButtonVariant.Primary,
                enabled = uiState.otpCode.length == 6 && !uiState.isVerifying,
                isLoading = uiState.isVerifying,
                modifier = Modifier.weight(1f),
            )

            AppButton(
                text = stringResource(id = R.string.did_not_receive_code),
                onClick = { viewModel.resendEmail() },
                variant = ButtonVariant.Outlined,
                enabled = uiState.cooldownSeconds == 0 && !uiState.isLoading,
                isLoading = uiState.isLoading,
                modifier = Modifier.weight(1f),
            )
        }

        // // G-09-VerifyOTP: Success message display
        uiState.successMessage?.let { msg ->
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = msg,
                style = AppTextStyle.BodyMedium,
                color = Color(0xFF2E7D32),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        // // G-09-VerifyOTP: Error message display
        uiState.errorMessage?.let { msg ->
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = msg,
                style = AppTextStyle.BodyMedium,
                color = Color.Red,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}
