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
            val mockCatalogs =
                ExpedientCatalogs(
                    municipalities =
                    listOf(
                        "Querétaro",
                        "San Juan del Río",
                        "Corregidora",
                        "Jalpan de Serra",
                    ),
                    localities =
                    listOf(
                        "Centro",
                        "Santa Rosa Jáuregui",
                        "Felipe Carrillo Puerto",
                    ),
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

    // Validation
    fun onSubmitClick() {
        val current = _uiState.value.personalData
        val errors = mutableMapOf<String, String>()
        if (current.fullName.isBlank()) errors["fullName"] = "Este campo es obligatorio"
        if (current.municipality.isBlank()) errors["municipality"] = "Selecciona un municipio"
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
