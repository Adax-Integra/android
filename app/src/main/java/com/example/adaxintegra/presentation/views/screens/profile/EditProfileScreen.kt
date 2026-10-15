package com.example.adaxintegra.presentation.views.screens.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
                    isEditing = !isEditing },
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

        EditProfileForm(
            nameValue = TODO(),
            onNameChange = TODO(),
            lastnameValue = TODO(),
            onLastnameChange = TODO(),
            countryCode = TODO(),
            onCountryCodeChange = TODO(),
            phoneValue = TODO(),
            onPhoneChange = TODO(),
            isExternal = TODO(),
            countryValue = TODO(),
            onCountryChange = TODO(),
            countryOptions = TODO(),
            stateValue = TODO(),
            onStateChange = TODO(),
            stateOptions = TODO(),
            municipalityValue = TODO(),
            onMunicipalityChange = TODO(),
            isLoading = TODO(),
            isEditing = TODO(),
            modifier = TODO(),
            nameError = TODO(),
            lastnameError = TODO(),
            phoneError = TODO(),
            countryError = TODO(),
            stateError = TODO(),
            municipalityError = TODO()
        )

    }
}
