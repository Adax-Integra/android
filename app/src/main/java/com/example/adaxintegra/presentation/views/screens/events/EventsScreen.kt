package com.example.adaxintegra.presentation.views.screens.events

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.adaxintegra.domain.model.Event
import com.example.adaxintegra.domain.model.EventScope
import com.example.adaxintegra.presentation.viewmodel.EventsUiState
import com.example.adaxintegra.presentation.viewmodel.EventsViewModel
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppButton
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppIcon
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppIcons
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.ButtonVariant
import com.example.adaxintegra.presentation.views.designsystem.atoms.IconSize
import com.example.adaxintegra.presentation.views.designsystem.atoms.Spacing
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.presentation.views.designsystem.molecules.EventListCard
import com.example.adaxintegra.presentation.views.designsystem.templates.ScreenTemplate
import com.example.adaxintegra.ui.theme.IconGrey
import java.time.Instant

// E-01: every active event of the association, opened from "Ver todos" in both homes
@Suppress("ktlint:standard:function-naming")
@Composable
fun EventsScreen(
    onBack: () -> Unit,
    onEventClick: (String) -> Unit,
    onCreateEvent: () -> Unit = {},
    viewModel: EventsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Reloaded on every entry, so an event created or edited elsewhere shows up
    LaunchedEffect(Unit) {
        viewModel.loadEvents(EventScope.ALL)
    }

    EventsContent(
        uiState = uiState,
        onBack = onBack,
        onEventClick = onEventClick,
        onCreateEvent = onCreateEvent,
        onRetry = { viewModel.loadEvents(EventScope.ALL) },
        onRefresh = viewModel::refresh,
    )
}

// E-01: draws the events screen, separated from the view model so it can be previewed
@Suppress("ktlint:standard:function-naming")
@Composable
fun EventsContent(
    uiState: EventsUiState,
    onBack: () -> Unit,
    onEventClick: (String) -> Unit,
    onCreateEvent: () -> Unit = {},
    onRetry: () -> Unit,
    onRefresh: () -> Unit,
) {
    val spacing = Spacing()
    val errorMessage = uiState.error

    ScreenTemplate(
        title = "Eventos",
        subtitle = "Actividades de la asociación",
        onBack = onBack,
        isLoading = uiState.isLoading,
        isRefreshing = uiState.isRefreshing,
        onRefresh = onRefresh,
        modifier = Modifier.padding(horizontal = spacing.medium),
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateEvent,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
            ) {
                AppIcon(
                    imageVector = AppIcons.Add,
                    contentDescription = "Agregar evento",
                    size = IconSize.LargeIcon,
                    tint = Color.White,
                )
            }
        },
    ) {
        when {
            errorMessage != null && uiState.events.isEmpty() -> {
                // Scrollable so the pull gesture also works on this state
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(
                        spacing.medium,
                        Alignment.CenterVertically,
                    ),
                ) {
                    Text(
                        text = errorMessage,
                        textAlign = TextAlign.Center,
                    )
                    AppButton(
                        text = "Reintentar",
                        onClick = onRetry,
                        variant = ButtonVariant.Outlined,
                    )
                }
            }

            else -> {
                if (uiState.events.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "No hay eventos por ahora.",
                            style = AppTextStyle.BodyMedium,
                            color = IconGrey,
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(spacing.medium),
                        contentPadding = PaddingValues(
                            top = spacing.small,
                            bottom = spacing.medium,
                        ),
                    ) {
                        items(uiState.events, key = { it.id }) { event ->
                            EventListCard(
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
fun EventsContentPreview() {
    EventsContent(
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
                    startsAt = Instant.parse("2026-11-12T17:00:00Z"),
                    endsAt = Instant.parse("2026-11-12T19:00:00Z"),
                    modality = "Virtual",
                    location = null,
                    link = "https://meet.google.com/abc",
                    phone = null,
                    imageUrl = null,
                ),
            ),
        ),
        onBack = {},
        onEventClick = {},
        onCreateEvent = {},
        onRetry = {},
        onRefresh = {},
    )
}
