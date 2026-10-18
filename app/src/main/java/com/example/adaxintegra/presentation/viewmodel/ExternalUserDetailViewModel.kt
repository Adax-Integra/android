package com.example.adaxintegra.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.model.ExternalUserProfile
import com.example.adaxintegra.domain.model.ExternalUserProfileUpdate
import com.example.adaxintegra.domain.usecases.GetExternalProfileUseCase
import com.example.adaxintegra.domain.usecases.UpdateExternalProfileUseCase
import com.example.adaxintegra.presentation.views.screens.admin.ExternalUserDetailUiState

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ExternalUserDetailViewModel @Inject constructor(
    private val getExternalProfileUseCase: GetExternalProfileUseCase,
    private val updateExternalProfileUseCase: UpdateExternalProfileUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ExternalUserDetailUiState())
    val uiState: StateFlow<ExternalUserDetailUiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null

    fun loadProfile(userId: String) {
        loadJob?.cancel()
        _uiState.update { it.copy(isLoading = true, error = null) }

        loadJob = viewModelScope.launch {
            getExternalProfileUseCase(userId).collect { result ->
                when (result) {
                    is Result.Loading -> _uiState.update {
                        it.copy(isLoading = true, error = null)
                    }

                    is Result.Success -> _uiState.update {
                        it.copy(
                            profile = result.data,
                            isLoading = false,
                            isEditing = false,
                            email = result.data.email.orEmpty(),
                            phone = result.data.phone.orEmpty(),
                            country = result.data.country.orEmpty(),
                            state = result.data.state.orEmpty(),
                            municipality = result.data.municipality.orEmpty(),
                            error = null,
                        )
                    }

                    is Result.Error -> _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = errorMessage(result.exception),
                        )
                    }
                }
            }
        }
    }

    fun startEditing() {
        _uiState.update { current ->
            val profile = current.profile ?: return@update current
            current.copy(
                isEditing = true,
                showConfirmation = false,
                email = profile.email.orEmpty(),
                phone = profile.phone.orEmpty(),
                country = profile.country.orEmpty(),
                state = profile.state.orEmpty(),
                municipality = profile.municipality.orEmpty(),
                reason = "",
                consentConfirmed = false,
                error = null,
                successMessage = null,
            )
        }
    }

    fun cancelEditing() {
        _uiState.update {
            it.copy(
                isEditing = false,
                showConfirmation = false,
                reason = "",
                consentConfirmed = false,
                error = null,
            )
        }
    }

    fun onEmailChange(value: String) {
        _uiState.update { it.copy(email = value, error = null) }
    }

    fun onPhoneChange(value: String) {
        _uiState.update { it.copy(phone = value, error = null) }
    }

    fun onCountryChange(value: String) {
        _uiState.update { it.copy(country = value, error = null) }
    }

    fun onStateChange(value: String) {
        _uiState.update { it.copy(state = value, error = null) }
    }

    fun onMunicipalityChange(value: String) {
        _uiState.update { it.copy(municipality = value, error = null) }
    }

    fun onReasonChange(value: String) {
        _uiState.update { it.copy(reason = value.take(500), error = null) }
    }

    fun onConsentChange(confirmed: Boolean) {
        _uiState.update { it.copy(consentConfirmed = confirmed, error = null) }
    }

    private fun validationError(s: ExternalUserDetailUiState): String? {
        val p = s.profile ?: return "No se encontró el perfil."

        val fields = listOf(
            s.email.trim() to p.email.orEmpty().trim(),
            s.phone.trim() to p.phone.orEmpty().trim(),
            s.country.trim() to p.country.orEmpty().trim(),
            s.state.trim() to p.state.orEmpty().trim(),
            s.municipality.trim() to p.municipality.orEmpty().trim(),
        )
        if (fields.none { (new, old) -> new != old }) return "No hay cambios para guardar."

        val emailRegex = Regex("""^[^\s@]+@[^\s@]+\.[^\s@]+$""")
        if (fields[0].first != fields[0].second && !emailRegex.matches(fields[0].first))
            return "Ingresa un correo electrónico válido."

        val phoneRegex = Regex("""^\+\d{1,3}\d{10}$""")
        if (fields[1].first != fields[1].second && !phoneRegex.matches(fields[1].first))
            return "El teléfono debe incluir prefijo internacional y 10 dígitos."

        if (fields.drop(2).any { (new, old) -> new != old && new.isBlank() })
            return "Los campos de domicilio modificados no pueden quedar vacíos."

        if (s.reason.isBlank()) return "Escribe el motivo del cambio."
        if (!s.consentConfirmed) return "Debes confirmar la autorización de la externa."

        return null
    }

    fun requestConfirmation() {
        val current = _uiState.value
        if (!current.isEditing || current.isSaving) return

        val error = validationError(current)

        _uiState.update {
            it.copy(
                showConfirmation = error == null,
                error = error,
            )
        }
    }

    fun dismissConfirmation() {
        _uiState.update {
            it.copy(showConfirmation = false)
        }
    }

    private fun buildUpdate(s: ExternalUserDetailUiState): ExternalUserProfileUpdate {
        val original = requireNotNull(s.profile)

        fun changed(value: String, previous: String?): String? {
            val cleaned = value.trim()
            return cleaned.takeIf { it != previous.orEmpty().trim() }
        }

        return ExternalUserProfileUpdate(
            email = changed(s.email, original.email),
            phone = changed(s.phone, original.phone),
            country = changed(s.country, original.country),
            state = changed(s.state, original.state),
            municipality = changed(s.municipality, original.municipality),
            reason = s.reason.trim(),
            consentConfirmed = s.consentConfirmed,
        )
    }

    fun confirmAndSave() {
        val current = _uiState.value
        if (!current.showConfirmation || current.isSaving) return

        val profile = current.profile ?: return
        val error = validationError(current)

        if (error != null) {
            _uiState.update { it.copy(showConfirmation = false, error = error) }
            return
        }

        val update = buildUpdate(current)
        _uiState.update {
            it.copy(isSaving = true, showConfirmation = false, error = null)
        }

        viewModelScope.launch {
            updateExternalProfileUseCase(profile.userId, update).collect { result ->
                when (result) {
                    is Result.Loading -> _uiState.update {
                        it.copy(isSaving = true)
                    }

                    is Result.Success -> _uiState.update {
                        it.copy(
                            profile = result.data,
                            isSaving = false,
                            isEditing = false,
                            showConfirmation = false,
                            reason = "",
                            consentConfirmed = false,
                            successMessage = "Datos actualizados correctamente.",
                            error = null,
                        )
                    }

                    is Result.Error -> _uiState.update {
                        it.copy(
                            isSaving = false,
                            error = errorMessage(result.exception),
                        )
                    }
                }
            }
        }
    }

    fun clearSuccessMessage() {
        _uiState.update { it.copy(successMessage = null) }
    }

    private fun errorMessage(e: Throwable): String = when {
        e is HttpException && e.code() == 400 ->
            "Los datos no son válidos. Revisa los cambios."
        e is HttpException && e.code() == 401 ->
            "Tu sesión expiró. Vuelve a iniciar sesión."
        e is HttpException && e.code() == 403 ->
            "No tienes permiso para modificar esta cuenta."
        e is HttpException && e.code() == 404 ->
            "No se encontró la cuenta externa."
        e is HttpException && e.code() == 409 ->
            "El correo electrónico ya está registrado."
        e is IOException ->
            "Sin conexión a internet. Inténtalo de nuevo."
        else ->
            "Ocurrió un error. Inténtalo de nuevo."
    }
}
