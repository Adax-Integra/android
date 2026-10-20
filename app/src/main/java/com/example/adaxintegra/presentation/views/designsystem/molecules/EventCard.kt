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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.adaxintegra.domain.model.Event
import com.example.adaxintegra.presentation.util.EventDateFormatter
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.EventDateBadge
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.ui.theme.LightPurple
import com.example.adaxintegra.ui.theme.Purple
import com.example.adaxintegra.ui.theme.PurplePink
import com.example.adaxintegra.ui.theme.White
import java.time.Instant

// E-01: event card of the home carousel, same size as the iOS one
@Suppress("ktlint:standard:function-naming")
@Composable
fun EventCard(
    event: Event,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .width(160.dp)
            .height(192.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(White)
            .clickable(role = Role.Button, onClick = onClick),
    ) {
        Box {
            EventImage(
                imageUrl = event.imageUrl,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
            )
            EventDateBadge(
                day = EventDateFormatter.day(event.startsAt, event.endsAt),
                month = EventDateFormatter.shortMonth(event.startsAt),
                modifier = Modifier.padding(8.dp),
            )
        }

        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(
                text = event.title,
                style = AppTextStyle.BodyMedium,
                fontWeight = FontWeight.Bold,
                color = Purple,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = event.description,
                style = AppTextStyle.LabelSmall,
                color = LightPurple,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            // Virtual events have no place, so the modality is shown instead
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Icon(
                    imageVector = Icons.Outlined.Place,
                    contentDescription = null,
                    tint = LightPurple,
                    modifier = Modifier.size(12.dp),
                )
                Text(
                    text = event.location ?: event.modality,
                    style = AppTextStyle.LabelSmall,
                    color = LightPurple,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Text(
                text = EventDateFormatter.dateRange(event.startsAt, event.endsAt),
                style = AppTextStyle.LabelSmall,
                color = LightPurple,
                maxLines = 1,
            )
        }
    }
}

// E-01: image of the event, or a calendar icon when it has none
@Suppress("ktlint:standard:function-naming")
@Composable
fun EventImage(
    imageUrl: String?,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.background(PurplePink),
        contentAlignment = Alignment.Center,
    ) {
        if (imageUrl == null) {
            Icon(
                imageVector = Icons.Outlined.Event,
                contentDescription = null,
                tint = White,
                modifier = Modifier.size(32.dp),
            )
        } else {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize(),
            )
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview
@Composable
fun EventCardPreview() {
    EventCard(
        event = Event(
            id = "1",
            title = "Marcha por nuestros derechos",
            description = "Alza tu voz, no estás sola.",
            notes = null,
            startsAt = Instant.parse("2026-10-25T18:00:00Z"),
            endsAt = Instant.parse("2026-10-28T20:00:00Z"),
            modality = "Presencial",
            location = "Plaza de la Constitución",
            link = null,
            phone = null,
            imageUrl = null,
        ),
        onClick = {},
    )
}
