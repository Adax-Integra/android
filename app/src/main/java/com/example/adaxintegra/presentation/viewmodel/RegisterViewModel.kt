package com.example.adaxintegra.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.usecases.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

// ViewModel to manage user registration screen logic
@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
) : ViewModel() {

    // Observable UI state flow initialized with default values
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState = _uiState.asStateFlow()

    // Form field updates
    fun onNameChanged(name: String) {
        _uiState.update { it.copy(name = name, error = null) }
    }

    fun onLastnameChanged(lastname: String) {
        _uiState.update { it.copy(lastname = lastname, error = null) }
    }

    fun onPhoneChanged(phone: String) {
        _uiState.update { it.copy(phone = phone, error = null) }
    }

    fun onEmailChanged(email: String) {
        _uiState.update { it.copy(email = email, error = null) }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { it.copy(password = password, error = null) }
    }

    fun onConfirmPasswordChanged(confirmPassword: String) {
        _uiState.update { it.copy(confirmPassword = confirmPassword, error = null) }
    }

    // Toggle password visibility in the UI text fields
    fun togglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun toggleConfirmPasswordVisibility() {
        _uiState.update { it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible) }
    }

    // Opens confirmation dialog after client validations pass
    fun register() {
        val state = _uiState.value

        // Input validations before sending request
        if (state.name.isBlank()) {
            _uiState.update { it.copy(error = "El nombre es obligatorio") }
            return
        }

        if (state.lastname.isBlank()) {
            _uiState.update { it.copy(error = "Los apellidos son obligatorios") }
            return
        }

        if (state.phone.isBlank()) {
            _uiState.update { it.copy(error = "El teléfono celular es obligatorio") }
            return
        }

        if (state.email.isBlank()) {
            _uiState.update { it.copy(error = "El correo electrónico es obligatorio") }
            return
        }

        if (state.password.length < 8) {
            _uiState.update { it.copy(error = "La contraseña debe tener al menos 8 caracteres") }
            return
        }

        if (state.password != state.confirmPassword) {
            _uiState.update { it.copy(error = "Las contraseñas no coinciden") }
            return
        }

        // Show confirmation dialog before executing API call
        _uiState.update { it.copy(showConfirmationDialog = true, error = null) }
    }

    fun onDismissDialog() {
        _uiState.update { it.copy(showConfirmationDialog = false) }
    }

    // Confirms and executes registration UseCase via coroutine flow
    fun onConfirmRegister() {
        val state = _uiState.value
        _uiState.update { it.copy(showConfirmationDialog = false) }

        viewModelScope.launch {
            registerUseCase(
                name = state.name,
                lastname = state.lastname,
                phone = state.phone.trim(),
                email = state.email.trim(),
                password = state.password,
            ).collect { result ->
                _uiState.update { current ->
                    when (result) {
                        is Result.Loading -> current.copy(isLoading = true, error = null)

                        is Result.Success -> current.copy(
                            isLoading = false,
                            isRegisterSuccess = true,
                            userRole = result.data.role,
                        )

                        is Result.Error -> {
                            val errorMessage = when (val e = result.exception) {
                                is HttpException -> when (e.code()) {
                                    400 -> "Datos de registro inválidos"
                                    409 -> "El correo o teléfono ya está registrado"
                                    else -> "Error del servidor (${e.code()})"
                                }

                                is IOException -> "Error de conexión a internet"

                                else -> e.localizedMessage ?: "Error al registrar la cuenta"
                            }
                            current.copy(isLoading = false, error = errorMessage)
                        }
                    }
                }
            }
        }
    }
}
