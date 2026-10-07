package com.example.adaxintegra.presentation.views.designsystem.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Suppress("ktlint:standard:function-naming")
@Composable
fun ChipClickable(
    text: String,
    leadingIcon: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
) {
    // If not given an icon, we use by default the chackmark
    val effectiveTrailingIcon: @Composable (() -> Unit)? = when {
        leadingIcon != null -> leadingIcon
        selected -> {
            {
                AppIcon(
                    imageVector = AppIcons.CheckMark,
                    contentDescription = "Selected",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        else -> null
    }

    // How the chip should look like when SELECTED
    if (selected) {
        Box(
            modifier =
                Modifier
                    .height(28.dp)
                    .background(
                        color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(size = 4.dp),
                    )
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        shape = RoundedCornerShape(size = 4.dp),
                    )
                    .padding(horizontal = 10.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                effectiveTrailingIcon?.invoke()
                Text(
                    text = text,
                    style = AppTextStyle.LabelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold,
                )
            }

        }
        // How the chip should look like when NOT SELECTED
    } else {
        Box(
            modifier =
                Modifier
                    .height(28.dp)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        shape = RoundedCornerShape(size = 4.dp),
                    )
                    .padding(horizontal = 10.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = text,
                style = AppTextStyle.LabelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChipClickablePreviewSelected() {
    ChipClickable(
        text = "Selected",
        selected = true,
    )
}

@Preview(showBackground = true)
@Composable
fun ChipClickablePreviewNotSelected() {
    ChipClickable(
        text = "Not selected",
        selected = false,
    )
}
