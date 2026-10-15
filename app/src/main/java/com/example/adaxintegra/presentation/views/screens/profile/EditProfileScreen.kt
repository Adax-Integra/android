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

@Suppress("ktlint:standard:function-naming")
@Composable
fun EditProfileScreen(
    onBack: () -> Unit,
    isExternal: Boolean,
    viewModel: EditProfileViewModel = hiltViewModel(),
) {
    /*val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var isEditing by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("México") }
    var state by remember { mutableStateOf("México") }
    var municipality by remember { mutableStateOf("") }*/

    ScreenTemplate(
        title = "Tu Perfil",
        onBack = onBack,
        /*trailingAction = {
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
        }*/
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            //pull to refresh

            //cards with info that can be typed into when clicking edit button

            //name
           /* LabeledTextField(
                label = "Nombre",
                value = name,
                onValueChange = { name = it },
                enabled = isEditing,
            )

            //last name
            LabeledTextField(
                label = "Apellidos",
                value = lastName,
                onValueChange = { lastName = it },
                enabled = isEditing,
            )*/

            //phone

            //only shown to external
            //country

            //state

            //municipality


            //button to save changes only appears when clicking edit button

        }
    }
}
