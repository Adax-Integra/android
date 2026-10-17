package com.example.adaxintegra.presentation.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adaxintegra.domain.model.DocumentKind
import com.example.adaxintegra.domain.model.RecordDocument
import com.example.adaxintegra.domain.model.RecordDocumentType
import com.example.adaxintegra.domain.model.RecordDocuments
import com.example.adaxintegra.domain.usecases.DownloadMyDocumentUseCase
import com.example.adaxintegra.domain.usecases.GetMyDocumentsUseCase
import com.example.adaxintegra.domain.usecases.UploadDocumentUseCase
import com.example.adaxintegra.presentation.model.SharedDocument
import com.example.adaxintegra.presentation.util.DocumentImageEncoder
import com.example.adaxintegra.presentation.views.screens.profile.UploadDocumentsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

// R-06: shows, uploads and downloads the identity documents of the external user
@HiltViewModel
class UploadDocumentsViewModel @Inject constructor(
    private val getMyDocumentsUseCase: GetMyDocumentsUseCase,
    private val uploadDocumentUseCase: UploadDocumentUseCase,
    private val downloadMyDocumentUseCase: DownloadMyDocumentUseCase,
    private val documentImageEncoder: DocumentImageEncoder,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UploadDocumentsUiState())
    val uiState: StateFlow<UploadDocumentsUiState> = _uiState.asStateFlow()

    // Saved documents with their signed URLs, they expire after 15 minutes
    private val savedDocuments = mutableMapOf<DocumentKind, RecordDocument>()

    init {
        loadDocuments(isRefresh = false)
    }

    fun refresh() {
        loadDocuments(isRefresh = true)
    }

    private fun loadDocuments(isRefresh: Boolean) {
        _uiState.update {
            it.copy(
                isLoading = !isRefresh,
                isRefreshing = isRefresh,
                loadError = null,
            )
        }

        viewModelScope.launch {
            try {
                applyDocuments(getMyDocumentsUseCase())
                _uiState.update { it.copy(isLoading = false, isRefreshing = false) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isRefreshing = false,
                        loadError = messageFor(
                            e,
                            fallback = "No se pudieron cargar tus documentos. Intenta de nuevo.",
                        ),
                    )
                }
            }
        }
    }

    // Called when the user picks a picture, it is uploaded right away
    // Uploading again replaces the saved document
    fun onDocumentPicked(
        kind: DocumentKind,
        uri: Uri,
    ) {
        val state = _uiState.value
        if (kind in state.uploading) return

        val isReplacing = kind in state.uploaded
        _uiState.update { it.copy(uploading = it.uploading + kind) }

        viewModelScope.launch {
            try {
                val file = documentImageEncoder.encode(uri, fileName(kind))
                uploadDocumentUseCase(kind, file)

                // The new signed URL is asked again when the user downloads it
                savedDocuments.remove(kind)
                _uiState.update {
                    it.copy(
                        uploaded = it.uploaded + kind,
                        toastMessage = "${title(kind)} ${if (isReplacing) "actualizado" else "subido"}",
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        errorMessage = messageFor(
                            e,
                            fallback = "No se pudo subir el documento. Intenta de nuevo.",
                        ),
                    )
                }
            } finally {
                _uiState.update { it.copy(uploading = it.uploading - kind) }
            }
        }
    }

    fun downloadDocument(kind: DocumentKind) {
        if (kind in _uiState.value.downloading) return

        _uiState.update { it.copy(downloading = it.downloading + kind) }

        viewModelScope.launch {
            try {
                val (document, bytes) = downloadFile(kind)
                _uiState.update {
                    it.copy(documentToShare = toSharedDocument(kind, document, bytes))
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                logFailure(e)
                _uiState.update {
                    it.copy(errorMessage = "No se pudo descargar el documento. Intenta de nuevo")
                }
            } finally {
                _uiState.update { it.copy(downloading = it.downloading - kind) }
            }
        }
    }

    fun onErrorDismissed() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun onToastShown() {
        _uiState.update { it.copy(toastMessage = null) }
    }

    fun onDocumentShared() {
        _uiState.update { it.copy(documentToShare = null) }
    }

    // A signed URL lasts 15 minutes, so when it fails a fresh one is asked one time
    private suspend fun downloadFile(kind: DocumentKind): Pair<RecordDocument, ByteArray> {
        val cached = savedDocuments[kind]
        val cachedUrl = cached?.url

        if (cached != null && cachedUrl != null) {
            try {
                return cached to downloadMyDocumentUseCase(cachedUrl)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                // The URL probably expired, a new one is asked below
            }
        }

        applyDocuments(getMyDocumentsUseCase())

        val document = savedDocuments[kind]
            ?: throw IllegalStateException("El documento ya no existe.")
        val url = document.url
            ?: throw IllegalStateException("No se pudo preparar el documento.")

        return document to downloadMyDocumentUseCase(url)
    }

    private fun applyDocuments(documents: RecordDocuments) {
        savedDocuments.clear()
        documents.identityDocument?.let { savedDocuments[DocumentKind.IDENTITY] = it }
        documents.proofOfAddress?.let { savedDocuments[DocumentKind.PROOF_OF_ADDRESS] = it }

        _uiState.update { it.copy(uploaded = savedDocuments.keys.toSet()) }
    }

    // The backend saves .jpg or .png. A document uploaded when PDF was allowed can still be a PDF
    private fun toSharedDocument(
        kind: DocumentKind,
        document: RecordDocument,
        bytes: ByteArray,
    ): SharedDocument {
        val (extension, mimeType) = when {
            document.fileType == RecordDocumentType.PDF -> "pdf" to "application/pdf"
            bytes.isPng() -> "png" to "image/png"
            else -> "jpg" to "image/jpeg"
        }

        return SharedDocument(
            fileName = "${fileName(kind).substringBeforeLast('.')}.$extension",
            mimeType = mimeType,
            bytes = bytes,
        )
    }

    // PNG files start with the bytes 89 50 4E 47
    private fun ByteArray.isPng(): Boolean = size >= 4 &&
        this[0] == 0x89.toByte() &&
        this[1] == 0x50.toByte() &&
        this[2] == 0x4E.toByte() &&
        this[3] == 0x47.toByte()

    // The repository and the encoder already throw the message the user should read
    private fun messageFor(
        e: Exception,
        fallback: String,
    ): String {
        logFailure(e)

        return when (e) {
            is IllegalStateException, is IllegalArgumentException -> e.message ?: fallback

            // No connection or the backend took too long
            is IOException -> "No se pudo conectar con el servidor. Intenta de nuevo."

            else -> fallback
        }
    }

    // Only the kind of error reaches Logcat, never URLs or document data
    private fun logFailure(e: Exception) {
        Log.w(TAG, "R-06 documents request failed: ${e::class.java.simpleName}")
    }

    companion object {
        private const val TAG = "UploadDocuments"

        // Title of each section, also used in the toast
        fun title(kind: DocumentKind): String = when (kind) {
            DocumentKind.IDENTITY -> "Comprobante de identidad"
            DocumentKind.PROOF_OF_ADDRESS -> "Comprobante de domicilio"
        }

        // Text of the info button
        fun note(kind: DocumentKind): String = when (kind) {
            DocumentKind.IDENTITY ->
                "Puedes usar tu INE, pasaporte o cualquier documento que compruebe tu identidad"

            DocumentKind.PROOF_OF_ADDRESS ->
                "Usa un comprobante de domicilio que contenga tu dirección de vivienda"
        }

        // The backend does not keep the original name and every picture is sent as JPEG
        fun fileName(kind: DocumentKind): String = when (kind) {
            DocumentKind.IDENTITY -> "comprobante-identidad.jpg"
            DocumentKind.PROOF_OF_ADDRESS -> "comprobante-domicilio.jpg"
        }
    }
}
