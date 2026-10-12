package com.example.adaxintegra.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adaxintegra.domain.common.AuthError
import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.usecases.LoginUseCase
import com.example.adaxintegra.domain.usecases.ResendVerificationEmailUseCase
import com.example.adaxintegra.presentation.mapper.toAuthMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * G-02 / G-09: login, and the path to the code screen for unverified accounts.
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val resendVerificationEmailUseCase: ResendVerificationEmailUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun onEmailChanger(email: String) {
        _uiState.update { it.copy(email = email, isEmailNotConfirmed = false, error = null) }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { it.copy(password = password, isEmailNotConfirmed = false, error = null) }
    }

    fun login() {
        viewModelScope.launch {
            loginUseCase(_uiState.value.email, _uiState.value.password).collect { result ->
                _uiState.update { state ->
                    when (result) {
                        is Result.Loading -> state.copy(
                            isLoading = true,
                            error = null,
                            isEmailNotConfirmed = false,
                            resendSuccessMessage = null,
                        )

                        is Result.Success -> state.copy(
                            isLoading = false,
                            isLoginSuccess = true,
                            userId = result.data.userId,
                            userRole = result.data.role,
                        )

                        // only an unverified account shows the "verify with code" button;
                        // a wrong password must not offer to send codes
                        is Result.Error -> state.copy(
                            isLoading = false,
                            error = result.exception.toAuthMessage(),
                            isEmailNotConfirmed = result.exception == AuthError.EmailNotVerified,
                        )
                    }
                }
            }
        }
    }

    // G-09: asks for a new code and opens the code screen.
    // Navigation runs outside _uiState.update, whose block may run more than once
    fun resendVerificationEmail(onNavigateToVerify: (String) -> Unit = {}) {
        val targetEmail = _uiState.value.email.trim()
        if (targetEmail.isBlank()) return

        viewModelScope.launch {
            resendVerificationEmailUseCase(targetEmail).collect { result ->
                when (result) {
                    is Result.Loading -> _uiState.update { it.copy(isResendingEmail = true) }

                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                isResendingEmail = false,
                                resendSuccessMessage = "Código de 6 dígitos enviado.",
                            )
                        }
                        onNavigateToVerify(targetEmail)
                    }

                    is Result.Error -> {
                        _uiState.update { it.copy(isResendingEmail = false) }
                        // a code sent less than 60 s ago is still valid: go enter it
                        if (result.exception == AuthError.ResendTooSoon) {
                            onNavigateToVerify(targetEmail)
                        } else {
                            _uiState.update { it.copy(error = result.exception.toAuthMessage()) }
                        }
                    }
                }
            }
        }
    }
}
