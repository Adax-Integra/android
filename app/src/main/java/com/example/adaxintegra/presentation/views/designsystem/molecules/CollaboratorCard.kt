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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppIcon
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppIcons
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.ui.theme.AdaxIntegraTheme
import com.example.adaxintegra.ui.theme.IconGrey
import com.example.adaxintegra.ui.theme.Purple
import com.example.adaxintegra.ui.theme.White

// Same green and red used for the case status
private val ActiveColor = Color(0xFF34C759)
private val InactiveColor = Color(0xFFFF3B30)

private fun initials(name: String, lastName: String): String {
    var result = ""
    if (name.isNotBlank()) {
        result += name.trim().first().uppercaseChar()
    }
    if (lastName.isNotBlank()) {
        result += lastName.trim().first().uppercaseChar()
    }
    return result
}

// G-06: card of one collaborator (initials, name, email and status)
@Suppress("ktlint:standard:function-naming")
@Composable
fun CollaboratorCard(
    name: String,
    lastName: String,
    email: String?,
    isActive: Boolean,
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
            // Circle with the initials of the collaborator
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Purple, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = initials(name, lastName),
                    style = AppTextStyle.TitleMedium,
                    fontWeight = FontWeight.Bold,
                    color = White,
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = "$name $lastName".trim(),
                    style = AppTextStyle.BodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    AppIcon(
                        imageVector = AppIcons.Email,
                        contentDescription = null,
                        tint = IconGrey,
                    )
                    Text(
                        text = email ?: "Sin correo registrado",
                        style = AppTextStyle.LabelMedium,
                        color = IconGrey,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }

            StatusBadge(isActive = isActive)

            // Only visual for now: the detail of a collaborator is not part of G-06
            AppIcon(
                imageVector = AppIcons.ChevronRight,
                contentDescription = null,
                tint = Purple,
            )
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
private fun StatusBadge(isActive: Boolean) {
    val color = if (isActive) ActiveColor else InactiveColor
    val text = if (isActive) "Activa" else "Inactiva"

    Row(
        modifier = Modifier
            .background(color.copy(alpha = 0.15f), RoundedCornerShape(percent = 50))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .background(color, CircleShape),
        )
        Text(
            text = text,
            style = AppTextStyle.LabelSmall,
            fontWeight = FontWeight.Medium,
            color = color,
        )
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
private fun CollaboratorCardPreview() {
    AdaxIntegraTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            CollaboratorCard(
                name = "Ana",
                lastName = "Martinez Gomez",
                email = "ana.martinez@adax.org",
                isActive = true,
            )
            CollaboratorCard(
                name = "Celine",
                lastName = "Hernandez Alonso",
                email = null,
                isActive = false,
            )
        }
    }
}
