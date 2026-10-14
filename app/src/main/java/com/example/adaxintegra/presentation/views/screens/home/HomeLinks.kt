package com.example.adaxintegra.presentation.views.screens.home

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri

// NV-01 / NV-04: links shared by the external and internal homes
object HomeLinks {
    // NV-01: link the team left, the podcast card is the only way into it
    const val PODCAST_URL =
        "https://open.spotify.com/show/5yaZdYjbR36lrDmaJ0WhVk?si=pbLJm3ifQIuItgKcnyN08Q&utm_source=copy-link"

    // NV-01: the same podcast as video
    const val YOUTUBE_URL = "https://www.youtube.com/@AdaxDigitales"

    // NV-01: links to the profiles of the organization
    const val FACEBOOK_URL = "https://www.facebook.com/ADAxDigitales/"
    const val INSTAGRAM_URL = "https://www.instagram.com/adax_digitales_a.c._/"
    const val X_URL = "https://x.com/ADAxDigitalesAC"
    const val TIKTOK_URL = "https://www.tiktok.com/@adax_digitales"

    // Opens the link in the app of the network if it is installed, the browser otherwise
    fun open(context: Context, url: String) {
        runCatching {
            context.startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
        }
    }
}
