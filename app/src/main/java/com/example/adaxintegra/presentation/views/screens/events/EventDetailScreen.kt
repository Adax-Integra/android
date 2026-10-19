package com.example.adaxintegra.presentation.views.screens.events

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.adaxintegra.domain.model.Event
import com.example.adaxintegra.presentation.util.EventDateFormatter
import com.example.adaxintegra.presentation.viewmodel.EventDetailUiState
import com.example.adaxintegra.presentation.viewmodel.EventDetailViewModel
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppButton
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.ButtonVariant
import com.example.adaxintegra.presentation.views.designsystem.atoms.Spacing
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.presentation.views.designsystem.molecules.EventDetailSection
import com.example.adaxintegra.presentation.views.designsystem.molecules.EventImage
import com.example.adaxintegra.presentation.views.designsystem.templates.ScreenTemplate
import com.example.adaxintegra.presentation.views.screens.home.HomeLinks
import com.example.adaxintegra.ui.theme.Purple
import com.example.adaxintegra.ui.theme.White
import java.time.Instant

// E-01: detail of one event, opened from the home carousel or the events screen
@Suppress("ktlint:standard:function-naming")
@Composable
fun EventDetailScreen(
    eventId: String,
    onBack: () -> Unit,
    viewModel: EventDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(eventId) {
        viewModel.loadEvent(eventId)
    }

    EventDetailContent(
        uiState = uiState,
        onBack = onBack,
        onRetry = viewModel::retry,
    )
}

// E-01: draws the event detail, separated from the view model so it can be previewed
@Suppress("ktlint:standard:function-naming")
@Composable
fun EventDetailContent(
    uiState: EventDetailUiState,
    onBack: () -> Unit,
    onRetry: () -> Unit,
) {
    val spacing = Spacing()
    val event = uiState.event
    val errorMessage = uiState.error

    ScreenTemplate(
        title = "Detalle del evento",
        onBack = onBack,
        isLoading = uiState.isLoading,
        modifier = Modifier.padding(horizontal = spacing.medium),
    ) {
        when {
            event != null -> EventDetailBody(event = event)

            errorMessage != null -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(
                        spacing.medium,
                        Alignment.CenterVertically
                    ),
                ) {
                    Text(
                        text = errorMessage,
                        textAlign = TextAlign.Center,
                    )
                    // An unavailable event cannot be loaded again, so the button goes back instead
                    AppButton(
                        text = if (uiState.isUnavailable) "Regresar" else "Reintentar",
                        onClick = if (uiState.isUnavailable) onBack else onRetry,
                        variant = ButtonVariant.Outlined,
                    )
                }
            }
        }
    }
}

// E-01: image, title and the data of the event. Sections without data are not shown
@Suppress("ktlint:standard:function-naming")
@Composable
private fun EventDetailBody(event: Event) {
    val spacing = Spacing()
    val context = LocalContext.current
    val link = event.link
    val phone = event.phone
    val notes = event.notes

    // Virtual events have no place, so only the modality is shown
    val place = event.location?.let { "$it (${event.modality.lowercase()})" } ?: event.modality

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = spacing.medium),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(White),
        ) {
            EventImage(
                imageUrl = event.imageUrl,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
            )

            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                Text(
                    text = event.title,
                    style = AppTextStyle.TitleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Purple,
                )

                EventDetailSection(
                    title = "Fecha y hora",
                    text = "${EventDateFormatter.detailDate(event.startsAt)} · " +
                        EventDateFormatter.timeRange(event.startsAt, event.endsAt),
                    icon = Icons.Outlined.Schedule,
                )

                EventDetailSection(
                    title = "Lugar / modalidad",
                    text = place,
                    icon = Icons.Outlined.Place,
                )

                // The link opens in the browser or in the app of the meeting
                if (link != null) {
                    EventDetailSection(
                        title = "Enlace",
                        text = link,
                        icon = Icons.Outlined.Link,
                        onClick = { HomeLinks.open(context, link) },
                    )
                }

                // The phone opens the dialer with the number already written
                if (phone != null) {
                    EventDetailSection(
                        title = "Teléfono",
                        text = phone,
                        icon = Icons.Outlined.Call,
                        onClick = { HomeLinks.open(context, "tel:$phone") },
                    )
                }

                EventDetailSection(
                    title = "Descripción",
                    text = event.description,
                )

                if (notes != null) {
                    EventDetailSection(
                        title = "Notas",
                        text = notes,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(spacing.medium))
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
fun EventDetailContentPreview() {
    EventDetailContent(
        uiState = EventDetailUiState(
            event = Event(
                id = "1",
                title = "Reunión de equipo - Seguimiento de casos",
                description = "Revisión semanal de avance de casos activos.",
                notes = "Confirmar asistencia con al menos un día de anticipación.",
                startsAt = Instant.parse("2026-11-03T16:00:00Z"),
                endsAt = Instant.parse("2026-11-03T17:30:00Z"),
                modality = "Presencial",
                location = "Oficinas ADAX",
                link = "https://meet.google.com/abc",
                phone = "+524421234567",
                imageUrl = null,
            ),
        ),
        onBack = {},
        onRetry = {},
    )
}
