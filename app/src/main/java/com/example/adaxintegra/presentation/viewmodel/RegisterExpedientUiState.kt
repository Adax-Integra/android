package com.example.adaxintegra.presentation.viewmodel

import com.example.adaxintegra.domain.model.ExpedientCatalogs
import com.example.adaxintegra.domain.model.PersonalDataForm

data class RegisterExpedientUiState(
    val isLoadingCatalogs: Boolean = false,
    val isSubmitting: Boolean = false,
    // Compiled data
    val personalData: PersonalDataForm = PersonalDataForm(),
    val catalogs: ExpedientCatalogs = ExpedientCatalogs(),
    // Validation errors
    val personalDataErrors: Map<String, String> = emptyMap(),
    // Dialogs and navigation control
    val showConfirmationDialog: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
)
