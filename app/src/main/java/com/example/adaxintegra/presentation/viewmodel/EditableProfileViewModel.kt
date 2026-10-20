package com.example.adaxintegra.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adaxintegra.domain.repository.AuthRepository
import com.example.adaxintegra.domain.usecases.GetEditableProfileUseCase
import com.example.adaxintegra.domain.usecases.UpdateProfileUseCase
import com.example.adaxintegra.presentation.views.designsystem.molecules.CountryCodeOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditableProfileViewModel @Inject constructor(
    private val getEditableProfileUseCase: GetEditableProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        EditableProfileUiState(
            isLoading = true
        )
    )
    val uiState: StateFlow<EditableProfileUiState> =
        _uiState.asStateFlow()

    private var userId: String? = null

    //seen during read only mode
    private var originalName = ""
    private var originalLastName = ""
    private var originalPhoneCountryCode = "+52"
    private var originalPhone = ""
    private var originalCountry = ""
    private var originalState = ""
    private var originalMunicipality = ""

    //load profile
    init {
        loadProfile()
    }

    fun loadProfile(isRefresh: Boolean = false) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = !isRefresh,
                    isRefreshing = isRefresh,
                    error = null
                )
            }

            //import user id to get role
            try {
                val currentUserId =
                    authRepository.session.value?.userId

                if (currentUserId.isNullOrBlank()) {
                    throw IllegalStateException(
                        "Inicia sesión para consultar tu perfil"
                    )
                }

                userId = currentUserId

                val profile = getEditableProfileUseCase(currentUserId)

                originalName = profile.name
                originalLastName = profile.lastName
                originalPhone = profile.phone

                val (phoneCode, localPhone) = splitPhoneNumber(profile.phone)
                originalPhoneCountryCode = phoneCode

                //only seen by external, but could be empty
                originalCountry = profile.address?.country.orEmpty()
                originalState = profile.address?.state.orEmpty()
                originalMunicipality = profile.address?.municipality.orEmpty()

                _uiState.update {
                    it.copy(
                        name = profile.name,
                        lastName = profile.lastName,
                        phoneCountryCode = phoneCode,
                        phone = localPhone,
                        country = originalCountry,
                        state = originalState,
                        municipality = originalMunicipality,
                        hasAddressFields = profile.address != null,
                        isLoading = false,
                        isEditing = false,
                        isRefreshing = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isRefreshing = false,
                        error = e.message
                            ?: "No se pudo cargar tu perfil"
                    )
                }
            }
        }
    }

    //for pull to refresh, but block edits being lost on refresh
    fun refreshEditableProfile() {
        if (_uiState.value.isEditing || _uiState.value.isSaving) return
        loadProfile(isRefresh = true)
    }

    //handle editing mode
    fun startEditing() {
        _uiState.update {
            it.copy(
                isEditing = true,
                error = null,
                successMessage = null
            )
        }
    }

    //changing values
    fun onNameChange(value: String) {
        _uiState.update {
            it.copy(
                name = value
            )
        }
    }

    fun onLastNameChange(value: String) {
        _uiState.update {
            it.copy(
                lastName = value
            )
        }
    }

    fun onPhoneChange(value: String) {
        _uiState.update {
            it.copy(
                phone = value
            )
        }
    }

    fun onCountryChange(value: String) {
        _uiState.update {
            it.copy(
                country = value
            )
        }
    }

    fun onStateChange(value: String) {
        _uiState.update {
            it.copy(
                state = value
            )
        }
    }

    fun onMunicipalityChange(value: String) {
        _uiState.update {
            it.copy(
                municipality = value
            )
        }
    }

    fun onPhoneCountryCodeChange(code: String) {
        _uiState.update { state ->
            state.copy(phoneCountryCode = code)
        }
    }

    //pop up handling final confirmation from user to exit editing mode and discard changes
    fun requestDiscardConfirmation() {
        _uiState.update {
            it.copy(
                showDiscardConfirmation = true
            )
        }
    }

    fun dismissDiscardConfirmation() {
        _uiState.update {
            it.copy(
                showDiscardConfirmation = false
            )
        }
    }

    fun confirmDiscard() {
        val (originalCode, originalLocalPhone) =
            splitPhoneNumber(originalPhone)
        _uiState.update {
            it.copy(
                name = originalName,
                lastName = originalLastName,
                phoneCountryCode = originalCode,
                phone = originalLocalPhone,
                country = originalCountry,
                state = originalState,
                municipality = originalMunicipality,
                isEditing = false,
                showDiscardConfirmation = false,
                showSaveConfirmation = false,
                error = null,
                successMessage = null
            )
        }
    }

    //pop up handling final confirmation from user to save
    fun requestSaveConfirmation() {
        _uiState.update {
            it.copy(
                showSaveConfirmation = true
            )
        }
    }

    fun dismissSaveConfirmation() {
        _uiState.update {
            it.copy(
                showSaveConfirmation = false
            )
        }
    }

    fun confirmSave() {
        _uiState.update {
            it.copy(
                showSaveConfirmation = false
            )

        }
        saveChanges()
    }

    //save changes
    fun saveChanges() {
        val currentUserId = userId ?: return
        val state = _uiState.value

        if (state.isSaving || !state.isEditing) return

        val fullPhone = state.phoneCountryCode + state.phone.trim()
        //name, lastname and phone must have data validation
        if (
            state.name.isBlank() ||
            state.lastName.isBlank() ||
            state.phone.isBlank()
        ) {
            _uiState.update {
                it.copy(error = "Completa todos los campos obligatorios.")
            }
            return
        }

        val fields = mutableMapOf<String, String>()

        if (state.name.trim() != originalName) {
            fields["name"] = state.name.trim()
        }

        if (state.lastName.trim() != originalLastName) {
            fields["last_name"] = state.lastName.trim()
        }

        if (fullPhone != originalPhone) {
            fields["phone"] = fullPhone
        }

        if (state.hasAddressFields) {
            if (state.country.trim() != originalCountry) {
                fields["country"] = state.country.trim()
            }

            if (state.state.trim() != originalState) {
                fields["state"] = state.state.trim()
            }

            if (
                state.municipality.trim() != originalMunicipality
            ) {
                fields["municipality"] = state.municipality.trim()
            }
        }

        //if nothing happens, just exit editing mode
        if (fields.isEmpty()) {
            val (originalCode, originalLocalPhone) =
                splitPhoneNumber(originalPhone)
            _uiState.update {
                it.copy(
                    name = originalName,
                    lastName = originalLastName,
                    phoneCountryCode = originalCode,
                    phone = originalLocalPhone,
                    country = originalCountry,
                    state = originalState,
                    municipality = originalMunicipality,
                    isEditing = false,
                    error = null,
                    successMessage = null
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isSaving = true,
                    error = null,
                    successMessage = null
                )
            }

            try {
                updateProfileUseCase(currentUserId, fields)

                val profile = getEditableProfileUseCase(currentUserId)

                originalName = profile.name
                originalLastName = profile.lastName
                originalPhone = profile.phone

                val (phoneCode, localPhone) = splitPhoneNumber(profile.phone)
                originalPhoneCountryCode = phoneCode

                originalCountry = profile.address?.country.orEmpty()
                originalState = profile.address?.state.orEmpty()
                originalMunicipality =
                    profile.address?.municipality.orEmpty()

                _uiState.update {
                    it.copy(
                        name = profile.name,
                        lastName = profile.lastName,
                        phoneCountryCode = phoneCode,
                        phone = localPhone,
                        country = originalCountry,
                        state = originalState,
                        municipality = originalMunicipality,
                        hasAddressFields = profile.address != null,
                        isSaving = false,
                        isEditing = false,
                        successMessage = "Perfil actualizado correctamente",
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        error = e.message
                            ?: "Hubo un error al actualizar tu perfil"
                    )
                }
            }
        }
    }


    private fun splitPhoneNumber(phone: String): Pair<String, String> {
        val matchingCode = CountryCodeOptions
            .map { it.code }
            .distinct()
            .sortedByDescending { it.length }
            .firstOrNull { phone.startsWith(it) }

        return if (matchingCode != null) {
            matchingCode to phone.removePrefix(matchingCode)
        } else {
            // Fallback for numbers without a recognized calling code.
            "+52" to phone
        }
    }

}
