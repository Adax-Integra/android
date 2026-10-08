package com.example.adaxintegra.presentation.views.designsystem.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.ui.theme.Purple
import com.example.adaxintegra.ui.theme.White

/**
 * // G-09-VerifyOTP: Atom component rendering a circular purple badge with a white checkmark icon.
 */
@Suppress("ktlint:standard:function-naming")
@Composable
fun VerificationSuccessBadge(
    modifier: Modifier = Modifier,
    size: Int = 80,
) {
    Box(
        modifier = modifier
            .size(size.dp)
            .background(color = Purple, shape = CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        AppIcon(
            imageVector = AppIcons.CheckMark,
            contentDescription = null,
            size = IconSize.LargeIcon,
            tint = White,
        )
    }
}
