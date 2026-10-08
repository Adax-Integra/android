package com.example.adaxintegra.presentation.views.designsystem.organisms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.R
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppButton
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.ButtonVariant
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.presentation.views.designsystem.atoms.VerificationSuccessBadge

/**
 * // G-09-VerifyOTP: Organism component displaying the account verification success screen content.
 */
@Suppress("ktlint:standard:function-naming")
@Composable
fun VerificationSuccessContent(
    onContinueClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        // // G-09-VerifyOTP: Success badge atom
        VerificationSuccessBadge(size = 80)

        Spacer(modifier = Modifier.height(32.dp))

        // // G-09-VerifyOTP: Title text
        Text(
            text = stringResource(id = R.string.account_verified_title),
            style = AppTextStyle.TitleLarge,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(16.dp))

        // // G-09-VerifyOTP: Subtitle description
        Text(
            text = stringResource(id = R.string.account_verified_subtitle),
            style = AppTextStyle.BodyMedium,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp),
        )

        Spacer(modifier = Modifier.height(80.dp))

        // // G-09-VerifyOTP: Outlined "Continuar" button
        AppButton(
            text = stringResource(id = R.string.continue_button),
            onClick = onContinueClick,
            variant = ButtonVariant.Outlined,
            modifier = Modifier.fillMaxWidth(0.75f),
        )
    }
}
