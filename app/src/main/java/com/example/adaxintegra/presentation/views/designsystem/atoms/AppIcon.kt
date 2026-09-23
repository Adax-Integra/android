package com.example.adaxintegra.presentation.views.designsystem.atoms

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// how icons will be displayed
enum class IconSize(
    val dp: Dp,
) {
    RegularIcon(20.dp),
    LargeIcon(24.dp),
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun AppIcon(
    imageVector: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    size: IconSize = IconSize.RegularIcon,
    tint: Color = LocalContentColor.current,
) {
    Icon(
        imageVector = imageVector,
        contentDescription = contentDescription,
        modifier = modifier.size(size.dp),
        tint = tint,
    )
}
