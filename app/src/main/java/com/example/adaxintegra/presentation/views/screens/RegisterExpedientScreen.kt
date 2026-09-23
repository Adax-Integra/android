@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.example.adaxintegra.presentation.views.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.adaxintegra.domain.model.CaseDataForm
import com.example.adaxintegra.domain.model.PersonalDataForm
import com.example.adaxintegra.presentation.viewmodel.ExpedientUiState
import com.example.adaxintegra.presentation.viewmodel.RegisterExpedientViewModel

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
            TopAppBar(
                title = { Text(if (uiState.currentStep == 1) "Paso 1: Datos Personales" else "Paso 2: Datos del Caso") },
                navigationIcon = {
                    // Cancel register button
                    IconButton(onClick = onCancel) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Cancelar")
                    }
                },
            )
        },
    ) { paddingValues ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
        ) {
            if (uiState.isLoadingCatalogs) {
                CircularProgressIndicator(modifier = Modifier.align(androidx.compose.ui.Alignment.Center))
            } else {
                // Alternates between step 1 and 2 depending on uiState
                when {
                    // If charging the catalogs or sending the form to the server
                    uiState.isLoadingCatalogs || uiState.isSubmitting -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }

                    // Shows step 1
                    uiState.currentStep == 1 -> {
                        PersonalDataStepContent(
                            uiState = uiState,
                            onDataChange = viewModel::onPersonalDataChange,
                            onNextClick = viewModel::onNextStepClick,
                        )
                    }

                    // Shows step 2
                    uiState.currentStep == 2 -> {
                        CaseDataStepContent(
                            uiState = uiState,
                            onDataChange = viewModel::onCaseDataChange,
                            onBackClick = viewModel::onPreviousStepClick,
                            onSubmitClick = viewModel::onSubmitClick,
                        )
                    }
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
    onNextClick: () -> Unit,
) {
    // Allows the screen to scroll down if the fields are large
    val scrollState = rememberScrollState()

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
            style = MaterialTheme.typography.bodyMedium,
        )

        // Name field
        OutlinedTextField(
            value = uiState.personalData.fullName,
            onValueChange = { newValue ->
                onDataChange(uiState.personalData.copy(fullName = newValue))
            },
            label = { Text("Nombre Completo *") },
            modifier = Modifier.fillMaxWidth(),
            isError = uiState.personalDataErrors.containsKey("fullName"),
            supportingText = {
                uiState.personalDataErrors["fullName"]?.let { errorMsg ->
                    Text(text = errorMsg, color = MaterialTheme.colorScheme.error)
                }
            },
        )

        // Phone number field
        OutlinedTextField(
            value = uiState.personalData.phoneNumber,
            onValueChange = { newValue ->
                onDataChange(uiState.personalData.copy(phoneNumber = newValue))
            },
            label = { Text("Teléfono de contacto") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth(),
        )

        // Municipality field
        OutlinedTextField(
            value = uiState.personalData.municipality,
            onValueChange = { newValue ->
                onDataChange(uiState.personalData.copy(municipality = newValue))
            },
            label = { Text("Municipio *") },
            modifier = Modifier.fillMaxWidth(),
            isError = uiState.personalDataErrors.containsKey("municipality"),
            supportingText = {
                uiState.personalDataErrors["municipality"]?.let { errorMsg ->
                    Text(text = errorMsg, color = MaterialTheme.colorScheme.error)
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
            Text(buttonText)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Continue button
        Button(
            onClick = onNextClick,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Continuar")
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun CaseDataStepContent(
    uiState: ExpedientUiState,
    onDataChange: (CaseDataForm) -> Unit,
    onBackClick: () -> Unit,
    onSubmitClick: () -> Unit,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = "Describe los detalles del caso o incidente reportado.",
            style = MaterialTheme.typography.bodyMedium,
        )

        // Violence type field
        OutlinedTextField(
            value = uiState.caseData.violenceType,
            onValueChange = { newValue ->
                onDataChange(uiState.caseData.copy(violenceType = newValue))
            },
            label = { Text("Tipo de Violencia *") },
            modifier = Modifier.fillMaxWidth(),
            isError = uiState.caseDataErrors.containsKey("violenceType"),
            supportingText = {
                uiState.caseDataErrors["violenceType"]?.let { errorMsg ->
                    Text(text = errorMsg, color = MaterialTheme.colorScheme.error)
                }
            },
        )

        // Case description field
        OutlinedTextField(
            value = uiState.caseData.caseDescription,
            onValueChange = { newValue ->
                onDataChange(uiState.caseData.copy(caseDescription = newValue))
            },
            label = { Text("Descripción del Caso *") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 4, // Allows to write larger texts comfortabely
            isError = uiState.caseDataErrors.containsKey("caseDescription"),
            supportingText = {
                uiState.caseDataErrors["caseDescription"]?.let { errorMsg ->
                    Text(text = errorMsg, color = MaterialTheme.colorScheme.error)
                }
            },
        )

        // Aditional notes field
        OutlinedTextField(
            value = uiState.caseData.additionalNotes,
            onValueChange = { newValue ->
                onDataChange(uiState.caseData.copy(additionalNotes = newValue))
            },
            label = { Text("Observaciones / Notas adicionales") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2,
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Back and save buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            OutlinedButton(
                onClick = onBackClick,
                modifier = Modifier.weight(1f),
            ) {
                Text("Atrás")
            }

            Button(
                onClick = onSubmitClick,
                modifier = Modifier.weight(1f),
            ) {
                Text("Guardar")
            }
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
        title = { Text("Confirmar Registro") },
        text = { Text("¿Estás segura de que deseas guardar este expediente?") },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        },
    )
}
