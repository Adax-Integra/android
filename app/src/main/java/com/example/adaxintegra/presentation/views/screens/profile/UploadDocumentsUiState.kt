package com.example.adaxintegra.presentation.views.screens.profile

import com.example.adaxintegra.domain.model.DocumentKind
import com.example.adaxintegra.presentation.model.SharedDocument

// R-06: state of "Mis documentos"
data class UploadDocumentsUiState(
    // The sections wait for the saved documents, so an uploaded one never shows as empty
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val loadError: String? = null,
    // Documents already saved in the backend
    val uploaded: Set<DocumentKind> = emptySet(),
    // Documents being sent, their box shows a spinner
    val uploading: Set<DocumentKind> = emptySet(),
    val downloading: Set<DocumentKind> = emptySet(),
    // Shown in the "Aviso" dialog
    val errorMessage: String? = null,
    // Confirms an upload with a toast, it is cleared once shown
    val toastMessage: String? = null,
    // Downloaded document waiting for the share sheet
    val documentToShare: SharedDocument? = null,
)
