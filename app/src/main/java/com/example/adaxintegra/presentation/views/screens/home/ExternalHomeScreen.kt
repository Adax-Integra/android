package com.example.adaxintegra.presentation.views.screens.home

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.adaxintegra.presentation.viewmodel.ExternalCasesViewModel
import com.example.adaxintegra.presentation.views.designsystem.atoms.SectionTitle
import com.example.adaxintegra.presentation.views.designsystem.atoms.Spacing
import com.example.adaxintegra.presentation.views.designsystem.molecules.HomeHeader
import com.example.adaxintegra.presentation.views.designsystem.molecules.PodcastCard
import com.example.adaxintegra.presentation.views.designsystem.organisms.CurrentCaseCard

//NV-01: link the team left, the podcast card is the only way into it
private const val PODCAST_URL =
    "https://open.spotify.com/show/5yaZdYjbR36lrDmaJ0WhVk?si=pbLJm3ifQIuItgKcnyN08Q&utm_source=copy-link"

// V-01: home of the external user, reached from the bottom nav bar and after signing in
@Suppress("ktlint:standard:function-naming")
@Composable
fun ExternalHomeScreen(
    onCaseClick: (String) -> Unit,
    onRegisterCaseClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ExternalCasesViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val spacing = Spacing()
    val context = LocalContext.current

    //Reloaded on every entry, so a case registered in R-02 shows up here
    LaunchedEffect(Unit) {
        viewModel.loadCases()
    }

    // NV-01: V-04 already hands the list sorted by updatedAt, so the case in progress is
    // the first one that is not closed. The comparison ignores case because the backend
    // writes "Closed" with a capital C and CaseStatusUi only knows the lowercase values
    val currentCase = uiState.cases.firstOrNull { case ->
        !"closed".equals(case.state, ignoreCase = true)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = spacing.medium),
        verticalArrangement = Arrangement.spacedBy(spacing.medium),
    ) {
        Spacer(modifier = Modifier.height(spacing.small))

        HomeHeader()

        CurrentCaseCard(
            currentCase = currentCase,
            onCaseClick = onCaseClick,
            onRegisterCaseClick = onRegisterCaseClick,
            isLoading = uiState.isLoading,
            error = uiState.error,
        )

        SectionTitle(text = "Eventos")

        //NV-01: there is no events endpoint yet, the section stays empty as in iOS
        Spacer(modifier = Modifier.height(spacing.extraLarge))

        SectionTitle(text = "Podcast")

        PodcastCard(
            onClick = {
                //Spotify if it is installed, the browser otherwise
                runCatching {
                    context.startActivity(Intent(Intent.ACTION_VIEW, PODCAST_URL.toUri()))
                }
            },
        )

        Spacer(modifier = Modifier.height(spacing.large))
    }
}
