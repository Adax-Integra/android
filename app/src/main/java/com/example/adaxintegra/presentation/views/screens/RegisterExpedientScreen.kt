@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.example.adaxintegra.presentation.views.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.adaxintegra.domain.model.PersonalDataForm
import com.example.adaxintegra.presentation.viewmodel.ExpedientUiState
import com.example.adaxintegra.presentation.viewmodel.RegisterExpedientViewModel
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.Spacing
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.presentation.views.designsystem.organisms.AppHeader

@Suppress("ktlint:standard:function-naming")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterExpedientScreen(
    viewModel: RegisterExpedientViewModel,
    onCancel: () -> Unit,
    onSuccess: () -> Unit,
) {
    // Viewmodel state updates in screen
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Validates success in register
    if (uiState.isSuccess) {
        onSuccess()
    }

    Scaffold(
        topBar = {
            Box(modifier = Modifier.statusBarsPadding()) {
                AppHeader(
                    title = "Registrar Expediente",
                    subtitle = "Datos personales de la solicitante",
                    onBack = onCancel,
                )
            }
        },
    ) { paddingValues ->
        Box(
            modifier =
            Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            when {
                uiState.isLoadingCatalogs || uiState.isSubmitting -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                else -> {
                    PersonalDataStepContent(
                        uiState = uiState,
                        onDataChange = viewModel::onPersonalDataChange,
                        onSubmitClick = viewModel::onSubmitClick,
                    )
                }
            }
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun PersonalDataStepContent(
    uiState: ExpedientUiState,
    onDataChange: (PersonalDataForm) -> Unit,
    onSubmitClick: () -> Unit,
) {
    // Allows the screen to scroll down if the fields are large
    val scrollState = rememberScrollState()
    // Design System spacing rules
    val spacing = Spacing()

    // Files selector
    val filePickerLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent(),
        ) { uri: Uri? ->
            // When selecting a file, the route updates with uri
            if (uri != null) {
                onDataChange(uiState.personalData.copy(proofUri = uri))
            }
        }

    Column(
        modifier =
        Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = "Ingresa la información de la usuaria",
            style = AppTextStyle.BodyMedium,
            fontWeight = FontWeight.Normal,
        )

        // Name field
        OutlinedTextField(
            value = uiState.personalData.fullName,
            onValueChange = { newValue ->
                onDataChange(uiState.personalData.copy(fullName = newValue))
            },
            label = {
                Text(
                    text = "Nombre Completo *",
                    style = AppTextStyle.BodySmall,
                    fontWeight = FontWeight.Normal,
                )
            },
            modifier = Modifier.fillMaxWidth(),
            isError = uiState.personalDataErrors.containsKey("fullName"),
            supportingText = {
                uiState.personalDataErrors["fullName"]?.let { errorMsg ->
                    Text(
                        text = errorMsg,
                        style = AppTextStyle.LabelSmall,
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Normal,
                    )
                }
            },
        )

        // Phone number field
        OutlinedTextField(
            value = uiState.personalData.phoneNumber,
            onValueChange = { newValue ->
                onDataChange(uiState.personalData.copy(phoneNumber = newValue))
            },
            label = {
                Text(
                    text = "Teléfono de contacto",
                    style = AppTextStyle.BodySmall,
                    fontWeight = FontWeight.Normal,
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth(),
        )

        // Municipality field
        OutlinedTextField(
            value = uiState.personalData.municipality,
            onValueChange = { newValue ->
                onDataChange(uiState.personalData.copy(municipality = newValue))
            },
            label = {
                Text(
                    text = "Municipio *",
                    style = AppTextStyle.BodySmall,
                    fontWeight = FontWeight.Normal,
                )
            },
            modifier = Modifier.fillMaxWidth(),
            isError = uiState.personalDataErrors.containsKey("municipality"),
            supportingText = {
                uiState.personalDataErrors["municipality"]?.let { errorMsg ->
                    Text(
                        text = errorMsg,
                        style = AppTextStyle.LabelSmall,
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Normal,
                    )
                }
            },
        )

        Spacer(modifier = Modifier.height(8.dp))

        // File selector
        OutlinedButton(
            onClick = { filePickerLauncher.launch("image/*") }, // Opens the phone's gallery for image selecting
            modifier = Modifier.fillMaxWidth(),
        ) {
            val buttonText =
                if (uiState.personalData.proofUri != null) {
                    " Documento seleccionado"
                } else {
                    "Adjuntar comprobante / foto"
                }
            Text(
                text = buttonText,
                style = AppTextStyle.LabelMedium,
                fontWeight = FontWeight.Medium,
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Submit button
        Button(
            onClick = onSubmitClick,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = "Guardar Expediente",
                style = AppTextStyle.LabelMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun ConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Confirmar Registro",
                style = AppTextStyle.TitleMedium,
                fontWeight = FontWeight.Bold,
            )
        },
        text = {
            Text(
                text = "¿Estás segura de que deseas guardar este expediente?",
                style = AppTextStyle.BodyMedium,
                fontWeight = FontWeight.Normal,
            )
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(
                    text = "Confirmar",
                    style = AppTextStyle.LabelMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text(
                    text = "Cancelar",
                    style = AppTextStyle.LabelMedium,
                    fontWeight = FontWeight.Normal,
                )
            }
        },
    )
}
