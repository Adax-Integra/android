package com.example.adaxintegra.presentation.model

// R-06: a downloaded document waiting for the share sheet
// It is not a data class because it holds a ByteArray
class SharedDocument(
    val fileName: String,
    val mimeType: String,
    val bytes: ByteArray,
)
