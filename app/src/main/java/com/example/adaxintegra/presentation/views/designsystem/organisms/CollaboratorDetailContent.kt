package com.example.adaxintegra.presentation.views.designsystem.organisms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.domain.model.CollaboratorDetail
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppButton
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppIcon
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppIcons
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.ButtonVariant
import com.example.adaxintegra.presentation.views.designsystem.atoms.Spacing
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.ui.theme.AdaxIntegraTheme
import com.example.adaxintegra.ui.theme.IconGrey
import com.example.adaxintegra.ui.theme.Purple
import com.example.adaxintegra.ui.theme.PurplePink
import com.example.adaxintegra.ui.theme.White

// Same green and red used in the G-06 collaborator card
private val ActiveColor = Color(0xFF34C759)
private val InactiveColor = Color(0xFFFF3B30)

private val MONTHS = listOf(
    "ene", "feb", "mar", "abr", "may", "jun",
    "jul", "ago", "sep", "oct", "nov", "dic",
)

// Splits "2024-07-15" into [2024, 07, 15], null if the date is not valid.
// Also accepts a full date with time, only the part before "T" is used
private fun dateParts(date: String?): List<String>? {
    if (date.isNullOrBlank()) {
        return null
    }

    val parts = date.substringBefore('T').split("-")
    val month = parts.getOrNull(1)?.toIntOrNull()

    if (parts.size != 3 || month == null || month !in 1..12) {
        return null
    }
    return parts
}

// "15 jul 2024", the date is shown as the backend sends it, without changing the time zone
private fun formatDate(date: String?): String {
    val parts = dateParts(date) ?: return "Sin registro"
    val day = parts[2].toIntOrNull() ?: return "Sin registro"
    return "$day ${MONTHS[parts[1].toInt() - 1]} ${parts[0]}"
}

// "jul 2024", used in the "Desde" text of the header
private fun formatMonthYear(date: String?): String? {
    val parts = dateParts(date) ?: return null
    return "${MONTHS[parts[1].toInt() - 1]} ${parts[0]}"
}

// The backend sends the role name in English, the screen shows it in Spanish
private fun roleText(role: String?): String = when (role) {
    "internal" -> "Colaboradora"
    "admin" -> "Administradora"
    else -> "Sin rol"
}

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

// G-10: draws the data of one collaborator, the screen only decides when to show it
// G-04: the status section has the button to activate or deactivate the account
@Suppress("ktlint:standard:function-naming")
@Composable
fun CollaboratorDetailContent(
    collaborator: CollaboratorDetail,
    onCopy: (String) -> Unit,
    onChangeStatusClick: () -> Unit,
    onEditClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val spacing = Spacing()
    val fullName = "${collaborator.name} ${collaborator.lastName}".trim()
    val sinceText = formatMonthYear(collaborator.joinedAt)
    val statusColor = if (collaborator.isActive) ActiveColor else InactiveColor
    val statusText = if (collaborator.isActive) "Activa" else "Inactiva"
    val statusDescription = if (collaborator.isActive) {
        "Puede iniciar sesión y recibir nuevas asignaciones."
    } else {
        "No puede iniciar sesión ni recibir nuevas asignaciones. Sus casos actuales permanecen en el expediente hasta que se reasignen."
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(vertical = spacing.small),
        verticalArrangement = Arrangement.spacedBy(spacing.medium),
    ) {
        // Header: initials, name, role, date of entry and status
        DetailCard {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(spacing.medium),
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(Purple, CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = initials(collaborator.name, collaborator.lastName),
                        style = AppTextStyle.TitleLarge,
                        fontWeight = FontWeight.Bold,
                        color = White,
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(spacing.extraSmall),
                ) {
                    Text(
                        text = fullName,
                        style = AppTextStyle.TitleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(spacing.small),
                    ) {
                        RoleBadge(text = roleText(collaborator.role))

                        if (sinceText != null) {
                            Text(
                                text = "Desde $sinceText",
                                style = AppTextStyle.LabelSmall,
                                color = IconGrey,
                            )
                        }
                    }

                    StatusText(text = statusText, color = statusColor)
                }
            }
        }

        DetailSection(title = "Contacto") {
            // G-18: edit the collaborator without leaving G-10
            AppButton(
                text = "Editar",
                onClick = onEditClick,
                variant = ButtonVariant.Outlined,
                modifier = Modifier.fillMaxWidth(),
            )
            ContactRow(
                icon = AppIcons.Email,
                label = "Correo electrónico",
                value = collaborator.email,
                emptyText = "Sin correo registrado",
                onCopy = onCopy,
            )
            HorizontalDivider()
            ContactRow(
                icon = AppIcons.Phone,
                label = "Número de teléfono",
                value = collaborator.phone,
                emptyText = "Sin teléfono registrado",
                onCopy = onCopy,
            )
        }

        DetailSection(title = "Datos organizacionales") {
            InfoRow(label = "Rol", value = roleText(collaborator.role))
            HorizontalDivider()
            InfoRow(label = "Sede", value = collaborator.office ?: "Sin asignar")
            HorizontalDivider()
            InfoRow(label = "Ingreso", value = formatDate(collaborator.joinedAt))
        }

        DetailSection(title = "Estatus en la organización") {
            StatusText(text = statusText, color = statusColor)
            Text(
                text = statusDescription,
                style = AppTextStyle.BodySmall,
                color = IconGrey,
            )

            // G-04: an active account can be deactivated and an inactive one reactivated
            if (collaborator.isActive) {
                AppButton(
                    text = "Desactivar cuenta",
                    onClick = onChangeStatusClick,
                    variant = ButtonVariant.Outlined,
                    modifier = Modifier.fillMaxWidth(),
                )
            } else {
                AppButton(
                    text = "Reactivar cuenta",
                    onClick = onChangeStatusClick,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

// White card with the same look as the G-06 collaborator card
@Suppress("ktlint:standard:function-naming")
@Composable
private fun DetailCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            content = content,
        )
    }
}

// Card with a purple title in capital letters, as in the design
@Suppress("ktlint:standard:function-naming")
@Composable
private fun DetailSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    DetailCard {
        Text(
            text = title.uppercase(),
            style = AppTextStyle.LabelSmall,
            fontWeight = FontWeight.Bold,
            color = Purple,
        )
        content()
    }
}

// Row with an icon, the value and the "Copiar" button
@Suppress("ktlint:standard:function-naming")
@Composable
private fun ContactRow(
    icon: ImageVector,
    label: String,
    value: String?,
    emptyText: String,
    onCopy: (String) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        AppIcon(
            imageVector = icon,
            contentDescription = null,
            tint = Purple,
        )

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = AppTextStyle.LabelSmall,
                color = IconGrey,
            )
            Text(
                text = value ?: emptyText,
                style = AppTextStyle.BodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }

        // Only a registered value can be copied
        if (value != null) {
            TextButton(onClick = { onCopy(value) }) {
                Text(
                    text = "Copiar",
                    color = Purple,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

// Row with the label on the left and the value on the right
@Suppress("ktlint:standard:function-naming")
@Composable
private fun InfoRow(
    label: String,
    value: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = label,
            style = AppTextStyle.BodyMedium,
            color = IconGrey,
        )
        Text(
            text = value,
            modifier = Modifier.weight(1f),
            style = AppTextStyle.BodyMedium,
            textAlign = TextAlign.End,
        )
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
private fun RoleBadge(text: String) {
    Box(
        modifier = Modifier
            .background(PurplePink, RoundedCornerShape(percent = 50))
            .padding(horizontal = 8.dp, vertical = 2.dp),
    ) {
        Text(
            text = text,
            style = AppTextStyle.LabelSmall,
            fontWeight = FontWeight.Medium,
            color = Purple,
        )
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
private fun StatusText(
    text: String,
    color: Color,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(color, CircleShape),
        )
        Text(
            text = text,
            style = AppTextStyle.LabelMedium,
            fontWeight = FontWeight.Medium,
            color = color,
        )
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
fun CollaboratorDetailContentPreview() {
    AdaxIntegraTheme {
        CollaboratorDetailContent(
            collaborator = CollaboratorDetail(
                userId = "user-1",
                name = "Celine",
                lastName = "Hernández Alonso",
                email = "celine.hernandez@adax.org",
                phone = "+524421110000",
                role = "internal",
                office = "Querétaro, Qro.",
                joinedAt = "2024-07-15",
                isActive = false,
            ),
            onCopy = {},
            onChangeStatusClick = {},
        )
    }
}
