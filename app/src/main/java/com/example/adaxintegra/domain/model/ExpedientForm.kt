package com.example.adaxintegra.domain.model

import android.net.Uri

// Catálogos cargados desde el backend para llenar los dropdowns
data class CatalogosExpediente(
    val municipios: List<String> = emptyList(),
    val localidades: List<String> = emptyList(),
    val tiposViolencia: List<String> = emptyList(),
)

// Datos recopilados en paso 1
data class DatosPersonalesForm(
    val nombreCompleto: String = "",
    val municipio: String = "",
    val localidad: String = "",
    val telefono: String = "",
    val comprobanteUri: Uri? = null, // Uri local de la imagen o documento seleccionado en el teléfono
)

// Datos recopilados en paso 2
data class DatosCasoForm(
    val tipoViolencia: String = "",
    val descripcionCaso: String = "",
    val notasAdicionales: String = "",
)
