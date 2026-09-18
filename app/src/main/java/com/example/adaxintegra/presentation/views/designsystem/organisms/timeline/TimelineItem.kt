package com.example.adaxintegra.presentation.views.designsystem.organisms.timeline

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

//item on the timeline
@Composable
fun TimelineItem(
    status: TimelineStatus,
    isLast: Boolean,
    content: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {

        //circle indicator and line to create timeline
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TimelineIndicator(status)

            if (!isLast) {
                VerticalTimelineLine()
            }
        }

        //content displayed next to timeline
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp)
        ) {
            content()
        }
    }
}