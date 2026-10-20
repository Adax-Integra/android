package com.example.adaxintegra.presentation.views.designsystem.organisms

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppIcon
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppIcons
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.presentation.views.designsystem.atoms.VerificationBadge
import com.example.adaxintegra.presentation.views.designsystem.atoms.VerificationStatus
import com.example.adaxintegra.ui.theme.IconGrey
import com.example.adaxintegra.ui.theme.Purple

// G-07: initials, name, role, email and verification status of the external user
@Suppress("ktlint:standard:function-naming")
@Composable
fun ExternalProfileHeaderCard(
    initials: String,
    name: String,
    email: String,
    status: VerificationStatus,
    modifier: Modifier = Modifier,
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
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(Purple.copy(alpha = 0.12f), CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = initials,
                    style = AppTextStyle.TitleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Purple,
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = name,
                    style = AppTextStyle.TitleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = "Externa",
                    style = AppTextStyle.BodyMedium,
                    color = IconGrey,
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    AppIcon(
                        imageVector = AppIcons.Email,
                        modifier = Modifier.size(16.dp),
                        tint = IconGrey,
                    )
                    Text(
                        text = email,
                        style = AppTextStyle.LabelMedium,
                        color = IconGrey,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                VerificationBadge(
                    status = status,
                    modifier = Modifier.padding(top = 2.dp),
                )
            }
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
private fun ExternalProfileHeaderCardPreview() {
    ExternalProfileHeaderCard(
        initials = "SP",
        name = "Sandra Pérez Ruiz",
        email = "sp.ruiz@ejemplo.com",
        status = VerificationStatus.IN_REVIEW,
    )
}
