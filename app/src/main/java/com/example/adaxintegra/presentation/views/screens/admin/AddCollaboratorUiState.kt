package com.example.adaxintegra.presentation.views.screens.admin

// G-03: state of the "Agregar Colaboradora" form
data class AddCollaboratorUiState(
    val isDialogOpen: Boolean = false,
    val name: String = "",
    val lastName: String = "",
    val email: String = "",
    val password: String = "",
    val phone: String = "",
    // Phone country code selected in the form (Mexico by default)
    val countryCode: String = "+52",
    // one message per field: "name", "lastName", "email", "password", "phone"
    val fieldErrors: Map<String, String> = emptyMap(),
    val generalError: String? = null,
    val isSaving: Boolean = false,
    val successMessage: String? = null,
) {
    // Acceptance criteria: "Guardar" is enabled only when every field is filled
    val canSave: Boolean
        get() = name.isNotBlank() &&
            lastName.isNotBlank() &&
            email.isNotBlank() &&
            password.isNotBlank() &&
            phone.isNotBlank() &&
            !isSaving
}
