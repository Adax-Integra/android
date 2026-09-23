package com.example.adaxintegra.domain.model

import android.net.Uri

// Catálogos cargados desde el backend para llenar los dropdowns
data class ExpedientCatalogs(
    val municipalities: List<String> = emptyList(),
    val localities: List<String> = emptyList(),
    val violenceTypes: List<String> = emptyList(),
)

// Datos recopilados en paso 1
data class PersonalDataForm(
    val fullName: String = "",
    val municipality: String = "",
    val locality: String = "",
    val phoneNumber: String = "",
    val proofUri: Uri? = null, // Local URI of the selected document/photo
)

// Datos recopilados en paso 2
data class CaseDataForm(
    val violenceType: String = "",
    val caseDescription: String = "",
    val additionalNotes: String = "",
)
