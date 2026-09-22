package com.example.adaxintegra.presentation.views.designsystem.organisms.timeline

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppIcon
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppIcons

@Suppress("ktlint:standard:function-naming")
@Composable
fun TimelineIndicator(status: TimelineStatus) {
    when (status) {
        // checkmark for completed step
        TimelineStatus.Completed -> {
            Box(
                modifier =
                    Modifier
                        .size(24.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = CircleShape,
                        ),
                contentAlignment = Alignment.Center,
            ) {
                AppIcon(
                    imageVector = AppIcons.CheckMark,
                    contentDescription = "Completado",
                    tint = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }

        // filled circle for step in progress
        TimelineStatus.InProgress -> {
            Box(
                modifier =
                    Modifier
                        .size(24.dp)
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = CircleShape,
                        ),
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    modifier =
                        Modifier
                            .size(10.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primary,
                                shape = CircleShape,
                            ),
                )
            }
        }

        // open circle for not started step
        TimelineStatus.NotStarted -> {
            Box(
                modifier =
                    Modifier
                        .size(24.dp)
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.outline,
                            shape = CircleShape,
                        ),
            )
        }
    }
}
