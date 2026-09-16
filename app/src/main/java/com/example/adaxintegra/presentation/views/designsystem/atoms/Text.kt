package com.example.adaxintegra.presentation.views.designsystem.atoms

import androidx.compose.ui.graphics.Color
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow

enum class AppTextStyle {
    TitleLarge,
    TitleMedium,
    BodyLarge,
    BodyMedium,
    BodySmall,
    LabelMedium,
    LabelSmall
}

@Composable
fun Text(
    text: String,
    modifier: Modifier = Modifier,
    style: AppTextStyle = AppTextStyle.BodyMedium,
    color: Color = Color.Unspecified,
    textAlign: TextAlign? = null,
    overflow: TextOverflow = TextOverflow.Clip,
    fontWeight: FontWeight
) {
    val textStyle: TextStyle = when (style) {
        AppTextStyle.TitleLarge -> MaterialTheme.typography.titleLarge
        AppTextStyle.TitleMedium -> MaterialTheme.typography.titleMedium
        AppTextStyle.BodyLarge -> MaterialTheme.typography.bodyLarge
        AppTextStyle.BodyMedium -> MaterialTheme.typography.bodyMedium
        AppTextStyle.BodySmall -> MaterialTheme.typography.bodySmall
        AppTextStyle.LabelMedium -> MaterialTheme.typography.labelMedium
        AppTextStyle.LabelSmall -> MaterialTheme.typography.labelSmall
    }

    Text(
        text = text,
        modifier = modifier,
        style = textStyle,
        color = color,
        textAlign = textAlign,
        overflow = overflow,
        fontWeight = fontWeight
    )
}