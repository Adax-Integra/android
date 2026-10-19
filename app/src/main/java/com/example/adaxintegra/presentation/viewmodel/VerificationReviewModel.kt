package com.example.adaxintegra.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.usecases.ReviewVerificationUseCase
import com.example.adaxintegra.presentation.views.designsystem.atoms.VerificationStatus
import com.example.adaxintegra.presentation.views.screens.admin.MAX_REASON_LENGTH
import com.example.adaxintegra.presentation.views.screens.admin.VerificationReviewUiState
import com.example.adaxintegra.presentation.views.screens.admin.reviewErrorMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

// B-02: staff approves or rejects the identity verification of an external user
@HiltViewModel
class VerificationReviewViewModel @Inject constructor(
    private val reviewVerificationUseCase: ReviewVerificationUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(VerificationReviewUiState())
    val uiState: StateFlow<VerificationReviewUiState> = _uiState.asStateFlow()

    // "Rechazar" only opens the reason, the decision is sent with "Confirmar rechazo"
    fun startRejecting() {
        _uiState.update { it.copy(isRejecting = true, rejectReason = "") }
    }

    fun cancelRejecting() {
        _uiState.update { it.copy(isRejecting = false) }
    }

    fun onReasonChange(value: String) {
        _uiState.update { it.copy(rejectReason = value.take(MAX_REASON_LENGTH)) }
    }

    fun approve(userId: String) {
        submit(userId, VerificationStatus.VERIFIED, reason = null)
    }

    fun reject(userId: String) {
        submit(userId, VerificationStatus.NOT_VERIFIED, _uiState.value.reasonToSend)
    }

    fun onErrorDismissed() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    private fun submit(userId: String, status: VerificationStatus, reason: String?) {
        if (_uiState.value.isSubmitting) {
            return
        }
        _uiState.update { it.copy(isSubmitting = true, errorMessage = null) }

        viewModelScope.launch {
            reviewVerificationUseCase(userId, status.value, reason).collect { result ->
                when (result) {
                    is Result.Loading -> Unit

                    is Result.Success -> _uiState.update {
                        it.copy(
                            isSubmitting = false,
                            decidedStatus = VerificationStatus.from(result.data),
                        )
                    }

                    is Result.Error -> _uiState.update {
                        val code = (result.exception as? HttpException)?.code()
                        it.copy(isSubmitting = false, errorMessage = reviewErrorMessage(code))
                    }
                }
            }
        }
    }
}
