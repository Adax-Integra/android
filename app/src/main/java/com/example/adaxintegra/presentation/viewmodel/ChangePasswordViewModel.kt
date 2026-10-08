package com.example.adaxintegra.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adaxintegra.domain.repository.AuthRepository
import com.example.adaxintegra.presentation.views.screens.profile.ChangePasswordUiState
import com.example.adaxintegra.presentation.views.screens.profile.PasswordRules
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChangePasswordUiState())
    val uiState: StateFlow<ChangePasswordUiState> = _uiState.asStateFlow()

    fun onCurrentPasswordChange(value: String) {
        _uiState.update {
            val newState = it.copy(currentPassword = value, errorMessage = null)
            newState.copy(rules = evaluateRules(newState))
        }
    }

    fun onNewPasswordChange(value: String) {
        _uiState.update {
            val newState = it.copy(newPassword = value, errorMessage = null)
            newState.copy(rules = evaluateRules(newState))
        }
    }

    fun onConfirmPasswordChange(value: String) {
        _uiState.update {
            val newState = it.copy(confirmPassword = value, errorMessage = null)
            newState.copy(rules = evaluateRules(newState))
        }
    }

    fun toggleCurrentPasswordVisibility() {
        _uiState.update { it.copy(isCurrentPasswordVisible = !it.isCurrentPasswordVisible) }
    }

    fun toggleNewPasswordVisibility() {
        _uiState.update { it.copy(isNewPasswordVisible = !it.isNewPasswordVisible) }
    }

    fun toggleConfirmPasswordVisibility() {
        _uiState.update { it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible) }
    }

    fun submitChangePassword(onSuccess: () -> Unit) {
        val state = _uiState.value
        if (!state.rules.isValid || state.currentPassword.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Por favor verifica los requisitos de la contraseña.") }
            return
        }

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            // Simula el procesamiento del cambio de contraseña
            delay(800)
            _uiState.update { it.copy(isLoading = false) }
            onSuccess()
        }
    }

    private fun evaluateRules(state: ChangePasswordUiState): PasswordRules {
        val newPass = state.newPassword
        val currPass = state.currentPassword
        val confPass = state.confirmPassword

        val hasValidLength = newPass.length in 8..24
        val hasUpperAndLower = newPass.any { it.isUpperCase() } && newPass.any { it.isLowerCase() }
        val hasNumber = newPass.any { it.isDigit() }
        val isDifferentFromCurrent = currPass.isNotBlank() && newPass.isNotBlank() && newPass != currPass
        val doPasswordsMatch = newPass.isNotBlank() && confPass.isNotBlank() && newPass == confPass

        val hasNoSpecialAccents = if (newPass.isBlank()) false else {
            !Regex("[áéíóúÁÉÍÓÚñÑçÇæÆäëïöüÄËÏÖÜ]").containsMatchIn(newPass)
        }

        return PasswordRules(
            hasValidLength = hasValidLength,
            hasUpperAndLower = hasUpperAndLower,
            hasNumber = hasNumber,
            isDifferentFromCurrent = isDifferentFromCurrent,
            doPasswordsMatch = doPasswordsMatch,
            hasNoSpecialAccents = hasNoSpecialAccents,
        )
    }
}
