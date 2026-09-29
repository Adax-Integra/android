package com.example.adaxintegra.presentation.views.designsystem.organisms

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppIcon
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppIcons
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.IconSize
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text

// general header to be used across screens
@Suppress("ktlint:standard:function-naming")
@Composable
fun AppHeader(
    title: String,
    subtitle: String? = null,
    onBack: (() -> Unit)? = null,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .heightIn(min = 56.dp)
                .padding(vertical = 8.dp),
        // arrow and title centered on the same line
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = {
                onBack?.invoke()
            },
        ) {
            AppIcon(
                imageVector = AppIcons.ArrowBack,
                contentDescription = "Regresar",
                size = IconSize.LargeIcon,
            )
        }

        Spacer(modifier = Modifier.width(4.dp))

        Column {
            Text(
                text = title,
                style = AppTextStyle.TitleLarge,
                fontWeight = FontWeight.Bold,
            )

            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = AppTextStyle.BodyMedium,
                    fontWeight = FontWeight.Normal,
                )
            }
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
fun AppHeaderPreviewWithoutSubtitle() {
    Column {
        AppHeader(title = "Title")
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
fun AppHeaderPreviewWithSubtitle() {
    Column {
        AppHeader(title = "Title", subtitle = "Subtitle")
    }
}
