package com.example.adaxintegra.presentation.views.screens.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.adaxintegra.presentation.viewmodel.EditProfileViewModel
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppButton
import com.example.adaxintegra.presentation.views.designsystem.molecules.LabeledTextField
import com.example.adaxintegra.presentation.views.designsystem.templates.ScreenTemplate
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppIcon
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppIcons
import com.example.adaxintegra.presentation.views.designsystem.atoms.IconSize
import com.example.adaxintegra.presentation.views.designsystem.organisms.EditProfileForm

@Suppress("ktlint:standard:function-naming")
@Composable
fun EditProfileScreen(
    onBack: () -> Unit,
    isExternal: Boolean,
    viewModel: EditProfileViewModel = hiltViewModel(),
) {

    var isEditing by remember { mutableStateOf(false) }

    var name by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var countryCode by remember { mutableStateOf("+52") }

    var country by remember { mutableStateOf("México") }
    var state by remember { mutableStateOf("Querétaro") }
    var municipality by remember { mutableStateOf("") }

    //temp values used to restore form on cancel
    var originalName by remember { mutableStateOf("") }
    var originalLastName by remember { mutableStateOf("") }
    var originalPhone by remember { mutableStateOf("") }
    var originalCountryCode by remember { mutableStateOf("+52") }
    var originalCountry by remember { mutableStateOf("México") }
    var originalState by remember { mutableStateOf("Querétaro") }
    var originalMunicipality by remember { mutableStateOf("") }

    ScreenTemplate(
        title = "Tu Perfil",
        onBack = onBack,
        trailingAction = {
            IconButton(
                onClick = {
                    if (isEditing) {
                        //restore values from before editing when canceling
                        name = originalName
                        lastName = originalLastName
                        phone = originalPhone
                        countryCode = originalCountryCode
                        country = originalCountry
                        state = originalState
                        municipality = originalMunicipality

                        isEditing = false
                    } else {
                        //original values are saved so that if the process is canceled they reappear
                        originalName = name
                        originalLastName = lastName
                        originalPhone = phone
                        originalCountryCode = countryCode
                        originalCountry = country
                        originalState = state
                        originalMunicipality = municipality

                        isEditing = true
                    }
                },
            ) {
                AppIcon(
                    imageVector = if (isEditing) {
                        AppIcons.Close
                    } else {
                        AppIcons.Edit
                    },
                    contentDescription = if (isEditing) {
                        "Cancelar edición"
                    } else {
                        "Editar perfil"
                    },
                    size = IconSize.LargeIcon,
                )
            }
        }
    ) {

        val stateOptions = when (country) {
            "México" -> listOf("Querétaro", "Jalisco", "Puebla", "Yucatán")
            "Estados Unidos" -> listOf("California", "Texas", "Florida")
            "Canadá" -> listOf("Ontario", "Quebec", "Alberta")
            "Colombia" -> listOf("Antioquia", "Cundinamarca", "Valle del Cauca")
            "Venezuela" -> listOf("Carabobo", "Miranda", "Zulia")
            else -> emptyList()
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            EditProfileForm(
                nameValue = name,
                onNameChange = { name = it },
                lastnameValue = lastName,
                onLastnameChange = { lastName = it },
                countryCode = countryCode,
                onCountryCodeChange = { countryCode = it },
                phoneValue = phone,
                onPhoneChange = { phone = it },
                isExternal = isExternal,
                countryValue = country,
                onCountryChange = { newCountry ->
                    country = newCountry
                    state = ""
                    municipality = ""
                },
                countryOptions = listOf(
                    "México",
                    "Estados Unidos",
                    "Canadá",
                    "Colombia",
                    "Venezuela",
                ),
                stateValue = state,
                onStateChange = { state = it },
                stateOptions = stateOptions,
                municipalityValue = municipality,
                onMunicipalityChange = { municipality = it },
                isLoading = TODO(),
                isEditing = isEditing,
                modifier = Modifier.fillMaxWidth(),
            )

            if (isEditing) {
                Spacer(modifier = Modifier.height(24.dp))

                AppButton(
                    onClick = {
                        //backend integration will go here later.
                        isEditing = false
                    },
                    enabled = TODO(),
                    modifier = Modifier.fillMaxWidth(),
                    text = "Guardar cambios",
                    )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
