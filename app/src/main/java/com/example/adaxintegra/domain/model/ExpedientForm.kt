package com.example.adaxintegra.domain.model

import android.net.Uri

// Catalogs charged from backend for filling the dropdowns
data class ExpedientCatalogs(
    val municipalities: List<String> = emptyList(),
    val localities: List<String> = emptyList(),
)

// Data compiled
data class PersonalDataForm(
    // Profile
    val name: String = "",
    val lastName: String = "",
    val email: String = "",
    val birthDate: String = "", // YYYY-MM-DD
    val phone: String = "",
    // Adress
    val addressLine1: String = "",
    val addressLine2: String = "",
    val neighborhood: String = "",
    val zipCode: String = "",
    val country: String = "México",
    val state: String = "",
    val city: String = "",
    // Identification file
    val proofUri: Uri? = null, // Local URI of the selected document/photo
)
