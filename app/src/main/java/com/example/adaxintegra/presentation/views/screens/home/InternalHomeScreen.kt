package com.example.adaxintegra.presentation.views.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.adaxintegra.presentation.views.designsystem.atoms.SectionTitle
import com.example.adaxintegra.presentation.views.designsystem.atoms.Spacing
import com.example.adaxintegra.presentation.views.designsystem.molecules.HomeHeader
import com.example.adaxintegra.presentation.views.designsystem.molecules.PodcastCard

// NV-04: home of the internal and admin users, reached from the bottom nav bar and after signing in
@Suppress("ktlint:standard:function-naming")
@Composable
fun InternalHomeScreen(modifier: Modifier = Modifier) {
    val spacing = Spacing()
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = spacing.medium),
        verticalArrangement = Arrangement.spacedBy(spacing.medium),
    ) {
        Spacer(modifier = Modifier.height(spacing.small))

        // NV-04: the default message is meant for the external users, the internal users get their own
        HomeHeader(message = "Bienvenida a tu espacio de trabajo")

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

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
fun InternalHomeScreenPreview() {
    InternalHomeScreen()
}
