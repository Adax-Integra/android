package com.example.adaxintegra.presentation.views.screens.cases

//R-02: state of the "Registrar el caso" form
data class CreateCaseUiState(
    val writtenDescription: String = "",
    val writtenHelpsWanted: String = "",
    val hasExternalSupport: Boolean? = null,
    val fieldErrors: Map<String, String> = emptyMap(),
    val generalError: String? = null,
    val isSaving: Boolean = false,
    val successMessage: String? = null,
){
    //"Guardar" is enabled only when the three fields carry real text and the
    // support question was answered
    val canSave: Boolean
        get() = writtenDescription.isNotBlank() &&
            writtenHelpsWanted.isNotBlank() &&
            hasExternalSupport != null &&
            !isSaving
}
