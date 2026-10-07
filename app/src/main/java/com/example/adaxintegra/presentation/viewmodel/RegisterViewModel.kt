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

// ViewModel to manage user registration screen logic with per-field validations
@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
) : ViewModel() {

    // Observable UI state flow initialized with default values
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState = _uiState.asStateFlow()

    // Form field updates clearing field-specific errors upon typing
    fun onNameChanged(name: String) {
        _uiState.update { it.copy(name = name, nameError = null, error = null) }
    }

    fun onLastnameChanged(lastname: String) {
        _uiState.update { it.copy(lastname = lastname, lastnameError = null, error = null) }
    }

    fun onPhoneChanged(phone: String) {
        _uiState.update { it.copy(phone = phone, phoneError = null, error = null) }
    }

    fun onEmailChanged(email: String) {
        _uiState.update { it.copy(email = email, emailError = null, error = null) }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { it.copy(password = password, passwordError = null, confirmPasswordError = null, error = null) }
    }

    fun onConfirmPasswordChanged(confirmPassword: String) {
        _uiState.update { it.copy(confirmPassword = confirmPassword, confirmPasswordError = null, error = null) }
    }

    // Toggle password visibility in the UI text fields
    fun togglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun toggleConfirmPasswordVisibility() {
        _uiState.update { it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible) }
    }

    // Opens confirmation dialog only if all per-field validations pass
    fun register() {
        val state = _uiState.value

        var hasError = false
        var nameErr: String? = null
        var lastnameErr: String? = null
        var phoneErr: String? = null
        var emailErr: String? = null
        var passwordErr: String? = null
        var confirmPasswordErr: String? = null

        // Per-field validations
        if (state.name.isBlank()) {
            nameErr = "El nombre es obligatorio"
            hasError = true
        }

        if (state.lastname.isBlank()) {
            lastnameErr = "Los apellidos son obligatorios"
            hasError = true
        }

        if (state.phone.isBlank()) {
            phoneErr = "El teléfono celular es obligatorio"
            hasError = true
        } else if (state.phone.trim().length < 10) {
            phoneErr = "El teléfono debe ser de 10 dígitos"
            hasError = true
        }

        val emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$".toRegex()
        if (state.email.isBlank()) {
            emailErr = "El correo electrónico es obligatorio"
            hasError = true
        } else if (!state.email.trim().matches(emailRegex)) {
            emailErr = "Ingresa un correo electrónico válido"
            hasError = true
        }

        if (state.password.isBlank()) {
            passwordErr = "La contraseña es obligatoria"
            hasError = true
        } else if (state.password.length < 8) {
            passwordErr = "La contraseña debe tener al menos 8 caracteres"
            hasError = true
        }

        if (state.confirmPassword.isBlank()) {
            confirmPasswordErr = "Confirma tu contraseña"
            hasError = true
        } else if (state.password != state.confirmPassword) {
            confirmPasswordErr = "Las contraseñas no coinciden"
            hasError = true
        }

        // If any field validation fails, update errors and do NOT show confirmation dialog
        if (hasError) {
            _uiState.update {
                it.copy(
                    nameError = nameErr,
                    lastnameError = lastnameErr,
                    phoneError = phoneErr,
                    emailError = emailErr,
                    passwordError = passwordErr,
                    confirmPasswordError = confirmPasswordErr,
                )
            }
            return
        }

        // All validations passed, show confirmation dialog
        _uiState.update {
            it.copy(
                showConfirmationDialog = true,
                nameError = null,
                lastnameError = null,
                phoneError = null,
                emailError = null,
                passwordError = null,
                confirmPasswordError = null,
                error = null,
            )
        }
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
                name = state.name.trim(),
                lastname = state.lastname.trim(),
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
                            val rawMsg = result.exception.message ?: result.exception.localizedMessage ?: ""
                            val errorMessage = when {
                                rawMsg.contains("User already registered", ignoreCase = true) ||
                                    rawMsg.contains("user_already_exists", ignoreCase = true) -> "El correo electrónico ya está registrado"

                                rawMsg.contains("Password should be at least", ignoreCase = true) -> "La contraseña debe tener al menos 8 caracteres"

                                result.exception is HttpException -> when (result.exception.code()) {
                                    400 -> "Datos de registro inválidos"
                                    409 -> "El correo o teléfono ya está registrado"
                                    else -> "Error del servidor (${result.exception.code()})"
                                }

                                result.exception is IOException -> "Error de conexión a internet"

                                else -> rawMsg.ifBlank { "Error al registrar la cuenta" }
                            }
                            current.copy(isLoading = false, error = errorMessage)
                        }
                    }
                }
            }
        }
    }
}
