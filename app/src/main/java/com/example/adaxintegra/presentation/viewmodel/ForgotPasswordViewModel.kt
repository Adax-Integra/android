package com.example.adaxintegra.presentation.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adaxintegra.domain.common.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel
@Inject constructor() :
    ViewModel() {

    private val _uiState = MutableStateFlow(ForgotPasswordUiState())
    val uiState: StateFlow<ForgotPasswordUiState> = _uiState.asStateFlow()

    fun onEmailChange(email: String) {
        _uiState.update {
            it.copy(
                email = email,
                emailError = null,
                successMessage = null,
            )
        }
    }

    fun onSendEmailClick() {
        val email = _uiState.value.email.trim()

        if (email.isBlank()) {
            _uiState.update {
                it.copy(
                    emailError = "Ingresa tu correo electrónico.",
                    successMessage = null,
                )
            }
            return
        }

        //validate input is an email address
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _uiState.update {
                it.copy(
                    emailError = "Ingresa un correo electrónico válido.",
                    successMessage = null,
                )
            }
            return
        }

        //temporary success message.
        _uiState.update {
            it.copy(
                email = email,
                emailError = null,
                successMessage =
                    "Se ha enviado un correo para recuperar tu contraseña.",
            )
        }
    }
}
