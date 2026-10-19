package com.example.adaxintegra.presentation.views.designsystem.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.outlined.GppMaybe
import androidx.compose.material.icons.outlined.PendingActions
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.ui.theme.Orange

private val InReviewColor = Color(0xFFF5A623)
private val VerifiedColor = Color(0xFF43A047)

enum class VerificationStatus(
    val value: String,
    val label: String,
    val color: Color,
    val icon: ImageVector,
) {
    NOT_VERIFIED("NO_VERIFICADO", "No verificado", Orange, Icons.Outlined.GppMaybe),
    IN_REVIEW("EN_REVISION", "En revisión", InReviewColor, Icons.Outlined.PendingActions),
    VERIFIED("VERIFICADO", "Verificado", VerifiedColor, Icons.Filled.Verified),
    ;

    companion object {
        // An unknown or missing status is shown as not verified
        fun from(value: String?): VerificationStatus = entries.find { it.value == value } ?: NOT_VERIFIED
    }
}

// G-07: pill with the icon and the name of the verification status
@Suppress("ktlint:standard:function-naming")
@Composable
fun VerificationBadge(
    status: VerificationStatus,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .background(status.color.copy(alpha = 0.15f), RoundedCornerShape(percent = 50))
            .padding(horizontal = 10.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Icon(
            imageVector = status.icon,
            contentDescription = null,
            tint = status.color,
            modifier = Modifier.size(14.dp),
        )
        Text(
            text = status.label,
            style = AppTextStyle.LabelSmall,
            fontWeight = FontWeight.SemiBold,
            color = status.color,
        )
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
private fun VerificationBadgePreview() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        VerificationStatus.entries.forEach { VerificationBadge(status = it) }
    }
}
