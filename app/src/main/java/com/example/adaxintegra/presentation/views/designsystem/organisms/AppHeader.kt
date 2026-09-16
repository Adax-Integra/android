package com.example.adaxintegra.presentation.views.designsystem.organisms

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppIcon
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppIcons
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text

@Composable
fun AppHeader(
    title: String,
    subtitle: String? = null,
    onBack: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        IconButton(
            onClick = {
                onBack?.invoke()
            }
        ) {
            AppIcon(
                imageVector = AppIcons.ArrowBack,
                contentDescription = "Regresar"
            )
        }

        Column {
            Text (
                text = title,
                style = AppTextStyle.TitleMedium,
                fontWeight = FontWeight.SemiBold
            )

            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = AppTextStyle.BodySmall,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}
