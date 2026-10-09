package com.example.adaxintegra.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adaxintegra.domain.model.UserSession
import com.example.adaxintegra.domain.usecases.CheckPrivacyConsentUseCase
import com.example.adaxintegra.domain.usecases.LogoutUseCase
import com.example.adaxintegra.domain.usecases.ObserveSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
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
) : ViewModel() {
    val session: StateFlow<UserSession?> = observeSessionUseCase()

    private val _needsPrivacyConsent = MutableStateFlow(false)
    val needsPrivacyConsent: StateFlow<Boolean> = _needsPrivacyConsent.asStateFlow()

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
        logoutUseCase()
        _needsPrivacyConsent.value = false
    }
}
