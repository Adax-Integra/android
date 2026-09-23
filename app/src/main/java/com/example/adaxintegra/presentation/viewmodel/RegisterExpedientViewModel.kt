package com.example.adaxintegra.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adaxintegra.domain.model.CaseDataForm
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

                // Simula un delay de la red
                delay(1000)

                // Datos de prueba
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
                        violenceTypes =
                            listOf(
                                "Violencia sexual",
                                "Violencia digital",
                                "Violencia económica",
                                "Violencia laboral",
                            ),
                    )
                _uiState.update {
                    it.copy(catalogs = mockCatalogs, isLoadingCatalogs = false)
                }
            }
        }

        // Actualizaciones de la parte 1 del formulario
        fun onPersonalDataChange(updated: PersonalDataForm) {
            _uiState.update { it.copy(personalData = updated, personalDataErrors = emptyMap()) }
        }

        // Actualizaciones de la parte 2 del formulario
        fun onCaseDataChange(updated: CaseDataForm) {
            _uiState.update { it.copy(caseData = updated, caseDataErrors = emptyMap()) }
        }

        // Validación de la parte 1
        fun onNextStepClick() {
            val current = _uiState.value.personalData
            val errors = mutableMapOf<String, String>()
            if (current.fullName.isBlank()) errors["nombre"] = "Este campo es obligatorio"
            if (current.municipality.isBlank()) errors["municipio"] = "Selecciona un municipio"
            if (errors.isNotEmpty()) {
                _uiState.update { it.copy(personalDataErrors = errors) }
            } else {
                _uiState.update { it.copy(currentStep = 2, personalDataErrors = emptyMap()) }
            }
        }

        fun onPreviousStepClick() {
            _uiState.update { it.copy(currentStep = 1) }
        }

        // Validación de la parte 2
        fun onSubmitClick() {
            val current = _uiState.value.caseData
            val errors = mutableMapOf<String, String>()
            if (current.violenceType.isBlank()) errors["tipoViolencia"] = "Selecciona el tipo de violencia"
            if (current.caseDescription.isBlank()) errors["descripcion"] = "Ingresa la descripción del caso"
            if (errors.isNotEmpty()) {
                _uiState.update { it.copy(caseDataErrors = errors) }
            } else {
                // Si la validación es correcta muestra la confirmación
                _uiState.update { it.copy(showConfirmationDialog = true, caseDataErrors = emptyMap()) }
            }
        }

        fun onDismissDialog() {
            _uiState.update { it.copy(showConfirmationDialog = false) }
        }

        // Proceso de subida de archivos y guardado
        fun onConfirmSubmit() {
            viewModelScope.launch {
                _uiState.update { it.copy(showConfirmationDialog = false, isSubmitting = true) }
                // Simula subida a bucket y guardado en backend
                delay(2000)
                _uiState.update { it.copy(isSubmitting = false, isSuccess = true) }
            }
        }
    }
