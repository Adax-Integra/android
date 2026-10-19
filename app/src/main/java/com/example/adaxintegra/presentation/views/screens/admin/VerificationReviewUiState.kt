package com.example.adaxintegra.presentation.views.screens.admin

import com.example.adaxintegra.presentation.views.designsystem.atoms.VerificationStatus

// B-02: state of the approve and reject buttons. The documents come from
// RecordDocumentsViewModel (V-13), so this state only has the decision
data class VerificationReviewUiState(
    val isRejecting: Boolean = false,
    val rejectReason: String = "",
    val isSubmitting: Boolean = false,
    // Status the backend saved, the screen goes back to the detail with it
    val decidedStatus: VerificationStatus? = null,
    // Shown in the "Aviso" dialog
    val errorMessage: String? = null,
) {
    // The reason is optional, an empty one is not sent
    val reasonToSend: String?
        get() = rejectReason.trim().ifEmpty { null }
}

// A null code means the request did not reach the backend
fun reviewErrorMessage(code: Int?): String = when (code) {
    409 -> "Esta cuenta ya no está en revisión."
    401 -> "Tu sesión expiró. Vuelve a iniciar sesión."
    403 -> "No tienes permiso para revisar esta cuenta."
    404 -> "La cuenta de la externa no está disponible."
    null -> "No pudimos guardar la decisión. Revisa tu conexión e intenta de nuevo."
    else -> "No pudimos guardar la decisión. Intenta de nuevo más tarde."
}
