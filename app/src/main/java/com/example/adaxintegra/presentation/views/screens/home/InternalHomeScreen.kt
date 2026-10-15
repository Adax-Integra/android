package com.example.adaxintegra.presentation.views.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.adaxintegra.domain.entities.Record
import com.example.adaxintegra.presentation.viewmodel.InternalHomeUiState
import com.example.adaxintegra.presentation.viewmodel.InternalHomeViewModel
import com.example.adaxintegra.presentation.views.designsystem.atoms.AdaxLogo
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppButton
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.ButtonVariant
import com.example.adaxintegra.presentation.views.designsystem.atoms.SectionTitle
import com.example.adaxintegra.presentation.views.designsystem.atoms.Spacing
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.presentation.views.designsystem.molecules.PodcastCard
import com.example.adaxintegra.presentation.views.designsystem.molecules.RecordCard

// NV-04: home of the internal and admin users, reached from the bottom nav bar and after signing in
@Suppress("ktlint:standard:function-naming")
@Composable
fun InternalHomeScreen(
    onRecordClick: (String) -> Unit,
    onSeeAllRecordsClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InternalHomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Reloaded on every entry, so a record updated elsewhere moves to the top
    LaunchedEffect(Unit) {
        viewModel.loadHome()
    }

    InternalHomeContent(
        uiState = uiState,
        onRecordClick = onRecordClick,
        onSeeAllRecordsClick = onSeeAllRecordsClick,
        onRetry = viewModel::loadRecentRecords,
        onRefresh = viewModel::refresh,
        modifier = modifier,
    )
}

// NV-04: draws the internal home, separated from the view model so it can be previewed
@Suppress("ktlint:standard:function-naming")
@Composable
fun InternalHomeContent(
    uiState: InternalHomeUiState,
    onRecordClick: (String) -> Unit,
    onSeeAllRecordsClick: () -> Unit,
    onRetry: () -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val spacing = Spacing()
    val context = LocalContext.current
    val errorMessage = uiState.error

    // Shows the name when it is available, a generic greeting otherwise
    val greeting = if (uiState.userName.isNullOrBlank()) {
        "¡Hola!"
    } else {
        "¡Hola, ${uiState.userName}!"
    }

    PullToRefreshBox(
        isRefreshing = uiState.isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = spacing.medium),
            verticalArrangement = Arrangement.spacedBy(spacing.medium),
        ) {
            Spacer(modifier = Modifier.height(spacing.small))

            // NV-04: the design places the app logo at the top center
            AdaxLogo(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                size = 100.dp,
            )

            Text(
                text = greeting,
                style = AppTextStyle.HeadLineLarge,
                fontWeight = FontWeight.Bold,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                SectionTitle(
                    text = "Expedientes recientes",
                    modifier = Modifier.weight(1f),
                )

                TextButton(onClick = onSeeAllRecordsClick) {
                    Text(
                        text = "Ver todos",
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }

            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = spacing.medium),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator()
                    }
                }

                errorMessage != null -> {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(spacing.small),
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

                uiState.recentRecords.isEmpty() -> {
                    Text(
                        text = "Todavía no hay expedientes registrados.",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                    )
                }

                else -> {
                    // NV-04: the list has at most 3 records, so a simple loop is enough inside the scroll
                    uiState.recentRecords.forEach { record ->
                        RecordCard(
                            record = record,
                            onClick = { onRecordClick(record.userId) },
                        )
                    }
                }
            }

            SectionTitle(text = "Eventos")

            // NV-04: events are implemented in another user story, the section stays empty for now
            Spacer(modifier = Modifier.height(spacing.extraLarge))

            SectionTitle(text = "Podcast")

            PodcastCard(
                onSpotifyClick = { HomeLinks.open(context, HomeLinks.PODCAST_URL) },
                onYouTubeClick = { HomeLinks.open(context, HomeLinks.YOUTUBE_URL) },
            )

            Spacer(modifier = Modifier.height(spacing.large))
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
fun InternalHomeContentPreview() {
    InternalHomeContent(
        uiState = InternalHomeUiState(
            userName = "Jimena",
            recentRecords = listOf(
                Record(
                    recordId = "1",
                    userId = "user-1",
                    name = "Denisse Maldonado",
                    recordNumber = "EXP-2026-0052",
                    status = "SIN_EMPEZAR",
                    activeCasesCount = 2,
                    updatedAt = "2026-10-12T10:00:00",
                ),
                Record(
                    recordId = "2",
                    userId = "user-2",
                    name = "Sofía Méndez",
                    recordNumber = "EXP-2026-0051",
                    status = "EN_REVISION",
                    activeCasesCount = 0,
                    updatedAt = "2026-10-11T09:30:00",
                ),
            ),
        ),
        onRecordClick = {},
        onSeeAllRecordsClick = {},
        onRetry = {},
        onRefresh = {},
    )
}
