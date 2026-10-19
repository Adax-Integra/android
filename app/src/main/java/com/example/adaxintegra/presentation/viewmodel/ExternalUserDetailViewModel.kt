package com.example.adaxintegra.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.model.ExternalUserProfile
import com.example.adaxintegra.domain.model.toForm
import com.example.adaxintegra.domain.usecases.GetCountriesUseCase
import com.example.adaxintegra.domain.usecases.GetExternalProfileUseCase
import com.example.adaxintegra.domain.usecases.UpdateExternalProfileUseCase
import com.example.adaxintegra.presentation.views.screens.admin.ExternalUserDetailUiState
import com.example.adaxintegra.presentation.views.screens.admin.MAX_REASON_LENGTH
import com.example.adaxintegra.presentation.views.screens.admin.cleanEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

// G-07: shows the data of an external user and lets the admin edit the text fields
// The detail and the edit form share this ViewModel
@HiltViewModel
class ExternalUserDetailViewModel @Inject constructor(
    private val getExternalProfileUseCase: GetExternalProfileUseCase,
    private val updateExternalProfileUseCase: UpdateExternalProfileUseCase,
    private val getCountriesUseCase: GetCountriesUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ExternalUserDetailUiState())
    val uiState: StateFlow<ExternalUserDetailUiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null

    // Coming back to the screen does not reload, apply() already has the new data
    fun loadProfile(userId: String) {
        if (_uiState.value.profile != null) {
            return
        }
        loadJob?.cancel()
        _uiState.update { it.copy(loadError = null) }

        // The catalog is only needed to edit, so it loads apart and a failure
        // does not block the detail
        if (_uiState.value.countries.isEmpty()) {
            viewModelScope.launch {
                val countries = getCountriesUseCase()
                _uiState.update { it.copy(countries = countries) }
            }
        }

        loadJob = viewModelScope.launch {
            getExternalProfileUseCase(userId).collect { result ->
                when (result) {
                    is Result.Loading -> Unit

                    is Result.Success -> apply(result.data)

                    is Result.Error -> _uiState.update {
                        it.copy(loadError = "No se pudo cargar la información de la externa.")
                    }
                }
            }
        }
    }

    fun startEditing() {
        _uiState.update {
            it.copy(
                form = it.saved,
                reason = "",
                hasConsent = false,
                showErrors = false,
                takenEmail = null,
                isEditing = true,
            )
        }
    }

    fun cancelEditing() {
        _uiState.update {
            it.copy(isEditing = false, isShowingConfirmation = false)
        }
    }

    fun onEmailChange(value: String) {
        _uiState.update { it.copy(form = it.form.copy(email = value)) }
    }

    fun onCountryCodeChange(value: String) {
        _uiState.update { it.copy(form = it.form.copy(countryCode = value)) }
    }

    // Only digits, at most the 10 of the number
    fun onPhoneChange(value: String) {
        val digits = value.filter { it.isDigit() }.take(10)
        _uiState.update { it.copy(form = it.form.copy(phone = digits)) }
    }

    // A new country invalidates the selected state and the municipality
    fun selectCountry(country: String) {
        _uiState.update {
            if (country == it.form.country) {
                it
            } else {
                it.copy(form = it.form.copy(country = country, state = null, municipality = ""))
            }
        }
    }

    // A new state invalidates the municipality
    fun selectState(state: String) {
        _uiState.update {
            if (state == it.form.state) {
                it
            } else {
                it.copy(form = it.form.copy(state = state, municipality = ""))
            }
        }
    }

    fun onMunicipalityChange(value: String) {
        _uiState.update { it.copy(form = it.form.copy(municipality = value)) }
    }

    fun onReasonChange(value: String) {
        _uiState.update { it.copy(reason = value.take(MAX_REASON_LENGTH)) }
    }

    fun onConsentChange(checked: Boolean) {
        _uiState.update { it.copy(hasConsent = checked) }
    }

    // Without changes there is nothing to save, so it leaves like the back button.
    // Otherwise the first tap reveals the invalid fields
    fun onSaveTapped() {
        val current = _uiState.value
        if (current.isSaving) {
            return
        }
        if (current.changes.isEmpty()) {
            cancelEditing()
            return
        }

        val withErrors = current.copy(showErrors = true)
        _uiState.value = withErrors.copy(isShowingConfirmation = withErrors.isFormValid)
    }

    fun dismissConfirmation() {
        _uiState.update { it.copy(isShowingConfirmation = false) }
    }

    fun confirm() {
        val current = _uiState.value
        val profile = current.profile ?: return
        if (current.isSaving || !current.isFormValid) {
            return
        }

        _uiState.update {
            it.copy(isShowingConfirmation = false, isSaving = true, errorMessage = null)
        }

        viewModelScope.launch {
            updateExternalProfileUseCase(profile.userId, current.toUpdate()).collect { result ->
                when (result) {
                    is Result.Loading -> Unit

                    is Result.Success -> {
                        apply(result.data)
                        _uiState.update {
                            it.copy(
                                isSaving = false,
                                isEditing = false,
                                isShowingSuccessToast = true,
                            )
                        }
                    }

                    is Result.Error -> handle(result.exception)
                }
            }
        }
    }

    fun onToastShown() {
        _uiState.update { it.copy(isShowingSuccessToast = false) }
    }

    fun onErrorDismissed() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    // The detail and the form start with what the backend has now
    private fun apply(profile: ExternalUserProfile) {
        val data = profile.toForm()
        _uiState.update {
            it.copy(profile = profile, saved = data, form = data, loadError = null)
        }
    }

    private fun handle(exception: Throwable) {
        val code = (exception as? HttpException)?.code()

        _uiState.update {
            when (code) {
                // The email field gets the red outline with the message
                409 -> it.copy(
                    isSaving = false,
                    takenEmail = cleanEmail(it.form.email),
                    showErrors = true,
                )

                401 -> it.copy(isSaving = false, errorMessage = "Tu sesión expiró. Vuelve a iniciar sesión.")

                403 -> it.copy(isSaving = false, errorMessage = "No tienes permiso para modificar estos datos.")

                404 -> it.copy(isSaving = false, errorMessage = "La cuenta de la externa no está disponible.")

                400 -> it.copy(isSaving = false, errorMessage = "Revisa los datos e intenta de nuevo.")

                null -> it.copy(
                    isSaving = false,
                    errorMessage = "No pudimos guardar los cambios. Revisa tu conexión e intenta de nuevo.",
                )

                else -> it.copy(
                    isSaving = false,
                    errorMessage = "No pudimos guardar los cambios. Intenta de nuevo más tarde.",
                )
            }
        }
    }
}
