package com.example.adaxintegra.presentation.views.designsystem.organisms

import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppButton
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.ButtonVariant
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text

/**
 * // G-09-Register: Organism component displaying a warning dialog when user attempts to navigate back with unsaved form input.
 */
@Suppress("ktlint:standard:function-naming")
@Composable
fun UnsavedChangesDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Información sin guardar",
                style = AppTextStyle.TitleMedium,
                fontWeight = FontWeight.Bold,
            )
        },
        text = {
            Text(
                text = "Tienes información sin guardar, ¿quieres regresar?",
                style = AppTextStyle.BodyMedium,
                fontWeight = FontWeight.Normal,
            )
        },
        confirmButton = {
            AppButton(
                text = "Sí, regresar",
                onClick = onConfirm,
            )
        },
        dismissButton = {
            AppButton(
                text = "Cancelar",
                onClick = onDismiss,
                variant = ButtonVariant.Outlined,
            )
        },
    )
}
