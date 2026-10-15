package com.example.adaxintegra.presentation.views.designsystem.organisms

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.presentation.views.designsystem.molecules.LabeledTextField
import com.example.adaxintegra.presentation.views.designsystem.molecules.PhoneField
import com.example.adaxintegra.presentation.views.designsystem.molecules.ProfileDropdownField

@Suppress("ktlint:standard:function-naming")
@Composable
fun EditProfileForm (
    nameValue: String,
    onNameChange: (String) -> Unit,
    lastnameValue: String,
    onLastnameChange: (String) -> Unit,
    countryCode: String,
    onCountryCodeChange: (String) -> Unit = {},
    phoneValue: String,
    onPhoneChange: (String) -> Unit,

    //profile location
    isExternal: Boolean,
    countryValue: String,
    onCountryChange: (String) -> Unit,
    countryOptions: List<String>,
    stateValue: String,
    onStateChange: (String) -> Unit,
    stateOptions: List<String>,
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

    Column(modifier = modifier.fillMaxWidth()) {
        LabeledTextField(
            label = "Nombre",
            value = nameValue,
            onValueChange = onNameChange,
            placeholder = "Escribe aquí",
            enabled = fieldsEnabled,
            errorMessage = nameError,
        )

        Spacer(modifier = Modifier.height(16.dp))

        LabeledTextField(
            label = "Apellido",
            value = lastnameValue,
            onValueChange = onLastnameChange,
            placeholder = "Escribe aquí",
            enabled = fieldsEnabled,
            errorMessage = lastnameError,
        )

        Spacer(modifier = Modifier.height(16.dp))

        PhoneField(
            countryCode = countryCode,
            phoneValue = phoneValue,
            onPhoneChange = onPhoneChange,
            onCountryCodeChange = onCountryCodeChange,
            enabled = fieldsEnabled,
            errorMessage = phoneError,
            placeholder = "1368536985",
        )

        //these only show up on external accounts
        if (isExternal) {
            Spacer(modifier = Modifier.height(16.dp))

            ProfileDropdownField(
                label = "País",
                selectedValue = countryValue,
                options = countryOptions,
                enabled = fieldsEnabled,
                onOptionSelected = onCountryChange,
            )

            Spacer(modifier = Modifier.height(16.dp))

            ProfileDropdownField(
                label = "Estado",
                selectedValue = stateValue,
                options = stateOptions,
                enabled = fieldsEnabled && countryValue.isNotBlank(),
                onOptionSelected = onStateChange,
            )

            Spacer(modifier = Modifier.height(16.dp))

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
}
