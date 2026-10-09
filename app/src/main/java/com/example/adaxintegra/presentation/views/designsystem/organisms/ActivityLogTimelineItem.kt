package com.example.adaxintegra.presentation.views.designsystem.organisms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.domain.model.ActivityLogEntry
import com.example.adaxintegra.presentation.util.DateFormatter
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.presentation.views.designsystem.molecules.ActivityLogCard
import com.example.adaxintegra.ui.theme.AdaxIntegraTheme
import com.example.adaxintegra.ui.theme.Purple
import java.util.Date

// V-06: one row of the timeline: time on the left, purple line and the card
@Suppress("ktlint:standard:function-naming")
@Composable
fun ActivityLogTimelineItem(
    entry: ActivityLogEntry,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Local time of the action (ex. 12:44)
        Text(
            text = DateFormatter.time(entry.createdAt),
            style = AppTextStyle.LabelMedium,
            fontWeight = FontWeight.Bold,
            color = Purple,
            modifier = Modifier.width(48.dp),
        )

        // Vertical line that joins the actions
        Box(
            modifier = Modifier
                .width(4.dp)
                .fillMaxHeight()
                .background(Purple),
        )

        ActivityLogCard(
            action = entry.action,
            title = entry.title,
            description = entry.description,
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp, top = 6.dp, bottom = 6.dp),
        )
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
fun ActivityLogTimelineItemPreview() {
    AdaxIntegraTheme {
        ActivityLogTimelineItem(
            entry = ActivityLogEntry(
                logId = "1",
                actorName = "Mariana Robles Ortega",
                action = "update_external_profile",
                title = "Edición de datos de usuaria",
                description = "Mariana Robles Ortega editó los datos de Valeria Prueba Hora",
                reason = "Corrección del teléfono",
                createdAt = Date(),
            ),
        )
    }
}
