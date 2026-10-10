package com.example.adaxintegra.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adaxintegra.domain.common.FieldValidationException
import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.model.MAX_CASE_DESCRIPTION_LENGTH
import com.example.adaxintegra.domain.model.MAX_CASE_HELPS_WANTED_LENGTH
import com.example.adaxintegra.domain.model.NewCase
import com.example.adaxintegra.domain.repository.AuthRepository
import com.example.adaxintegra.domain.usecases.CreateCaseUseCase
import com.example.adaxintegra.presentation.views.screens.cases.CreateCaseUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

// R-02: the external user registers a new case after confirming her data (R-01)
@HiltViewModel
class CreateCaseViewModel @Inject constructor(
    private val createCaseUseCase: CreateCaseUseCase,
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CreateCaseUiState())
    val uiState: StateFlow<CreateCaseUiState> = _uiState.asStateFlow()

    // Each input is cut at the maximum length the backend accepts
    fun onDescriptionChange(value: String) {
        val limited = value.take(MAX_CASE_DESCRIPTION_LENGTH)
        _uiState.update {
            it.copy(
                writtenDescription = limited,
                fieldErrors = it.fieldErrors - "writtenDescription",
                generalError = null,
            )
        }
    }

    fun onHelpsWantedChange(value: String) {
        val limited = value.take(MAX_CASE_HELPS_WANTED_LENGTH)
        _uiState.update {
            it.copy(
                writtenHelpsWanted = limited,
                fieldErrors = it.fieldErrors - "writtenHelpsWanted",
                generalError = null,
            )
        }
    }

    // The Sí/No dropdown maps to the boolean the backend expects
    fun onExternalSupportChange(value: Boolean) {
        _uiState.update {
            it.copy(
                hasExternalSupport = value,
                fieldErrors = it.fieldErrors - "hasExternalSupport",
                generalError = null,
            )
        }
    }

    fun cancel() {
        _uiState.update { CreateCaseUiState() }
    }

    // Closes the confirmation dialog ("Aceptar")
    fun dismissSuccessMessage() {
        _uiState.update { it.copy(successMessage = null) }
    }

    fun save() {
        val state = _uiState.value
        val errors = validate(state)

        if (errors.isNotEmpty()) {
            _uiState.update { it.copy(fieldErrors = errors) }
            return
        }

        val userId = authRepository.session.value?.userId

        if (userId.isNullOrBlank()) {
            _uiState.update { it.copy(generalError = "No hay una sesión activa.") }
            return
        }

        val newCase = NewCase(
            writtenDescription = state.writtenDescription.trim(),
            writtenHelpsWanted = state.writtenHelpsWanted.trim(),
            hasExternalSupport = state.hasExternalSupport == true,
        )

        viewModelScope.launch {
            createCaseUseCase(userId, newCase).collect { result ->
                _uiState.update { current ->
                    when (result) {
                        is Result.Loading -> current.copy(isSaving = true, generalError = null)

                        is Result.Success -> CreateCaseUiState(
                            successMessage = "Tu caso fue registrado correctamente. " +
                                "Puedes seguirlo desde Mis Casos.",
                        )

                        is Result.Error -> errorState(current, result.exception)
                    }
                }
            }
        }
    }

    private fun validate(state: CreateCaseUiState): Map<String, String> {
        val errors = mutableMapOf<String, String>()

        if (state.writtenDescription.isBlank()) {
            errors["writtenDescription"] = "La descripción del caso es obligatoria."
        } else if (state.writtenDescription.trim().length > MAX_CASE_DESCRIPTION_LENGTH) {
            errors["writtenDescription"] =
                "La descripción debe tener máximo $MAX_CASE_DESCRIPTION_LENGTH caracteres."
        }

        if (state.writtenHelpsWanted.isBlank()) {
            errors["writtenHelpsWanted"] = "Indica qué ayuda esperas recibir."
        } else if (state.writtenHelpsWanted.trim().length > MAX_CASE_HELPS_WANTED_LENGTH) {
            errors["writtenHelpsWanted"] =
                "Este campo debe tener máximo $MAX_CASE_HELPS_WANTED_LENGTH caracteres."
        }

        if (state.hasExternalSupport == null) {
            errors["hasExternalSupport"] = "Selecciona Sí o No."
        }

        return errors
    }

    private fun errorState(state: CreateCaseUiState, e: Throwable): CreateCaseUiState {
        if (e is FieldValidationException) {
            val flagged = e.fieldErrors.keys
                .filter { it in SERVER_FIELD_MESSAGES }
                .associateWith { field -> SERVER_FIELD_MESSAGES.getValue(field) }

            return state.copy(
                isSaving = false,
                fieldErrors = state.fieldErrors + flagged,
                generalError = if (flagged.isEmpty()) "Revisa los datos del caso." else null,
            )
        }

        val message = when {
            e is HttpException && e.code() == 401 -> "Tu sesión expiró. Vuelve a iniciar sesión."
            e is HttpException && e.code() == 403 -> "No tienes permiso para registrar este caso."
            // R-02 runs after R-01, so a missing record is not a form problem
            e is HttpException && e.code() == 404 ->
                "No tienes un expediente registrado. Comunícate con la asociación."
            e is HttpException && e.code() == 400 -> "Revisa los datos del caso."
            e is IOException -> "Sin conexión a internet. Inténtalo de nuevo."
            else -> "Ocurrió un error al registrar el caso."
        }

        return state.copy(isSaving = false, generalError = message)
    }

    companion object {
        private val SERVER_FIELD_MESSAGES = mapOf(
            "writtenDescription" to "Revisa la descripción del caso.",
            "writtenHelpsWanted" to "Revisa la ayuda que esperas recibir.",
            "hasExternalSupport" to "Selecciona Sí o No.",
        )
    }
}
