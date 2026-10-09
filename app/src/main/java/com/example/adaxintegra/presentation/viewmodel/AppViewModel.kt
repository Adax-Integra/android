package com.example.adaxintegra.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adaxintegra.domain.model.UserSession
import com.example.adaxintegra.domain.usecases.CheckPrivacyConsentUseCase
import com.example.adaxintegra.domain.usecases.LogoutUseCase
import com.example.adaxintegra.domain.usecases.ObserveSessionUseCase
import com.example.adaxintegra.domain.usecases.RestoreSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    observeSessionUseCase: ObserveSessionUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val checkPrivacyConsentUseCase: CheckPrivacyConsentUseCase,
    private val restoreSessionUseCase: RestoreSessionUseCase,
) : ViewModel() {

    val session: StateFlow<UserSession?> = observeSessionUseCase()

    private val _isRestoringSession = MutableStateFlow(true)
    val isRestoringSession = _isRestoringSession.asStateFlow()

    private val _restoreError = MutableStateFlow<String?>(null)
    val restoreError = _restoreError.asStateFlow()

    private val _logoutError = MutableStateFlow<String?>(null)
    val logoutError = _logoutError.asStateFlow()

    private val _needsPrivacyConsent = MutableStateFlow(false)
    val needsPrivacyConsent: StateFlow<Boolean> = _needsPrivacyConsent.asStateFlow()

    private var isLoggingOut = false

    init {
        restoreSession()
    }

    // Navigation waits until local session restoration finishes.
    private fun restoreSession() {
        viewModelScope.launch {
            _isRestoringSession.value = true
            _restoreError.value = null

            try {
                restoreSessionUseCase()
            } catch (exception: CancellationException) {
                throw exception
            } catch (_: Exception) {
                _restoreError.value =
                    "No se pudo recuperar la sesión. Intenta nuevamente."
            } finally {
                _isRestoringSession.value = false
            }
        }
    }

    fun retryRestoreSession() {
        if (!_isRestoringSession.value) {
            restoreSession()
        }
    }

    fun checkPrivacyConsent() {
        viewModelScope.launch {
            try {
                if (session.value?.role == "external") {
                    val hasAccepted = checkPrivacyConsentUseCase()
                    _needsPrivacyConsent.value = !hasAccepted
                } else {
                    _needsPrivacyConsent.value = false
                }
            } catch (_: Exception) {
                _needsPrivacyConsent.value = false
            }
        }
    }

    fun setPrivacyConsentAccepted() {
        _needsPrivacyConsent.value = false
    }

    fun logout() {
        if (isLoggingOut) return
        isLoggingOut = true

        viewModelScope.launch {
            _logoutError.value = null

            try {
                logoutUseCase()
                _needsPrivacyConsent.value = false
            } catch (exception: CancellationException) {
                throw exception
            } catch (_: Exception) {
                _logoutError.value = "No se pudo borrar la sesión guardada. Intenta nuevamente."
            } finally {
                isLoggingOut = false
            }
        }
    }

    fun dismissLogoutError() {
        _logoutError.value = null
    }
}
