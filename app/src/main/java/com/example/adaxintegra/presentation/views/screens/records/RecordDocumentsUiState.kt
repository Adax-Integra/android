package com.example.adaxintegra.presentation.views.screens.records

import com.example.adaxintegra.presentation.model.DocumentPreviewState

data class RecordDocumentsUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null,
    val identityDocument: DocumentPreviewState = DocumentPreviewState.Empty,
    val proofOfAddress: DocumentPreviewState = DocumentPreviewState.Empty,
)
