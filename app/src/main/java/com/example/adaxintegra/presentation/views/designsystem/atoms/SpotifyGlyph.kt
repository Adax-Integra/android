package com.example.adaxintegra.presentation.views.designsystem.atoms

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.R

//Nv-01: official Spotify icon of the podcast card
@Suppress("ktlint:standard:function-naming")
@Composable
fun SpotifyGlyph(
    modifier: Modifier = Modifier,
    size: Dp = 72.dp,
) {
    Image(
        painter = painterResource(id = R.drawable.ic_spotify),
        //Decorative: the card text already says "Spotify"
        contentDescription = null,
        modifier = modifier.size(size),
    )
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
fun SpotifyGlyphPreview() {
    SpotifyGlyph()
}
