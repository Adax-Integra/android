package com.example.adaxintegra.domain.model

// The phone is saved as one text like "+524421234567": the last 10 digits are the number
private const val PHONE_DIGITS = 10
private const val DEFAULT_COUNTRY_CODE = "+52"

// G-07: editable fields of the form. Name, last name and birth date are not here
// since the associate asked them to be kept as fixed
data class ExternalProfileForm(
    val email: String = "",
    val countryCode: String = DEFAULT_COUNTRY_CODE,
    val phone: String = "",
    val country: String? = null,
    val state: String? = null,
    val municipality: String = "",
) {
    // Same text the backend saves, empty when there is no number
    val fullPhone: String
        get() = if (phone.isEmpty()) "" else countryCode + phone
}

// One row of the confirmation sheet: what a field had and what it will have
data class ExternalProfileChange(
    val label: String,
    val oldValue: String,
    val newValue: String,
)

// G-07: values that came from the backend, the form starts with them
fun ExternalUserProfile.toForm(): ExternalProfileForm {
    val savedPhone = phone.orEmpty()
    val hasCode = savedPhone.length > PHONE_DIGITS

    return ExternalProfileForm(
        email = email.orEmpty(),
        countryCode = if (hasCode) savedPhone.dropLast(PHONE_DIGITS) else DEFAULT_COUNTRY_CODE,
        phone = if (hasCode) savedPhone.takeLast(PHONE_DIGITS) else savedPhone,
        country = country?.takeIf { it.isNotBlank() },
        state = state?.takeIf { it.isNotBlank() },
        municipality = municipality.orEmpty(),
    )
}
