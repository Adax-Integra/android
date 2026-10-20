package com.example.adaxintegra.presentation.views.designsystem.organisms

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppButton
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.presentation.views.designsystem.atoms.VerificationStatus
import com.example.adaxintegra.ui.theme.AdaxIntegraTheme
import com.example.adaxintegra.ui.theme.IconGrey

// B-01: notice on the home of the external user with her verification status.
// Only "No verificado" has the button, the other states only inform
@Suppress("ktlint:standard:function-naming")
@Composable
fun VerificationBanner(
    status: VerificationStatus,
    onUploadClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ),
        border = BorderStroke(1.dp, status.color.copy(alpha = 0.4f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Icon(
                    imageVector = status.icon,
                    contentDescription = null,
                    tint = status.color,
                    modifier = Modifier.size(20.dp),
                )
                Text(
                    text = verificationBannerTitle(status),
                    style = AppTextStyle.TitleMedium,
                    fontWeight = FontWeight.Bold,
                )
            }

            Text(
                text = verificationBannerMessage(status),
                style = AppTextStyle.BodyMedium,
                color = IconGrey,
            )

            if (status == VerificationStatus.NOT_VERIFIED) {
                AppButton(
                    text = "Subir documentos",
                    onClick = onUploadClick,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

fun verificationBannerTitle(status: VerificationStatus): String = when (status) {
    VerificationStatus.NOT_VERIFIED -> "Verifica tu identidad"
    VerificationStatus.IN_REVIEW -> "Documentos en revisión"
    VerificationStatus.VERIFIED -> "Cuenta verificada"
}

fun verificationBannerMessage(status: VerificationStatus): String = when (status) {
    VerificationStatus.NOT_VERIFIED ->
        "Tu cuenta aún no está verificada. Sube tu identificación (INE) y tu " +
            "comprobante de domicilio para poder crear tu caso."

    VerificationStatus.IN_REVIEW ->
        "Recibimos tus documentos y están en revisión. Te avisaremos cuando tu " +
            "cuenta quede verificada."

    VerificationStatus.VERIFIED -> "Tu identidad ya fue verificada."
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
private fun VerificationBannerPreview() {
    AdaxIntegraTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            VerificationBanner(status = VerificationStatus.NOT_VERIFIED, onUploadClick = {})
            VerificationBanner(status = VerificationStatus.IN_REVIEW, onUploadClick = {})
        }
    }
}
