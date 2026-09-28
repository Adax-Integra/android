package com.example.adaxintegra.presentation.views.designsystem.molecules

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.StatusDot
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.ui.theme.IconGrey

// state (dot + text) on the left and last update on the right
@Suppress("ktlint:standard:function-naming")
@Composable
fun StatusRow(
    statusText: String,
    statusColor: Color,
    updatedAt: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            StatusDot(color = statusColor)
            Text(
                text = statusText,
                style = AppTextStyle.LabelMedium,
                fontWeight = FontWeight.Medium,
            )
        }
        Text(
            text = "Actualizado: $updatedAt",
            style = AppTextStyle.LabelMedium,
            fontWeight = FontWeight.Medium,
            color = IconGrey,
        )
    }
}
