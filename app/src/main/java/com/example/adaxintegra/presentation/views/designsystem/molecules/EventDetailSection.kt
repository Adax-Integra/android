package com.example.adaxintegra.presentation.views.designsystem.molecules

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.ui.theme.Black
import com.example.adaxintegra.ui.theme.Purple

// E-01: one block of the event detail, a purple title and its text.
// When onClick is given the text is a link (the event link or phone)
@Suppress("ktlint:standard:function-naming")
@Composable
fun EventDetailSection(
    title: String,
    text: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    onClick: (() -> Unit)? = null,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Purple,
                    modifier = Modifier.size(18.dp),
                )
            }
            Text(
                text = title.uppercase(),
                style = AppTextStyle.LabelMedium,
                fontWeight = FontWeight.Bold,
                color = Purple,
            )
        }

        Text(
            text = text,
            style = AppTextStyle.BodyMedium,
            color = if (onClick == null) Black else Purple,
            fontWeight = if (onClick == null) FontWeight.Normal else FontWeight.SemiBold,
            modifier = if (onClick == null) {
                Modifier
            } else {
                Modifier.clickable(role = Role.Button, onClick = onClick)
            },
        )
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
fun EventDetailSectionPreview() {
    EventDetailSection(
        title = "Fecha y hora",
        text = "Sábado 25 de octubre, 2026 · 12:00 p.m. - 2:00 p.m.",
        icon = Icons.Outlined.Schedule,
    )
}
