package com.example.adaxintegra.presentation.views.designsystem.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Suppress("ktlint:standard:function-naming")
@Composable
fun ChipClickable(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    leadingIcon: @Composable (() -> Unit)? = null,
    selected: Boolean = false,
) {
    val shape = RoundedCornerShape(size = 4.dp)

    // If not given an icon, we use by default the checkmark
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

    val chipModifier = modifier
        .clip(shape)
        .clickable { onClick() }
        .height(28.dp)
        .then(
            if (selected) {
                Modifier.background(
                    color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f),
                    shape = shape,
                )
            } else {
                Modifier
            }
        )
        .border(
            width = 1.dp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            shape = shape,
        )
        .padding(horizontal = 10.dp, vertical = 4.dp)

    Box(
        modifier = chipModifier,
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
