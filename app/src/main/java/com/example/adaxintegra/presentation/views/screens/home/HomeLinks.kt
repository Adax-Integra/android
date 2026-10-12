package com.example.adaxintegra.presentation.views.screens.home

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri

// NV-04: links shared by the external and internal homes
object HomeLinks {
    // Podcast link the team left, the podcast card is the only way into it
    private const val PODCAST_URL =
        "https://open.spotify.com/show/5yaZdYjbR36lrDmaJ0WhVk?si=pbLJm3ifQIuItgKcnyN08Q&utm_source=copy-link"

    // Opens the podcast in Spotify if it is installed, the browser otherwise
    fun openPodcast(context: Context) {
        runCatching {
            context.startActivity(Intent(Intent.ACTION_VIEW, PODCAST_URL.toUri()))
        }
    }
}
