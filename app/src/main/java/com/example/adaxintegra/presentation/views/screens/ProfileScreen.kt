package com.example.adaxintegra.presentation.views.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppButton
import com.example.adaxintegra.presentation.views.designsystem.atoms.ButtonVariant
import com.example.adaxintegra.presentation.views.designsystem.templates.ScreenTemplate

@Suppress("ktlint:standard:function-naming")
// profile screen, reached through bottom nav bar option
@Composable
fun ProfileScreen(
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ScreenTemplate(
        title = "Perfil",
        modifier = modifier.padding(24.dp),
    ) {
        AppButton(
            text = "Cerrar sesión",
            onClick = onLogout,
            variant = ButtonVariant.Outlined,
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
        )
    }
}
