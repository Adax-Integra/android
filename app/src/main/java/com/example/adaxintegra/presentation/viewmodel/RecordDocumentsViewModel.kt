package com.example.adaxintegra.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.model.RecordDocument
import com.example.adaxintegra.domain.usecases.DownloadRecordDocumentUseCase
import com.example.adaxintegra.domain.usecases.GetRecordDocumentsUseCase
import com.example.adaxintegra.presentation.model.DocumentPreviewState
import com.example.adaxintegra.presentation.util.DocumentPreviewRenderer
import com.example.adaxintegra.presentation.views.screens.records.RecordDocumentsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class RecordDocumentsViewModel @Inject constructor(
    private val getRecordDocumentsUseCase: GetRecordDocumentsUseCase,
    private val downloadRecordDocumentUseCase: DownloadRecordDocumentUseCase,
    private val documentPreviewRenderer: DocumentPreviewRenderer,
) : ViewModel() {
    private val _uiState = MutableStateFlow(RecordDocumentsUiState())
    val uiState: StateFlow<RecordDocumentsUiState> = _uiState.asStateFlow()

    private var userId: String? = null
    private var loadJob: Job? = null

    fun loadDocuments(userId: String) {
        // Rotating the phone does not download the same record again
        if (this.userId == userId) return
        this.userId = userId
        fetch(isRefresh = false)
    }

    // Signed URLs expire after 15 minutes, refreshing asks the backend for new ones
    fun refresh() {
        fetch(isRefresh = true)
    }

    private fun fetch(isRefresh: Boolean) {
        val id = userId ?: return
        loadJob?.cancel()

        loadJob = viewModelScope.launch {
            getRecordDocumentsUseCase(id).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.update {
                            it.copy(isLoading = !isRefresh, isRefreshing = isRefresh, error = null)
                        }
                    }

                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isRefreshing = false,
                                error = errorMessage(result.exception),
                            )
                        }
                    }

                    is Result.Success -> {
                        val documents = result.data
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isRefreshing = false,
                                error = null,
                                identityDocument = documents.identityDocument.initialState(),
                                proofOfAddress = documents.proofOfAddress.initialState(),
                            )
                        }

                        // Both files download at the same time
                        coroutineScope {
                            launch {
                                val state = loadPreview(documents.identityDocument)
                                _uiState.update { it.copy(identityDocument = state) }
                            }
                            launch {
                                val state = loadPreview(documents.proofOfAddress)
                                _uiState.update { it.copy(proofOfAddress = state) }
                            }
                        }
                    }
                }
            }
        }
    }

    private suspend fun loadPreview(document: RecordDocument?): DocumentPreviewState {
        val url = document?.url
        if ((document == null) || (url == null)) return document.initialState()

        return try {
            val bytes = downloadRecordDocumentUseCase(url)
            val pages = documentPreviewRenderer.render(bytes, document.fileType)

            if (pages.isEmpty()) {
                DocumentPreviewState.Error("No se pudo mostrar el documento")
            } else {
                DocumentPreviewState.Ready(pages)
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            val message = when (e) {
                is IOException -> "No se pudo conectar con el servidor"
                is IllegalStateException -> e.message.takeIf { !it.isNullOrBlank() }
                    ?: "No se pudo cargar el documento"
                else -> "No se pudo cargar el documento"
            }
            DocumentPreviewState.Error(message)
        }
    }

    private fun RecordDocument?.initialState(): DocumentPreviewState = when {
        this == null -> DocumentPreviewState.Empty
        url == null -> DocumentPreviewState.Error("No se pudo cargar el documento")
        else -> DocumentPreviewState.Loading
    }

    private fun errorMessage(e: Throwable): String = when (e) {
        is IllegalStateException -> e.message ?: "No se pudieron cargar los documentos"
        is HttpException -> when (e.code()) {
            401 -> "Tu sesión expiró. Vuelve a iniciar sesión."
            403 -> "No tienes permiso para ver estos documentos."
            404 -> "No se encontró el expediente."
            500, 502, 503, 504 -> "El servidor no está disponible en este momento. Inténtalo más tarde."
            else -> "No se pudieron cargar los documentos. Inténtalo de nuevo."
        }
        is IOException -> "Sin conexión a internet. Inténtalo de nuevo."
        else -> "No se pudieron cargar los documentos. Inténtalo de nuevo."
    }
}
