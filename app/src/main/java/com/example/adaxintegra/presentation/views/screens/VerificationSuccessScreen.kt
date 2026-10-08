package com.example.adaxintegra.presentation.views.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.adaxintegra.presentation.views.designsystem.organisms.VerificationSuccessContent
import com.example.adaxintegra.ui.theme.AdaxIntegraTheme

/**
 * // G-09-VerifyOTP: Screen displayed upon successful 6-digit OTP account verification.
 * Matches exact UI mockup with centered success badge, confirmation text, and outlined "Continuar" button.
 */
@Suppress("ktlint:standard:function-naming")
@Composable
fun VerificationSuccessScreen(
    onContinueClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF3F3F3)),
        contentAlignment = Alignment.Center,
    ) {
        VerificationSuccessContent(onContinueClick = onContinueClick)
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
private fun VerificationSuccessScreenPreview() {
    AdaxIntegraTheme {
        VerificationSuccessScreen(
            onContinueClick = {},
        )
    }
}
