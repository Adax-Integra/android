package com.example.adaxintegra.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adaxintegra.domain.usecases.AcceptPrivacyConsentUseCase
import com.example.adaxintegra.domain.usecases.GetCurrentPrivacyPolicyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PrivacyPolicyUiState(
    val isLoading: Boolean = false,
    val isSubmitting: Boolean = false,
    val error: String? = null,
    val policyId: String? = null,
    val documentUrl: String? = null,
    val isAcceptedSuccessfully: Boolean = false,
)

@HiltViewModel
class PrivacyPolicyViewModel @Inject constructor(
    private val getCurrentPrivacyPolicyUseCase: GetCurrentPrivacyPolicyUseCase,
    private val acceptPrivacyConsentUseCase: AcceptPrivacyConsentUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(PrivacyPolicyUiState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchCurrentPolicy()
    }

    private fun fetchCurrentPolicy() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val policyData = getCurrentPrivacyPolicyUseCase()
                android.util.Log.d("PrivacyPolicyVM", "policyData: $policyData, documentUrl: ${policyData?.documentUrl}, content: ${policyData?.content}")
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        policyId = policyData?.policyId,
                        documentUrl = policyData?.documentUrl,
                    )
                }
            } catch (e: Exception) {
                android.util.Log.e("PrivacyPolicyVM", "Error fetching policy", e)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.localizedMessage ?: "Error al cargar aviso de privacidad",
                    )
                }
            }
        }
    }

    fun acceptPolicy(onSuccess: () -> Unit) {
        val policyId = _uiState.value.policyId
        if (policyId.isNullOrBlank()) {
            _uiState.update { it.copy(error = "No se encontró el identificador del aviso de privacidad") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, error = null) }
            try {
                acceptPrivacyConsentUseCase(policyId)
                _uiState.update { it.copy(isSubmitting = false, isAcceptedSuccessfully = true) }
                onSuccess()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSubmitting = false,
                        error = e.localizedMessage ?: "Error al aceptar el aviso de privacidad",
                    )
                }
            }
        }
    }

    fun getDocumentUrl(): String? {
        val url = _uiState.value.documentUrl ?: "https://xtuuspwioiflfofbhsps.supabase.co/storage/v1/object/public/privacy-policy/version-1.1.0/Aviso_Adax_Digitales.pdf"
        android.util.Log.d("PrivacyPolicyVM", "Original documentUrl from backend: $url")
        val absoluteUrl = if (url.startsWith("http://") || url.startsWith("https://")) {
            url
        } else {
            val base = "https://dev-api.adaxdigitales.com"
            if (url.startsWith("/")) "$base$url" else "$base/$url"
        }

        return if (absoluteUrl.contains("supabase.co/storage/v1/object/")) {
            absoluteUrl
                .replace(Regex("https://[^/]+\\.supabase\\.co"), "https://xtuuspwioiflfofbhsps.supabase.co")
                .replace(Regex("/object/public/[^/]+/"), "/object/public/privacy-policy/")
                .replace(Regex("/object/sign/[^/]+/"), "/object/sign/privacy-policy/")
                .replace(Regex("/object/authenticated/[^/]+/"), "/object/authenticated/privacy-policy/")
        } else {
            absoluteUrl
        }
    }
}
