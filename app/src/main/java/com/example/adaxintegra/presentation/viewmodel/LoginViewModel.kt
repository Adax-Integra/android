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

    fun login(onNavigateToVerify: (String) -> Unit) {
        val state = _uiState.value
        if (state.isLoading || state.isResendingEmail) return

        val targetEmail = state.email.trim()
        val password = state.password

        _uiState.update {
            it.copy(
                isLoading = true,
                error = null,
                isLoginSuccess = false,
                isEmailNotConfirmed = false,
                resendSuccessMessage = null,
            )
        }

        viewModelScope.launch {
            loginUseCase(targetEmail, password).collect { result ->
                when (result) {
                    is Result.Loading -> Unit

                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isLoginSuccess = true,
                                userId = result.data.userId,
                                userRole = result.data.role,
                            )
                        }
                    }

                    is Result.Error -> {
                        val needsVerification =
                            result.exception == AuthError.EmailNotVerified

                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = result.exception.toAuthMessage(),
                                isEmailNotConfirmed = needsVerification,
                            )
                        }

                        if (needsVerification) {
                            resendVerificationEmail(
                                email = targetEmail,
                                onNavigateToVerify = onNavigateToVerify,
                            )
                        }
                    }
                }
            }
        }
    }

    // G-09: asks for a new code and opens the code screen.
    // Navigation runs outside _uiState.update, whose block may run more than once
    fun resendVerificationEmail(
        email: String = _uiState.value.email,
        onNavigateToVerify: (String) -> Unit = {},
    ) {
        val targetEmail = email.trim()
        val state = _uiState.value

        if (
            targetEmail.isBlank() ||
            state.isLoading ||
            state.isResendingEmail
        ) return

        _uiState.update {
            it.copy(
                isResendingEmail = true,
                error = null,
                resendSuccessMessage = null,
            )
        }

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
