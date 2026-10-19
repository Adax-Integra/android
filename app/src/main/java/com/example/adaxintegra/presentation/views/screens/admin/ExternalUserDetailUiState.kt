package com.example.adaxintegra.presentation.views.screens.admin

import com.example.adaxintegra.domain.model.Country
import com.example.adaxintegra.domain.model.ExternalProfileChange
import com.example.adaxintegra.domain.model.ExternalProfileForm
import com.example.adaxintegra.domain.model.ExternalUserProfile
import com.example.adaxintegra.domain.model.ExternalUserProfileUpdate

// Same limit the backend checks for the reason of the change
const val MAX_REASON_LENGTH = 500

private const val EMPTY_VALUE = "Sin dato"
private val EMAIL_REGEX = Regex("""^[^\s@]+@[^\s@]+\.[^\s@]+$""")

// G-07: state of the detail and the edit form of an external user
// Both screens share it
data class ExternalUserDetailUiState(
    val profile: ExternalUserProfile? = null,
    // Values that came from the backend. Compared with the form to know what changed
    val saved: ExternalProfileForm = ExternalProfileForm(),
    val form: ExternalProfileForm = ExternalProfileForm(),
    val countries: List<Country> = emptyList(),
    val reason: String = "",
    val hasConsent: Boolean = false,
    // Turned on by the "Guardar" tap, so empty fields are not marked
    // as errors before the admin tries to save
    val showErrors: Boolean = false,
    // Email that the backend answered as already registered (409)
    val takenEmail: String? = null,
    // Only for the first load, the detail can not be shown without the profile
    val loadError: String? = null,
    val isSaving: Boolean = false,
    val isEditing: Boolean = false,
    val isShowingConfirmation: Boolean = false,
    val isShowingSuccessToast: Boolean = false,
    // Shown in the "Aviso" dialog
    val errorMessage: String? = null,
) {
    val displayName: String
        get() {
            val fullName = "${profile?.name.orEmpty()} ${profile?.lastName.orEmpty()}".trim()
            return fullName.ifEmpty { "Sin nombre" }
        }

    val initials: String
        get() {
            val first = profile?.name?.trim()?.firstOrNull()?.toString().orEmpty()
            val second = profile?.lastName?.trim()?.firstOrNull()?.toString().orEmpty()
            return (first + second).uppercase()
        }

    // The API sends "yyyy-MM-dd", the screen shows "dd/MM/yyyy"
    val birthDateText: String
        get() {
            val parts = profile?.birthDate?.take(10)?.split("-")
            if (parts == null || parts.size != 3 || parts.any { it.isBlank() }) {
                return EMPTY_VALUE
            }
            return "${parts[2]}/${parts[1]}/${parts[0]}"
        }

    val countryOptions: List<String>
        get() = countries.map { it.name }

    val stateOptions: List<String>
        get() = countries.firstOrNull { it.name == form.country }?.states.orEmpty()

    // Only the fields that changed, in the order of the form
    val changes: List<ExternalProfileChange>
        get() = listOf(
            Triple("Correo electrónico", cleanEmail(saved.email), cleanEmail(form.email)),
            Triple("Teléfono celular", phoneText(saved), phoneText(form)),
            Triple("País", saved.country.orEmpty(), form.country.orEmpty()),
            Triple("Estado", saved.state.orEmpty(), form.state.orEmpty()),
            Triple("Municipio/Localidad", saved.municipality.trim(), form.municipality.trim()),
        ).filter { (_, old, new) -> old != new }
            .map { (label, old, new) ->
                ExternalProfileChange(
                    label = label,
                    oldValue = displayValue(old),
                    newValue = displayValue(new),
                )
            }

    // Error messages for each field. They are computed,
    // so they disappear as soon as the admin fixes the field
    val emailError: String? get() = if (showErrors) emailProblem else null
    val phoneError: String? get() = if (showErrors) phoneProblem else null
    val stateError: String? get() = if (showErrors) stateProblem else null
    val municipalityError: String? get() = if (showErrors) municipalityProblem else null
    val reasonError: String?
        get() = if (showErrors && trimmedReason.isEmpty()) "Este campo es obligatorio" else null
    val consentError: String?
        get() = if (showErrors && !hasConsent) "Confirma que la externa autorizó el cambio" else null

    // Only the reason and the consent are required,
    // the other fields are checked only when the admin changed them
    val isFormValid: Boolean
        get() = emailProblem == null &&
            phoneProblem == null &&
            stateProblem == null &&
            municipalityProblem == null &&
            trimmedReason.isNotEmpty() &&
            hasConsent

    val trimmedReason: String
        get() = reason.trim()

    // Both inboxes receive the notice when the email changes
    val noticeText: String
        get() {
            val savedEmail = cleanEmail(saved.email)
            val newEmail = cleanEmail(form.email)
            return when {
                savedEmail.isEmpty() -> "Se enviará un aviso a $newEmail sin mostrar los datos nuevos."

                newEmail != savedEmail ->
                    "Se enviará un aviso a $savedEmail y a $newEmail sin mostrar los datos nuevos."

                else -> "Se enviará un aviso a $savedEmail sin mostrar los datos nuevos."
            }
        }

    // Body of the PATCH: only the fields that changed go in it
    fun toUpdate(): ExternalUserProfileUpdate {
        val email = cleanEmail(form.email)
        val municipality = form.municipality.trim()

        return ExternalUserProfileUpdate(
            email = email.takeIf { it != cleanEmail(saved.email) },
            phone = form.fullPhone.takeIf { it != saved.fullPhone },
            country = form.country.takeIf { it != saved.country },
            state = form.state.takeIf { it != saved.state },
            municipality = municipality.takeIf { it != saved.municipality.trim() },
            reason = trimmedReason,
            consentConfirmed = hasConsent,
        )
    }

    // The address counts as changed when any of its three fields changed
    private val addressChanged: Boolean
        get() = form.country != saved.country ||
            form.state != saved.state ||
            form.municipality.trim() != saved.municipality.trim()

    // Only checked when the admin changed the email
    private val emailProblem: String?
        get() {
            val email = cleanEmail(form.email)
            return when {
                email == cleanEmail(saved.email) -> null
                !EMAIL_REGEX.matches(email) -> "Ingresa un correo electrónico válido"
                email == takenEmail -> "Este correo ya está registrado en otra cuenta."
                else -> null
            }
        }

    // Only checked when the admin changed the phone
    private val phoneProblem: String?
        get() = when {
            form.fullPhone == saved.fullPhone -> null
            form.phone.isEmpty() -> "Ingresa un número válido"
            form.phone.length != 10 -> "Debe tener 10 dígitos"
            else -> null
        }

    // The state depends on the country and the municipality on the state
    private val stateProblem: String?
        get() = if (addressChanged && form.country != null && form.state == null) {
            "Selecciona un estado"
        } else {
            null
        }

    private val municipalityProblem: String?
        get() = if (addressChanged && form.state != null && form.municipality.isBlank()) {
            "Escribe el municipio o localidad"
        } else {
            null
        }
}

// Emails are compared and sent without spaces and in lowercase
fun cleanEmail(email: String): String = email.trim().lowercase()

// Empty values are shown as "Sin dato"
fun displayValue(value: String?): String = value?.trim()?.takeIf { it.isNotEmpty() } ?: EMPTY_VALUE

// "+52 442 123 4567", empty when there is no number
fun phoneText(form: ExternalProfileForm): String {
    val digits = form.phone
    if (digits.isEmpty()) {
        return ""
    }
    if (digits.length != 10) {
        return "${form.countryCode} $digits"
    }
    return "${form.countryCode} ${digits.take(3)} ${digits.substring(3, 6)} ${digits.takeLast(4)}"
}
