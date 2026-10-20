package com.example.adaxintegra.presentation.viewmodel

import com.example.adaxintegra.domain.model.UploadDocumentFile
import java.time.LocalTime
import java.util.Calendar

data class NewEventUiState(
    val title: String = "",
    val titleError: String? = null,
    val startDate: Calendar? = null,
    val endDate: Calendar? = null,
    val startDateError: String? = null,
    val endDateError: String? = null,
    val startTime: LocalTime? = null,
    val endTime: LocalTime? = null,
    val startTimeError: String? = null,
    val endTimeError: String? = null,
    val modality: String = "",
    val modalityError: String? = null,
    val location: String = "",
    val locationError: String? = null,
    val link: String = "",
    val linkError: String? = null,
    val phone: String = "",
    val phoneError: String? = null,
    val countryCode: String = "+52",
    val description: String = "",
    val descriptionError: String? = null,
    val notes: String = "",
    val notesError: String? = null,
    val selectedImageFile: UploadDocumentFile? = null,
    val selectedImageName: String? = null,
    val isImageUploading: Boolean = false,
    val imageUploadError: String? = null,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val toastMessage: String? = null,
    val error: String? = null,
    // E-02: true when the form opens an existing event to edit it
    val isEditing: Boolean = false,
    // E-02: image the event already has, shown until a new one is picked
    val currentImageUrl: String? = null,
)
