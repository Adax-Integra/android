package com.example.adaxintegra.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.usecases.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

// login's screen brain...
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun onEmailChanger(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    fun login() {
        viewModelScope.launch {
            loginUseCase(_uiState.value.email, _uiState.value.password).collect { result ->
                _uiState.update { state ->
                    when (result) {
                        is Result.Loading -> state.copy(isLoading = true, error = null)
                        is Result.Success -> state.copy(
                            isLoading = false,
                            isLoginSuccess = true,
                            userId = result.data.userId,
                            userRole = result.data.role,
                        )
                        is Result.Error -> {
                            val errorMessage = when (val e = result.exception) {
                                is HttpException -> when (e.code()) {
                                    401 -> "Credenciales incorrectas"
                                    404 -> "Endpoint no encontrado (404)"
                                    else -> "Error del servidor (${e.code()})"
                                }
                                is IOException -> "Error de conexión: ${e.localizedMessage}"
                                else -> e.localizedMessage ?: "Error desconocido"
                            }
                            state.copy(isLoading = false, error = errorMessage)
                        }
                    }
                }
            }
        }
    }
}
