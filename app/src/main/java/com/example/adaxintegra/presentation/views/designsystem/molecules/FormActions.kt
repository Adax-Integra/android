package com.example.adaxintegra.presentation.views.designsystem.molecules

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppButton
import com.example.adaxintegra.presentation.views.designsystem.atoms.ButtonVariant
import com.example.adaxintegra.presentation.views.designsystem.atoms.Spacing

// G-07: "Guardar" and "Cancelar" side by side at the end of a form
@Suppress("ktlint:standard:function-naming")
@Composable
fun FormActions(
    onPrimary: () -> Unit,
    onSecondary: () -> Unit,
    modifier: Modifier = Modifier,
    primaryTitle: String = "Guardar",
    secondaryTitle: String = "Cancelar",
    isPrimaryEnabled: Boolean = true,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Spacing().medium),
    ) {
        AppButton(
            text = primaryTitle,
            onClick = onPrimary,
            enabled = isPrimaryEnabled,
            modifier = Modifier.weight(1f),
        )
        AppButton(
            text = secondaryTitle,
            onClick = onSecondary,
            variant = ButtonVariant.Outlined,
            modifier = Modifier.weight(1f),
        )
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
private fun FormActionsPreview() {
    FormActions(onPrimary = {}, onSecondary = {})
}
