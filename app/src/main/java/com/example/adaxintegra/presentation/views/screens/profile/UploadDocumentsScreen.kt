package com.example.adaxintegra.presentation.views.screens.profile

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.adaxintegra.domain.model.DocumentKind
import com.example.adaxintegra.presentation.util.shareDocument
import com.example.adaxintegra.presentation.viewmodel.UploadDocumentsViewModel
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.presentation.views.designsystem.organisms.DocumentUploadSection
import com.example.adaxintegra.presentation.views.designsystem.templates.ScreenTemplate
import com.example.adaxintegra.ui.theme.AdaxIntegraTheme
import com.example.adaxintegra.ui.theme.Purple
import kotlinx.coroutines.CancellationException

// Types the "Archivos" picker shows, the backend only accepts JPEG or PNG
private val DOCUMENT_MIME_TYPES = arrayOf("image/jpeg", "image/png")

// R-06: the external user uploads, downloads or replaces her proof of identity and
// proof of address
@Suppress("ktlint:standard:function-naming")
@Composable
fun UploadDocumentsScreen(
    onBack: () -> Unit,
    viewModel: UploadDocumentsViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Document the open picker is for, it survives the rotation of the phone
    var pickingKind by rememberSaveable { mutableStateOf<DocumentKind?>(null) }

    val onPicked: (Uri?) -> Unit = { uri ->
        val kind = pickingKind
        pickingKind = null

        if (uri != null && kind != null) {
            viewModel.onDocumentPicked(kind, uri)
        }
    }

    // Android photo picker, it does not need any permission
    val galleryPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = onPicked,
    )

    val filesPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = onPicked,
    )

    LaunchedEffect(uiState.toastMessage) {
        uiState.toastMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            viewModel.onToastShown()
        }
    }

    LaunchedEffect(uiState.documentToShare) {
        uiState.documentToShare?.let { document ->
            try {
                shareDocument(context, document)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Toast.makeText(context, "No se pudo abrir el documento.", Toast.LENGTH_SHORT).show()
            }
            viewModel.onDocumentShared()
        }
    }

    UploadDocumentsContent(
        uiState = uiState,
        onBack = onBack,
        onRefresh = viewModel::refresh,
        onPickFromGallery = { kind ->
            pickingKind = kind
            galleryPicker.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
            )
        },
        onPickFromFiles = { kind ->
            pickingKind = kind
            filesPicker.launch(DOCUMENT_MIME_TYPES)
        },
        onDownload = viewModel::downloadDocument,
        onErrorDismissed = viewModel::onErrorDismissed,
    )
}

@Suppress("ktlint:standard:function-naming")
@Composable
private fun UploadDocumentsContent(
    uiState: UploadDocumentsUiState,
    onBack: () -> Unit,
    onRefresh: () -> Unit,
    onPickFromGallery: (DocumentKind) -> Unit,
    onPickFromFiles: (DocumentKind) -> Unit,
    onDownload: (DocumentKind) -> Unit,
    onErrorDismissed: () -> Unit,
) {
    ScreenTemplate(
        title = "Documentos de Identidad",
        onBack = onBack,
        isLoading = uiState.isLoading,
        error = uiState.loadError,
        isRefreshing = uiState.isRefreshing,
        onRefresh = onRefresh,
        modifier = Modifier.padding(horizontal = 16.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp),
        ) {
            DocumentKind.entries.forEach { kind ->
                DocumentUploadSection(
                    title = UploadDocumentsViewModel.title(kind),
                    note = UploadDocumentsViewModel.note(kind),
                    fileName = UploadDocumentsViewModel.fileName(kind),
                    isUploaded = kind in uiState.uploaded,
                    isUploading = kind in uiState.uploading,
                    isDownloading = kind in uiState.downloading,
                    onPickFromGallery = { onPickFromGallery(kind) },
                    onPickFromFiles = { onPickFromFiles(kind) },
                    onDownload = { onDownload(kind) },
                )
            }
        }
    }

    uiState.errorMessage?.let { message ->
        AlertDialog(
            onDismissRequest = onErrorDismissed,
            title = {
                Text(
                    text = "Aviso",
                    style = AppTextStyle.TitleMedium,
                    fontWeight = FontWeight.Bold,
                )
            },
            text = { Text(text = message) },
            confirmButton = {
                TextButton(onClick = onErrorDismissed) {
                    Text(
                        text = "Aceptar",
                        color = Purple,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            },
        )
    }
}

// Preview without using a backend or personal documents
@Suppress("ktlint:standard:function-naming")
@Preview(
    showBackground = true,
    widthDp = 360,
    heightDp = 804,
)
@Composable
private fun UploadDocumentsScreenPreview() {
    AdaxIntegraTheme(dynamicColor = false) {
        UploadDocumentsContent(
            uiState = UploadDocumentsUiState(
                isLoading = false,
                uploaded = setOf(DocumentKind.PROOF_OF_ADDRESS),
            ),
            onBack = {},
            onRefresh = {},
            onPickFromGallery = {},
            onPickFromFiles = {},
            onDownload = {},
            onErrorDismissed = {},
        )
    }
}
