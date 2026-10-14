
package com.example.adaxintegra.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adaxintegra.domain.model.UploadDocumentFile
import com.example.adaxintegra.domain.repository.UploadDocumentsRepository
import com.example.adaxintegra.presentation.views.screens.profile.UploadDocumentsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class UploadDocumentsViewModel @Inject constructor(
    private val repository: UploadDocumentsRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UploadDocumentsUiState())
    val uiState: StateFlow<UploadDocumentsUiState> = _uiState.asStateFlow()

    fun uploadDocuments(
        identityDocument: UploadDocumentFile?,
        proofOfAddress: UploadDocumentFile?,
    ) {
        if (_uiState.value.isLoading) return

        if (identityDocument == null && proofOfAddress == null) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Selecciona al menos un documento",
            )
            return
        }

        _uiState.value = _uiState.value.copy(
            isLoading = true,
            isSuccess = false,
            errorMessage = null,
        )

        viewModelScope.launch {
            try {
                repository.uploadDocuments(
                    identityDocument = identityDocument,
                    proofOfAddress = proofOfAddress,
                )

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isSuccess = true,
                    errorMessage = null,
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isSuccess = false,
                    errorMessage = "No se pudieron subir los documentos. Intenta de nuevo.",
                )
            }
        }
    }
}
