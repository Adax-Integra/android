package com.example.adaxintegra.presentation.model

import androidx.compose.ui.graphics.ImageBitmap

// V-13: what each document card shows
sealed interface DocumentPreviewState {
    // The external user has not uploaded this document yet
    data object Empty : DocumentPreviewState

    data object Loading : DocumentPreviewState

    // One image for photos, one per page for PDFs
    data class Ready(val pages: List<ImageBitmap>) : DocumentPreviewState

    data class Error(val message: String) : DocumentPreviewState
}
