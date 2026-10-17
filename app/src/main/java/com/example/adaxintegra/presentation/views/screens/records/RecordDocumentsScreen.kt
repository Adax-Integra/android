package com.example.adaxintegra.presentation.views.screens.records

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.adaxintegra.presentation.model.DocumentPreviewState
import com.example.adaxintegra.presentation.viewmodel.RecordDocumentsViewModel
import com.example.adaxintegra.presentation.views.designsystem.molecules.DocumentPreviewCard
import com.example.adaxintegra.presentation.views.designsystem.templates.ScreenTemplate
import com.example.adaxintegra.ui.theme.AdaxIntegraTheme

// V-13: documents of the external user inside her record (read only)
@Suppress("ktlint:standard:function-naming")
@Composable
fun RecordDocumentsScreen(
    userId: String,
    onBackClick: () -> Unit = {},
    viewModel: RecordDocumentsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(userId) {
        viewModel.loadDocuments(userId)
    }

    RecordDocumentsContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onRefresh = viewModel::refresh,
    )
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun RecordDocumentsContent(
    uiState: RecordDocumentsUiState,
    onBackClick: () -> Unit = {},
    onRefresh: () -> Unit = {},
) {
    ScreenTemplate(
        title = "Documentos",
        subtitle = "INE y comprobante de domicilio",
        onBack = onBackClick,
        isLoading = uiState.isLoading,
        error = uiState.error,
        isRefreshing = uiState.isRefreshing,
        onRefresh = onRefresh,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            DocumentPreviewCard(
                title = "INE",
                state = uiState.identityDocument,
                onRetry = onRefresh,
            )

            DocumentPreviewCard(
                title = "Comprobante de domicilio",
                state = uiState.proofOfAddress,
                onRetry = onRefresh,
            )
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
private fun RecordDocumentsPreview() {
    AdaxIntegraTheme(dynamicColor = false) {
        RecordDocumentsContent(
            uiState = RecordDocumentsUiState(
                identityDocument = DocumentPreviewState.Loading,
                proofOfAddress = DocumentPreviewState.Empty,
            ),
        )
    }
}
