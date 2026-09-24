package com.example.adaxintegra.domain.model

import android.net.Uri

// Catalogs charged from backend for filling the dropdowns
data class ExpedientCatalogs(
    val municipalities: List<String> = emptyList(),
    val localities: List<String> = emptyList(),
    val violenceTypes: List<String> = emptyList(),
)

// Data compiled in step 1
data class PersonalDataForm(
    val fullName: String = "",
    val municipality: String = "",
    val locality: String = "",
    val phoneNumber: String = "",
    val proofUri: Uri? = null, // Local URI of the selected document/photo
)

// Data compiled from step 2
data class CaseDataForm(
    val violenceType: String = "",
    val caseDescription: String = "",
    val additionalNotes: String = "",
)
