package com.example.adaxintegra.presentation.views.designsystem.molecules

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppIcon
import com.example.adaxintegra.presentation.views.designsystem.atoms.IconSize
import com.example.adaxintegra.ui.theme.Purple

// Molecule for back navigation arrow button matching the purple chevron design (<)
@Suppress("ktlint:standard:function-naming")
@Composable
fun BackIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String = "Regresar",
) {
    IconButton(
        onClick = onClick,
        modifier = modifier,
    ) {
        AppIcon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
            contentDescription = contentDescription,
            tint = Purple,
            size = IconSize.LargeIcon,
        )
    }
}
