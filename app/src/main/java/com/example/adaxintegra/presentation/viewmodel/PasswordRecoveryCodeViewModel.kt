package com.example.adaxintegra.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adaxintegra.domain.usecases.SendRecoveryEmailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.usecases.ValidateRecoveryCodeUseCase
import com.example.adaxintegra.presentation.mapper.toAuthMessage
import kotlinx.coroutines.delay

@HiltViewModel
class PasswordRecoveryCodeViewModel @Inject constructor(
    private val validateRecoveryCodeUseCase: ValidateRecoveryCodeUseCase,
    private val sendRecoveryEmailUseCase: SendRecoveryEmailUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(PasswordRecoveryCodeUiState())
    val uiState: StateFlow<PasswordRecoveryCodeUiState> = _uiState.asStateFlow()

    private val _events = Channel<PasswordRecoveryCodeEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        //60 second wait starts as soon as code is sent
        startCooldown()
    }

    fun setEmail(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    fun onOtpCodeChanged(code: String) {
        //maximum 6 digits
        if (code.length <= 6 && code.all { it.isDigit() }) {
            _uiState.update { it.copy(otpCode = code, errorMessage = null) }
        }
    }

    //backend will check code
    fun onContinueClick() {
        val state = _uiState.value

        if (state.otpCode.length != 6) {
            _uiState.update {
                it.copy(
                    errorMessage = "Ingresa el código completo de 6 dígitos."
                )
            }
            return
        }

        //add validation state
        if (state.isValidating) return

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isValidating = true,
                    errorMessage = null,
                    successMessage = null
                )
            }

            when (
                val result = validateRecoveryCodeUseCase(
                    email = state.email,
                    code = state.otpCode
                )
            ) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(isValidating = false)
                    }

                    _events.send(
                        PasswordRecoveryCodeEvent.CodeEntered(
                            email = state.email,
                            code = state.otpCode
                        )
                    )
                }

                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isValidating = false,
                            errorMessage = result.exception.toAuthMessage()
                        )
                    }
                }

                is Result.Loading -> Unit
            }
        }
    }

    fun onResendClick() {
        val state = _uiState.value

        //button will be disabled
        if (state.isResending || state.cooldownSeconds > 0 || state.sendCount >= state.maxSends) {
            return
        }

        _uiState.update {
            it.copy(
                isResending = true,
                errorMessage = null,
                successMessage = null)
        }

        viewModelScope.launch {
            when (val result = sendRecoveryEmailUseCase(state.email)) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            isResending = false,
                            sendCount = it.sendCount + 1,
                            otpCode = "",
                            successMessage = "Se envió un nuevo código.",
                        )
                    }
                    startCooldown()
                }

                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isResending = false,
                            errorMessage = result.exception.toAuthMessage(),
                        )
                    }
                }

                is Result.Loading -> Unit
            }
        }
    }

    //countdown timer
    private fun startCooldown() {
        viewModelScope.launch {
            _uiState.update { it.copy(cooldownSeconds = 60) }
            while (_uiState.value.cooldownSeconds > 0) {
                delay(1000L)
                _uiState.update { it.copy(cooldownSeconds = it.cooldownSeconds - 1) }
            }
        }
    }
}
