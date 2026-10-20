package com.example.adaxintegra.presentation.views.designsystem.organisms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppIcon
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppIcons
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.presentation.views.designsystem.molecules.ProfileInfoRow
import com.example.adaxintegra.ui.theme.IconGrey

// G-07: white card that groups the rows of one section of the external user's data.
// "isLocked" marks the sections the admin can not edit
@Suppress("ktlint:standard:function-naming")
@Composable
fun ProfileSectionCard(
    title: String,
    modifier: Modifier = Modifier,
    isLocked: Boolean = false,
    content: @Composable ColumnScope.() -> Unit,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = title,
                    modifier = Modifier.weight(1f),
                    style = AppTextStyle.BodyLarge,
                    fontWeight = FontWeight.Bold,
                )

                if (isLocked) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        AppIcon(
                            imageVector = AppIcons.Lock,
                            modifier = Modifier.size(14.dp),
                            tint = IconGrey,
                        )
                        Text(
                            text = "No editable",
                            style = AppTextStyle.LabelSmall,
                            color = IconGrey,
                        )
                    }
                }
            }

            content()
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
private fun ProfileSectionCardPreview() {
    ProfileSectionCard(title = "Datos de registro", isLocked = true) {
        ProfileInfoRow(label = "Nombre(s)", value = "Sandra")
        ProfileInfoRow(label = "Apellidos", value = "Pérez Ruiz")
    }
}
