package com.example.adaxintegra.presentation.viewmodel

import com.example.adaxintegra.domain.model.CatalogosExpediente
import com.example.adaxintegra.domain.model.DatosCasoForm
import com.example.adaxintegra.domain.model.DatosPersonalesForm

data class ExpedientUiState(
    val currentStep: Int = 1, // Paso 1 de datos personales
    val isLoadingCatalogs: Boolean = false,
    val isSubmitting: Boolean = false,
    // Datos ingresados
    val personalData: DatosPersonalesForm = DatosPersonalesForm(),
    val caseData: DatosCasoForm = DatosCasoForm(),
    val catalogs: CatalogosExpediente = CatalogosExpediente(),
    // Errores de validación
    val personalDataErrors: Map<String, String> = emptyMap(),
    val caseDataErrors: Map<String, String> = emptyMap(),
    // Control de dialogos y navegación
    val showConfirmationDialog: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
)
