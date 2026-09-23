package com.example.adaxintegra.presentation.viewmodel

import com.example.adaxintegra.domain.model.CaseDataForm
import com.example.adaxintegra.domain.model.ExpedientCatalogs
import com.example.adaxintegra.domain.model.PersonalDataForm

data class ExpedientUiState(
    val currentStep: Int = 1, // Paso 1 de datos personales
    val isLoadingCatalogs: Boolean = false,
    val isSubmitting: Boolean = false,
    // Datos ingresados
    val personalData: PersonalDataForm = PersonalDataForm(),
    val caseData: CaseDataForm = CaseDataForm(),
    val catalogs: ExpedientCatalogs = ExpedientCatalogs(),
    // Errores de validación
    val personalDataErrors: Map<String, String> = emptyMap(),
    val caseDataErrors: Map<String, String> = emptyMap(),
    // Control de dialogos y navegación
    val showConfirmationDialog: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
)
