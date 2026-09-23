package com.example.adaxintegra.presentation.viewmodel

import com.example.adaxintegra.domain.model.CaseDataForm
import com.example.adaxintegra.domain.model.ExpedientCatalogs
import com.example.adaxintegra.domain.model.PersonalDataForm

data class ExpedientUiState(
    val currentStep: Int = 1, // Step 1: personal data
    val isLoadingCatalogs: Boolean = false,
    val isSubmitting: Boolean = false,
    // Compiled data
    val personalData: PersonalDataForm = PersonalDataForm(),
    val caseData: CaseDataForm = CaseDataForm(),
    val catalogs: ExpedientCatalogs = ExpedientCatalogs(),
    // Validation errors
    val personalDataErrors: Map<String, String> = emptyMap(),
    val caseDataErrors: Map<String, String> = emptyMap(),
    // Dialogs and navigation control
    val showConfirmationDialog: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
)
