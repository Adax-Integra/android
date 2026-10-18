package com.example.adaxintegra.presentation.views.designsystem.molecules

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.domain.model.Event
import com.example.adaxintegra.presentation.util.EventDateFormatter
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.EventDateBadge
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.ui.theme.LightOrange
import com.example.adaxintegra.ui.theme.LightPurple
import com.example.adaxintegra.ui.theme.Purple
import com.example.adaxintegra.ui.theme.White
import java.time.Instant

// E-01: event card of the events screen, wider than the carousel one and with the start time
@Suppress("ktlint:standard:function-naming")
@Composable
fun EventListCard(
    event: Event,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(White)
            .clickable(role = Role.Button, onClick = onClick),
    ) {
        Box {
            EventImage(
                imageUrl = event.imageUrl,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
            )
            EventDateBadge(
                day = EventDateFormatter.day(event.startsAt),
                month = EventDateFormatter.shortMonth(event.startsAt),
                modifier = Modifier.padding(10.dp),
            )
        }

        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = event.title,
                    style = AppTextStyle.TitleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Purple,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f),
                )
                Text(
                    text = EventDateFormatter.time(event.startsAt),
                    style = AppTextStyle.LabelMedium,
                    fontWeight = FontWeight.Bold,
                    color = Purple,
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(LightOrange)
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                )
            }
            Text(
                text = event.description,
                style = AppTextStyle.BodySmall,
                color = LightPurple,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            // Virtual events have no place, so the modality is shown instead
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Icon(
                    imageVector = Icons.Outlined.Place,
                    contentDescription = null,
                    tint = LightPurple,
                    modifier = Modifier.size(14.dp),
                )
                Text(
                    text = event.location ?: event.modality,
                    style = AppTextStyle.LabelMedium,
                    color = LightPurple,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview
@Composable
fun EventListCardPreview() {
    EventListCard(
        event = Event(
            id = "1",
            title = "Marcha por nuestros derechos",
            description = "Alza tu voz, no estás sola. Te esperamos para caminar juntas.",
            notes = null,
            startsAt = Instant.parse("2026-10-25T18:00:00Z"),
            endsAt = Instant.parse("2026-10-25T20:00:00Z"),
            modality = "Presencial",
            location = "Plaza de la Constitución",
            link = null,
            phone = null,
            imageUrl = null,
        ),
        onClick = {},
    )
}
