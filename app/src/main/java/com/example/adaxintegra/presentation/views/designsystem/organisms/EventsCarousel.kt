package com.example.adaxintegra.presentation.views.designsystem.organisms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.domain.model.Event
import com.example.adaxintegra.presentation.viewmodel.EventsUiState
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppButton
import com.example.adaxintegra.presentation.views.designsystem.atoms.ButtonVariant
import com.example.adaxintegra.presentation.views.designsystem.atoms.SectionTitle
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.presentation.views.designsystem.molecules.EventCard
import java.time.Instant

// E-01: events section of both homes, shows the active events of the current month
@Suppress("ktlint:standard:function-naming")
@Composable
fun EventsCarousel(
    uiState: EventsUiState,
    onSeeAllClick: () -> Unit,
    onEventClick: (String) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val errorMessage = uiState.error

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SectionTitle(
                text = "Eventos",
                modifier = Modifier.weight(1f),
            )

            TextButton(onClick = onSeeAllClick) {
                Text(
                    text = "Ver todos",
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }

        // Same height in every state, so the sections below do not jump while it loads
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(196.dp),
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                errorMessage != null && uiState.events.isEmpty() -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
                    ) {
                        Text(text = errorMessage)
                        AppButton(
                            text = "Reintentar",
                            onClick = onRetry,
                            variant = ButtonVariant.Outlined,
                        )
                    }
                }

                uiState.events.isEmpty() -> {
                    Text(text = "No hay eventos este mes.")
                }

                else -> {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(uiState.events, key = { it.id }) { event ->
                            EventCard(
                                event = event,
                                onClick = { onEventClick(event.id) },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
fun EventsCarouselPreview() {
    EventsCarousel(
        uiState = EventsUiState(
            events = listOf(
                Event(
                    id = "1",
                    title = "Marcha por nuestros derechos",
                    description = "Alza tu voz, no estás sola.",
                    notes = null,
                    startsAt = Instant.parse("2026-10-25T18:00:00Z"),
                    endsAt = Instant.parse("2026-10-25T20:00:00Z"),
                    modality = "Presencial",
                    location = "Plaza de la Constitución",
                    link = null,
                    phone = null,
                    imageUrl = null,
                ),
                Event(
                    id = "2",
                    title = "Círculo de apoyo entre mujeres",
                    description = "Escucha, comparte, acompaña.",
                    notes = null,
                    startsAt = Instant.parse("2026-10-30T17:00:00Z"),
                    endsAt = Instant.parse("2026-10-30T19:00:00Z"),
                    modality = "Virtual",
                    location = null,
                    link = "https://meet.google.com/abc",
                    phone = null,
                    imageUrl = null,
                ),
            ),
        ),
        onSeeAllClick = {},
        onEventClick = {},
        onRetry = {},
    )
}
