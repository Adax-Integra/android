package com.example.adaxintegra.presentation.viewmodel

data class EditableProfileUiState (
    //everyone sees
    val name: String = "",
    val lastName: String = "",
    val phoneCountryCode: String = "+52",
    val phone: String = "",

    //external can see
    val country: String = "",
    val state: String = "",
    val municipality: String = "",

    val hasAddressFields: Boolean = false,

    //states of the view/ data
    val isEditing: Boolean = false,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isRefreshing: Boolean = false,

    //final confirmation pop ups
    val showSaveConfirmation: Boolean = false,
    val showDiscardConfirmation: Boolean = false,

    //resulting states
    val error: String? = null,
    val successMessage: String? = null,

)
