package com.example.adaxintegra.presentation.views.designsystem.organisms

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.adaxintegra.ui.theme.AdaxIntegraTheme
import com.example.adaxintegra.ui.theme.Purple

// top card that displays user's name and initials avatar
@Suppress("ktlint:standard:function-naming")
@Composable
fun ProfileHeaderCard(
    name: String,
    modifier: Modifier = Modifier,
    initials: String? = null,
    createdAt: String? = null,
) {
    val displayInitials = initials ?: calculateInitials(name)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
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
            // Circular avatar with initials
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(
                        color = Purple,
                        shape = CircleShape,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = displayInitials,
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium,
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                if (!createdAt.isNullOrBlank()) {
                    Text(
                        text = createdAt,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

private fun calculateInitials(fullName: String): String {
    val parts = fullName.trim().split("\\s+".toRegex()).filter { it.isNotBlank() }
    val first = parts[0].firstOrNull()?.uppercaseChar() ?: ""
    val second = parts[1].firstOrNull()?.uppercaseChar() ?: ""
    return "$first$second"
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
private fun ProfileHeaderCardPreview() {
    AdaxIntegraTheme {
        ProfileHeaderCard(
            name = "Celine Hernández Alonso",
            modifier = Modifier.padding(16.dp),
        )
    }
}
