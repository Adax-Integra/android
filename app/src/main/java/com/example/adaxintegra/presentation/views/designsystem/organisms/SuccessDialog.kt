package com.example.adaxintegra.presentation.views.designsystem.organisms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppButton
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppIcon
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppIcons
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.IconSize
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.ui.theme.AdaxIntegraTheme
import com.example.adaxintegra.ui.theme.Purple
import com.example.adaxintegra.ui.theme.White

// Small confirmation dialog with a single "Aceptar" button (reusable)
@Suppress("ktlint:standard:function-naming")
@Composable
fun SuccessDialog(
    title: String,
    message: String,
    onConfirm: () -> Unit,
    confirmText: String = "Aceptar",
) {
    Dialog(onDismissRequest = onConfirm) {
        SuccessDialogContent(
            title = title,
            message = message,
            onConfirm = onConfirm,
            confirmText = confirmText,
        )
    }
}

// Card content (separated from the Dialog so it can be previewed)
@Suppress("ktlint:standard:function-naming")
@Composable
private fun SuccessDialogContent(
    title: String,
    message: String,
    onConfirm: () -> Unit,
    confirmText: String,
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        // Follows the light/dark theme like the other screens
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Check mark inside a purple circle
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(color = Purple, shape = CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                AppIcon(
                    imageVector = AppIcons.CheckMark,
                    contentDescription = null,
                    size = IconSize.LargeIcon,
                    tint = White,
                )
            }

            Text(
                text = title,
                style = AppTextStyle.TitleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )

            Text(
                text = message,
                style = AppTextStyle.BodyMedium,
                textAlign = TextAlign.Center,
            )

            AppButton(
                text = confirmText,
                onClick = onConfirm,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
fun SuccessDialogPreview() {
    AdaxIntegraTheme {
        SuccessDialogContent(
            title = "Registro completo",
            message = "La cuenta de Ana López se registró correctamente.",
            onConfirm = {},
            confirmText = "Aceptar",
        )
    }
}
