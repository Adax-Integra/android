package com.example.adaxintegra.presentation.views.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.adaxintegra.domain.entities.Case
import com.example.adaxintegra.presentation.viewmodel.InternalHomeUiState
import com.example.adaxintegra.presentation.viewmodel.InternalHomeViewModel
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppButton
import com.example.adaxintegra.presentation.views.designsystem.atoms.ButtonVariant
import com.example.adaxintegra.presentation.views.designsystem.atoms.SectionTitle
import com.example.adaxintegra.presentation.views.designsystem.atoms.Spacing
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.presentation.views.designsystem.molecules.CaseCard
import com.example.adaxintegra.presentation.views.designsystem.molecules.HomeHeader
import com.example.adaxintegra.presentation.views.designsystem.molecules.PodcastCard

// NV-04: home of the internal and admin users, reached from the bottom nav bar and after signing in
@Suppress("ktlint:standard:function-naming")
@Composable
fun InternalHomeScreen(
    onCaseClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InternalHomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Reloaded on every entry, so a case updated in its detail leaves the list
    LaunchedEffect(Unit) {
        viewModel.loadStaleCases()
    }

    InternalHomeContent(
        uiState = uiState,
        onCaseClick = onCaseClick,
        onRetry = viewModel::loadStaleCases,
        onRefresh = viewModel::refresh,
        modifier = modifier,
    )
}

// NV-04: draws the internal home, separated from the view model so it can be previewed
@Suppress("ktlint:standard:function-naming")
@Composable
fun InternalHomeContent(
    uiState: InternalHomeUiState,
    onCaseClick: (String) -> Unit,
    onRetry: () -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val spacing = Spacing()
    val context = LocalContext.current
    val errorMessage = uiState.error

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

            // NV-04: the default message is meant for the external users, the internal users get their own
            HomeHeader(message = "Bienvenida a tu espacio de trabajo")

            SectionTitle(text = "Expedientes sin modificar")

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

                uiState.staleCases.isEmpty() -> {
                    Text(
                        text = "No hay expedientes abiertos pendientes de revisión.",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                    )
                }

                else -> {
                    // NV-04: the list has at most 5 cases, so a simple loop is enough inside the scroll
                    uiState.staleCases.forEach { case ->
                        CaseCard(
                            case = case,
                            modifier = Modifier.clickable {
                                onCaseClick(case.caseId)
                            },
                        )
                    }
                }
            }

            SectionTitle(text = "Eventos")

            // NV-04: events are implemented in another user story, the section stays empty for now
            Spacer(modifier = Modifier.height(spacing.extraLarge))

            SectionTitle(text = "Podcast")

            PodcastCard(
                onClick = { HomeLinks.openPodcast(context) },
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
            staleCases = Case.getMockData().take(2),
        ),
        onCaseClick = {},
        onRetry = {},
        onRefresh = {},
    )
}
