package com.example.adaxintegra.presentation.views.designsystem.molecules

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.CreateNewFolder
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.History
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppIcon
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.IconSize
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.ui.theme.AdaxIntegraTheme
import com.example.adaxintegra.ui.theme.IconGrey
import com.example.adaxintegra.ui.theme.LightPurple
import com.example.adaxintegra.ui.theme.Purple

// V-06: icon of each kind of action, as in the design (creation, edition, comment)
private fun actionIcon(action: String): ImageVector = when {
    action.startsWith("create") || action.startsWith("register") -> Icons.Outlined.CreateNewFolder
    action.startsWith("update") || action.startsWith("edit") -> Icons.Outlined.Edit
    action.contains("comment") -> Icons.Outlined.ChatBubbleOutline
    else -> Icons.Outlined.History
}

// V-06: card of one action of the activity log (icon, title and description)
@Suppress("ktlint:standard:function-naming")
@Composable
fun ActivityLogCard(
    action: String,
    title: String,
    description: String,
    modifier: Modifier = Modifier,
) {
    // Theme colors so the card is readable in light and dark mode
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            // Icon inside a light purple circle
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(LightPurple.copy(alpha = 0.35f), CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                AppIcon(
                    imageVector = actionIcon(action),
                    contentDescription = null,
                    size = IconSize.RegularIcon,
                    tint = Purple,
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = title,
                    style = AppTextStyle.BodyMedium,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = description,
                    style = AppTextStyle.LabelMedium,
                    color = IconGrey,
                )
            }
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
fun ActivityLogCardPreview() {
    AdaxIntegraTheme {
        ActivityLogCard(
            action = "update_external_profile",
            title = "Edición de datos de usuaria",
            description = "Mariana Robles Ortega editó los datos de Valeria Prueba Hora",
        )
    }
}
