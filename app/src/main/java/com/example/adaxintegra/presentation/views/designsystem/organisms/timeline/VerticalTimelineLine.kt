package com.example.adaxintegra.presentation.views.designsystem.organisms.timeline

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

//actual line for the timeline
@Composable
fun VerticalTimelineLine() {
    Box(
        modifier = Modifier
            .width(2.dp)
            //will stretch down dynamically depending on size of content
            .fillMaxHeight()
            .background(
                color = MaterialTheme.colorScheme.outlineVariant
            )
    )
}