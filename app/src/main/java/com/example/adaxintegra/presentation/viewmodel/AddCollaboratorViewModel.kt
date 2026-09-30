package com.example.adaxintegra.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.model.NewCollaborator
import com.example.adaxintegra.domain.usecases.CreateCollaboratorUseCase
import com.example.adaxintegra.presentation.views.screens.admin.AddCollaboratorUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

// G-03: the admin adds a new collaborator account
@HiltViewModel
class AddCollaboratorViewModel @Inject constructor(
    private val createCollaboratorUseCase: CreateCollaboratorUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(AddCollaboratorUiState())
    val uiState: StateFlow<AddCollaboratorUiState> = _uiState.asStateFlow()

    // Opens the form empty
    fun openDialog() {
        _uiState.update { AddCollaboratorUiState(isDialogOpen = true) }
    }

    // Acceptance criteria: way to cancel the process (nothing is saved)
    fun cancel() {
        _uiState.update { AddCollaboratorUiState() }
    }

    fun onNameChange(value: String) {
        _uiState.update { it.copy(name = value, fieldErrors = it.fieldErrors - "name", generalError = null) }
    }

    fun onLastNameChange(value: String) {
        _uiState.update { it.copy(lastName = value, fieldErrors = it.fieldErrors - "lastName", generalError = null) }
    }

    fun onEmailChange(value: String) {
        _uiState.update { it.copy(email = value, fieldErrors = it.fieldErrors - "email", generalError = null) }
    }

    fun onPasswordChange(value: String) {
        _uiState.update { it.copy(password = value, fieldErrors = it.fieldErrors - "password", generalError = null) }
    }

    // Only digits, maximum 10 (same rule as the backend)
    fun onPhoneChange(value: String) {
        val digits = value.filter { it.isDigit() }.take(PHONE_LENGTH)
        _uiState.update { it.copy(phone = digits, fieldErrors = it.fieldErrors - "phone", generalError = null) }
    }

    fun save() {
        val state = _uiState.value
        val errors = validate(state)
        if (errors.isNotEmpty()) {
            _uiState.update { it.copy(fieldErrors = errors) }
            return
        }

        val collaborator = NewCollaborator(
            name = state.name.trim(),
            lastName = state.lastName.trim(),
            email = state.email.trim().lowercase(),
            password = state.password,
            phone = state.phone,
        )

        viewModelScope.launch {
            createCollaboratorUseCase(collaborator).collect { result ->
                _uiState.update { current ->
                    when (result) {
                        is Result.Loading -> current.copy(isSaving = true, generalError = null)

                        // Closes the form and shows a confirmation
                        is Result.Success -> AddCollaboratorUiState(
                            successMessage = "Colaboradora ${result.data.name} agregada correctamente.",
                        )

                        is Result.Error -> errorState(current, result.exception)
                    }
                }
            }
        }
    }

    // Same rules as the backend, so most errors are shown before sending
    private fun validate(state: AddCollaboratorUiState): Map<String, String> {
        val errors = mutableMapOf<String, String>()

        if (state.name.isBlank()) {
            errors["name"] = "El nombre es obligatorio."
        }
        if (state.lastName.isBlank()) {
            errors["lastName"] = "Los apellidos son obligatorios."
        }
        if (state.email.isBlank()) {
            errors["email"] = "El correo es obligatorio."
        } else if (!EMAIL_REGEX.matches(state.email.trim())) {
            errors["email"] = "Ingresa un correo válido."
        }
        if (state.password.length < MIN_PASSWORD_LENGTH) {
            errors["password"] = "La contraseña debe tener al menos 8 caracteres."
        }
        if (state.phone.length != PHONE_LENGTH) {
            errors["phone"] = "El teléfono debe tener 10 dígitos."
        }

        return errors
    }

    private fun errorState(state: AddCollaboratorUiState, e: Throwable): AddCollaboratorUiState {
        // Acceptance criteria: an existing account cannot be registered again
        if (e is HttpException && e.code() == 409) {
            return state.copy(
                isSaving = false,
                fieldErrors = state.fieldErrors + ("email" to "Ya existe una cuenta con este correo."),
            )
        }

        val message = when {
            e is HttpException && e.code() == 401 -> "Tu sesión expiró. Vuelve a iniciar sesión."
            e is HttpException && e.code() == 403 -> "No tienes permiso para agregar colaboradoras."
            e is HttpException && e.code() == 400 -> "Revisa los datos ingresados."
            e is IOException -> "Sin conexión a internet. Inténtalo de nuevo."
            else -> "Ocurrió un error al agregar la colaboradora."
        }
        return state.copy(isSaving = false, generalError = message)
    }

    companion object {
        private val EMAIL_REGEX = Regex("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")
        private const val MIN_PASSWORD_LENGTH = 8
        private const val PHONE_LENGTH = 10
    }
}
