package com.example.adaxintegra.presentation.views.designsystem.organisms

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppIcons
import com.example.adaxintegra.ui.theme.AdaxIntegraTheme
import com.example.adaxintegra.ui.theme.Orange

// options for further details of profile information
// G-08: the colors can change, like the orange "Desactivar cuenta" option
@Suppress("ktlint:standard:function-naming")
@Composable
fun MenuOptionCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    subtitleColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = contentColor,
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = contentColor,
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = subtitleColor,
                )
            }

            Icon(
                imageVector = AppIcons.ChevronRight,
                contentDescription = "Abrir",
                tint = subtitleColor,
            )
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
private fun MenuOptionPreview() {
    AdaxIntegraTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            MenuOptionCard(
                icon = AppIcons.Profile,
                title = "Datos Personales",
                subtitle = "Nombre, teléfono, correo",
                onClick = {},
            )
            Spacer(modifier = Modifier.padding(top = 12.dp))
            MenuOptionCard(
                icon = AppIcons.Lock,
                title = "Seguridad",
                subtitle = "Cambio de contraseña",
                onClick = {},
            )
            Spacer(modifier = Modifier.padding(top = 12.dp))
            MenuOptionCard(
                icon = AppIcons.Delete,
                title = "Desactivar cuenta",
                subtitle = "Esta acción no se puede deshacer",
                onClick = {},
                containerColor = Orange.copy(alpha = 0.12f),
                contentColor = Orange,
                subtitleColor = Orange,
            )
        }
    }
}
