package com.example.adaxintegra.domain.model

// G-07: country of the catalog with the names of its states, both in spanish
data class Country(
    val name: String,
    val phoneCode: String,
    val states: List<String>,
)
