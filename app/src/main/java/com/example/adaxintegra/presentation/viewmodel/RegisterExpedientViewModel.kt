package com.example.adaxintegra.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adaxintegra.domain.model.ExpedientCatalogs
import com.example.adaxintegra.domain.model.PersonalDataForm
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class RegisterExpedientViewModel
@Inject
constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(ExpedientUiState())
    val uiState: StateFlow<ExpedientUiState> = _uiState.asStateFlow()

    init {
        loadCatalogs()
    }

    private fun loadCatalogs() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingCatalogs = true) }

            // Simulate a network delay
            delay(1000)

            // Mock data for testing
            val mockCatalogs = ExpedientCatalogs(
                municipalities = listOf("Querétaro", "San Juan del Río", "Corregidora", "El Marqués"),
                localities = listOf("Centro", "Santa Rosa Jáuregui", "Felipe Carrillo Puerto"),
            )
            _uiState.update {
                it.copy(catalogs = mockCatalogs, isLoadingCatalogs = false)
            }
        }
    }

    // Update of the form
    fun onPersonalDataChange(updated: PersonalDataForm) {
        _uiState.update { it.copy(personalData = updated, personalDataErrors = emptyMap()) }
    }

    // Validation of all fields for backend
    fun onSubmitClick() {
        val current = _uiState.value.personalData
        val errors = mutableMapOf<String, String>()

        // Profile validations
        if (current.name.isBlank()) {
            errors["name"] = "El nombre es obligatorio"
        }
        if (current.lastName.isBlank()) {
            errors["lastName"] = "Los apellidos son obligatorios"
        }

        // Email pattern validation
        val emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$".toRegex()
        if (current.email.isBlank()) {
            errors["email"] = "El correo electrónico es obligatorio"
        } else if (!current.email.matches(emailRegex)) {
            errors["email"] = "Ingresa un correo electrónico válido"
        }

        // Opcional birthdate with format YYYY-MM-DD
        val dateRegex = "^\\d{4}-\\d{2}-\\d{2}$".toRegex()
        if (current.birthDate.isNotBlank() && !current.birthDate.matches(dateRegex)) {
            errors["birthDate"] = "Formato inválido. Debe ser YYYY-MM-DD"
        }

        // Address validation
        if (current.addressLine1.isBlank()) errors["addressLine1"] = "La calle y número son obligatorios"
        if (current.neighborhood.isBlank()) errors["neighborhood"] = "La colonia es obligatoria"
        if (current.zipCode.isBlank()) errors["zipCode"] = "El código postal es obligatorio"
        if (current.country.isBlank()) errors["country"] = "El país es obligatorio"
        if (current.state.isBlank()) errors["state"] = "El estado es obligatorio"
        if (current.city.isBlank()) errors["city"] = "El municipio es obligatorio"

        // Show errors if there exist, if not, show confirmation dialog
        if (errors.isNotEmpty()) {
            _uiState.update { it.copy(personalDataErrors = errors) }
        } else {
            _uiState.update { it.copy(showConfirmationDialog = true, personalDataErrors = emptyMap()) }
        }
    }

    fun onDismissDialog() {
        _uiState.update { it.copy(showConfirmationDialog = false) }
    }

    // Data upload and save process
    fun onConfirmSubmit() {
        viewModelScope.launch {
            _uiState.update { it.copy(showConfirmationDialog = false, isSubmitting = true) }
            // Simulates a bucket upload and data save in backend
            delay(2000)
            _uiState.update { it.copy(isSubmitting = false, isSuccess = true) }
        }
    }
}
