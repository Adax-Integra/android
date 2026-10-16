package com.example.adaxintegra.presentation.views.designsystem.organisms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.presentation.views.designsystem.molecules.AutoCompleteOutlinedTextField
import com.example.adaxintegra.presentation.views.designsystem.molecules.LabeledTextField
import com.example.adaxintegra.presentation.views.designsystem.molecules.PhoneField

val MEXICAN_STATES = listOf(
    "Aguascalientes", "Baja California", "Baja California Sur", "Campeche", "Chiapas",
    "Chihuahua", "Ciudad de México", "Coahuila", "Colima", "Durango", "Guanajuato",
    "Guerrero", "Hidalgo", "Jalisco", "Estado de México", "Michoacán", "Morelos",
    "Nayarit", "Nuevo León", "Oaxaca", "Puebla", "Querétaro", "Quintana Roo",
    "San Luis Potosí", "Sinaloa", "Sonora", "Tabasco", "Tamaulipas", "Tlaxcala",
    "Veracruz", "Yucatán", "Zacatecas",
)

private val PHONE_PREFIXES = listOf("+52", "+1", "+34", "+57", "+54", "+56", "+51", "+502", "+503", "+504")

@Suppress("ktlint:standard:function-naming")
@Composable
fun EditProfileForm (
    nameValue: String,
    onNameChange: (String) -> Unit,
    lastnameValue: String,
    onLastnameChange: (String) -> Unit,
    countryCodeOptions: String,
    onCountryCodeChange: (String) -> Unit = {},
    phoneValue: String,
    onPhoneChange: (String) -> Unit,

    //profile location
    isExternal: Boolean,
    countryValue: String,
    onCountryChange: (String) -> Unit,
    countryOptions: String,
    stateValue: String,
    onStateChange: (String) -> Unit,
    stateOptions: List<String> = MEXICAN_STATES,
    municipalityValue: String,
    onMunicipalityChange: (String) -> Unit,

    //form ui state
    isLoading: Boolean,
    isEditing: Boolean,
    modifier: Modifier = Modifier,

    //validation messages
    nameError: String? = null,
    lastnameError: String? = null,
    phoneError: String? = null,
    countryError: String? = null,
    stateError: String? = null,
    municipalityError: String? = null,
) {
    val fieldsEnabled = isEditing && !isLoading

    Column(modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)) {
        LabeledTextField(
            label = "Nombre",
            value = nameValue,
            onValueChange = onNameChange,
            placeholder = "Escribe aquí",
            enabled = fieldsEnabled,
            errorMessage = nameError,
        )

        LabeledTextField(
            label = "Apellido",
            value = lastnameValue,
            onValueChange = onLastnameChange,
            placeholder = "Escribe aquí",
            enabled = fieldsEnabled,
            errorMessage = lastnameError,
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PhoneField(
                countryCode = countryCodeOptions,
                phoneValue = phoneValue,
                onPhoneChange = onPhoneChange,
                onCountryCodeChange = onCountryCodeChange,
                enabled = fieldsEnabled,
                errorMessage = phoneError,
                placeholder = "1368536985",
            )
        }

        //these only show up on external accounts
       /* if (isExternal) {
            AutoCompleteOutlinedTextField(
                value = countryValue,
                onValueChange = { newCountry ->
                    if (!newCountry.equals(countryValue, ignoreCase = true)) {
                        onStateChange("")
                        onMunicipalityChange("")
                    }
                    onCountryChange(newCountry)
                },
                label = "País",
                options = countryOptions,
                enabled = fieldsEnabled,
                isError = countryError != null,
            )

            //list of mexican states when mexico is chosen, but an open field text
            //with a different country
            val isMexico = countryValue.trim()
                .equals("México", ignoreCase = true)

            if (isMexico) {
                AutoCompleteOutlinedTextField(
                    value = stateValue,
                    onValueChange = onStateChange,
                    label = "Estado",
                    options = stateOptions,
                    enabled = fieldsEnabled,
                    isError = stateError != null,
                )
            } else {
                LabeledTextField(
                    label = "Estado",
                    value = stateValue,
                    onValueChange = onStateChange,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = fieldsEnabled,
                    errorMessage = stateError,
                )
            }*/

            LabeledTextField(
                label = "Municipio",
                value = municipalityValue,
                onValueChange = onMunicipalityChange,
                placeholder = "Escribe aquí...",
                enabled = fieldsEnabled,
                errorMessage = municipalityError,
            )
        }
    }

