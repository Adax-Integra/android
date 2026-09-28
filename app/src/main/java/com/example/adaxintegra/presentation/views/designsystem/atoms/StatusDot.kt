package com.example.adaxintegra.presentation.views.designsystem.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// colored dot that indicates the case state
@Suppress("ktlint:standard:function-naming")
@Composable
fun StatusDot(color: Color) {
    Box(
        modifier =
            Modifier
                .size(8.dp)
                .background(color = color, shape = CircleShape),
    )
}
