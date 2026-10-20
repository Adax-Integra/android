package com.example.adaxintegra.presentation.views.designsystem.molecules

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.R
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.ui.theme.LightPurple
import com.example.adaxintegra.ui.theme.Purple
import com.example.adaxintegra.ui.theme.White

// NV-01: card that sends the user to the podcast on Spotify
@Suppress("ktlint:standard:function-naming")
@Composable
fun PodcastCard(
    onSpotifyClick: () -> Unit,
    onYouTubeClick: () -> Unit,
    modifier: Modifier = Modifier,
    title: String = "Las hadas sí existen",
    description: String = "Escucha o ve nuestro podcast.",
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(White)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = title,
            style = AppTextStyle.TitleLarge,
            fontWeight = FontWeight.Bold,
            color = Purple,
        )

        Text(
            text = description,
            style = AppTextStyle.BodyLarge,
            fontWeight = FontWeight.Normal,
            color = LightPurple,
        )

        PodcastPlatformRow(
            logo = R.drawable.ic_spotify,
            text = "Escuchar en Spotify",
            onClick = onSpotifyClick,
        )

        PodcastPlatformRow(
            logo = R.drawable.ic_youtube,
            text = "Ver en YouTube",
            onClick = onYouTubeClick,
        )
    }
}

// NV-01: one platform of the card, the whole row is the touch target
@Suppress("ktlint:standard:function-naming")
@Composable
private fun PodcastPlatformRow(
    @DrawableRes logo: Int,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(role = Role.Button, onClick = onClick)
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Image(
            painter = painterResource(id = logo),
            contentDescription = null,
            modifier = Modifier.size(28.dp),
        )

        Text(
            text = text,
            style = AppTextStyle.BodyLarge,
            fontWeight = FontWeight.SemiBold,
            color = Purple,
        )
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
fun PodcastCardPreview() {
    PodcastCard(
        onSpotifyClick = {},
        onYouTubeClick = {},
    )
}
