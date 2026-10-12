@file:Suppress("ktlint:standard:filename")

package com.example.adaxintegra.domain.model

data class ExpedientCatalogs(
    val municipalities: List<String> = emptyList(),
    val localities: List<String> = emptyList(),
)

// User's data compiled
data class PersonalDataForm(
    val name: String = "",
    val lastName: String = "",
    val email: String = "",
    val birthDate: String = "",
    val phonePrefix: String = "+52",
    val phone: String = "",
    val country: String = "",
    val state: String = "",
    val municipality: String = "",
)
