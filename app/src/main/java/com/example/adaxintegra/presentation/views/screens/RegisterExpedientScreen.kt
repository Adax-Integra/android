@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.example.adaxintegra.presentation.views.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.adaxintegra.domain.model.PersonalDataForm
import com.example.adaxintegra.presentation.viewmodel.RegisterExpedientUiState
import com.example.adaxintegra.presentation.viewmodel.RegisterExpedientViewModel
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.Spacing
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.presentation.views.designsystem.molecules.AutoCompleteOutlinedTextField
import com.example.adaxintegra.presentation.views.designsystem.organisms.AppHeader

// Data catálog of states for smart autocomplete
private val MEXICAN_STATES = listOf(
    "Aguascalientes", "Baja California", "Baja California Sur", "Campeche", "Chiapas",
    "Chihuahua", "Ciudad de México", "Coahuila", "Colima", "Durango", "Guanajuato",
    "Guerrero", "Hidalgo", "Jalisco", "Estado de México", "Michoacán", "Morelos",
    "Nayarit", "Nuevo León", "Oaxaca", "Puebla", "Querétaro", "Quintana Roo",
    "San Luis Potosí", "Sinaloa", "Sonora", "Tabasco", "Tamaulipas", "Tlaxcala",
    "Veracruz", "Yucatán", "Zacatecas",
)

private val COUNTRIES = listOf("México", "Otro")
private val PHONE_PREFIXES = listOf("+52", "+1", "+34", "+57", "+54", "+56", "+51", "+502", "+503", "+504")

@Suppress("ktlint:standard:function-naming")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterExpedientScreen(
    viewModel: RegisterExpedientViewModel,
    userToken: String,
    onCancel: () -> Unit,
    onSuccess: () -> Unit,
) {
    // Viewmodel state updates in screen
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Validates success in register
    if (uiState.isSuccess) {
        onSuccess()
    }

    if (uiState.showConfirmationDialog) {
        ConfirmationDialog(
            onConfirm = { viewModel.onConfirmSubmit(userToken) },
            onDismiss = viewModel::onDismissDialog,
        )
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
    uiState: RegisterExpedientUiState,
    onDataChange: (PersonalDataForm) -> Unit,
    onSubmitClick: () -> Unit,
) {
    // Allows the screen to scroll down if the fields are large
    val scrollState = rememberScrollState()
    // Design System spacing rules
    val spacing = Spacing()
    val data = uiState.personalData

    Column(
        modifier =
        Modifier
            .fillMaxSize()
            .imePadding()
            .padding(spacing.medium)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        // Profile data
        Text(
            text = "Ingresa la información de la usuaria",
            style = AppTextStyle.TitleMedium,
            fontWeight = FontWeight.Bold,
        )

        // Name field
        OutlinedTextField(
            value = data.name,
            onValueChange = { onDataChange(data.copy(name = it)) },
            label = {
                Text(
                    text = "Nombre(s) *",
                    style = AppTextStyle.BodySmall,
                    fontWeight = FontWeight.Normal,
                )
            },
            modifier = Modifier.fillMaxWidth(),
            isError = uiState.personalDataErrors.containsKey("name"),
            supportingText = {
                uiState.personalDataErrors["name"]?.let { errorMsg ->
                    Text(
                        text = errorMsg,
                        style = AppTextStyle.LabelSmall,
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Normal,
                    )
                }
            },
        )

        // Last name field
        OutlinedTextField(
            value = data.lastName,
            onValueChange = { onDataChange(data.copy(lastName = it)) },
            label = { Text("Apellidos *", style = AppTextStyle.BodySmall, fontWeight = FontWeight.Normal) },
            modifier = Modifier.fillMaxWidth(),
            isError = uiState.personalDataErrors.containsKey("lastName"),
            supportingText = uiState.personalDataErrors["lastName"]?.let { error ->
                { Text(error, style = AppTextStyle.LabelSmall, color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Normal) }
            },
        )

        // Email field
        OutlinedTextField(
            value = data.email,
            onValueChange = { onDataChange(data.copy(email = it)) },
            label = { Text("Correo Electrónico *", style = AppTextStyle.BodySmall, fontWeight = FontWeight.Normal) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth(),
            isError = uiState.personalDataErrors.containsKey("email"),
            supportingText = uiState.personalDataErrors["email"]?.let { error ->
                { Text(error, style = AppTextStyle.LabelSmall, color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Normal) }
            },
        )

        // Birthdate field
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing.small)
        ) {
            var prefixExpanded by remember { mutableStateOf(false) }

            @OptIn(ExperimentalMaterial3Api::class)
            ExposedDropdownMenuBox(
                expanded = prefixExpanded,
                onExpandedChange = { prefixExpanded = !prefixExpanded },
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    value = data.phonePrefix,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Lada", style = AppTextStyle.BodySmall) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = prefixExpanded) },
                    modifier = Modifier.menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = prefixExpanded,
                    onDismissRequest = { prefixExpanded = false }
                ) {
                    PHONE_PREFIXES.forEach { prefix ->
                        DropdownMenuItem(
                            text = { Text(prefix) },
                            onClick = {
                                onDataChange(data.copy(phonePrefix = prefix))
                                prefixExpanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = data.phone,
                onValueChange = { onDataChange(data.copy(phone = it)) },
                label = { Text("Teléfono (10 dígitos)", style = AppTextStyle.BodySmall, fontWeight = FontWeight.Normal) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.weight(2.2f),
                isError = uiState.personalDataErrors.containsKey("phone"),
                supportingText = {
                    uiState.personalDataErrors["phone"]?.let { errorMsg ->
                        Text(text = errorMsg, style = AppTextStyle.LabelSmall, color = MaterialTheme.colorScheme.error)
                    }
                }
            )
        }
        Spacer(modifier = Modifier.height(spacing.small))

        // Location
        Text(
            text = "Dirección y Ubicación",
            style = AppTextStyle.TitleMedium,
            fontWeight = FontWeight.Bold,
        )

        OutlinedTextField(
            value = data.addressLine1,
            onValueChange = { onDataChange(data.copy(addressLine1 = it)) },
            label = { Text("Calle y número exterior *", style = AppTextStyle.BodySmall, fontWeight = FontWeight.Normal) },
            modifier = Modifier.fillMaxWidth(),
            isError = uiState.personalDataErrors.containsKey("addressLine1"),
        )

        OutlinedTextField(
            value = data.addressLine2,
            onValueChange = { onDataChange(data.copy(addressLine2 = it)) },
            label = { Text("Num. Interior / Ref. (Opcional)", style = AppTextStyle.BodySmall, fontWeight = FontWeight.Normal) },
            modifier = Modifier.fillMaxWidth(),
        )

        Row(horizontalArrangement = Arrangement.spacedBy(spacing.small)) {
            OutlinedTextField(
                value = data.neighborhood,
                onValueChange = { onDataChange(data.copy(neighborhood = it)) },
                label = { Text("Colonia *", style = AppTextStyle.BodySmall, fontWeight = FontWeight.Normal) },
                modifier = Modifier.weight(1f),
                isError = uiState.personalDataErrors.containsKey("neighborhood"),
            )
            OutlinedTextField(
                value = data.zipCode,
                onValueChange = { onDataChange(data.copy(zipCode = it)) },
                label = { Text("C.P. *", style = AppTextStyle.BodySmall, fontWeight = FontWeight.Normal) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f),
                isError = uiState.personalDataErrors.containsKey("zipCode"),
            )
        }

        AutoCompleteOutlinedTextField(
            value = data.country,
            onValueChange = { onDataChange(data.copy(country = it)) },
            label = "País *",
            options = COUNTRIES,
            isError = uiState.personalDataErrors.containsKey("country"),
        )

        val isStateEnabled = data.country != "Otro"

        AutoCompleteOutlinedTextField(
            value = if (isStateEnabled) data.state else "No aplica (Otro país)",
            onValueChange = { if (isStateEnabled) onDataChange(data.copy(state = it)) },
            label = if (isStateEnabled) "Estado *" else "Estado (No requerido)",
            options = if (isStateEnabled) MEXICAN_STATES else emptyList(),
            isError = uiState.personalDataErrors.containsKey("state"),
            enabled = isStateEnabled
        )

        OutlinedTextField(
            value = data.city,
            onValueChange = { onDataChange(data.copy(city = it)) },
            label = { Text("Ciudad / Municipio *", style = AppTextStyle.BodySmall, fontWeight = FontWeight.Normal) },
            modifier = Modifier.fillMaxWidth(),
            isError = uiState.personalDataErrors.containsKey("city"),
        )

        uiState.errorMessage?.let { error ->
            Text(
                text = error,
                style = AppTextStyle.BodySmall,
                color = MaterialTheme.colorScheme.error,
                fontWeight = FontWeight.Medium,
            )
        }

        Spacer(modifier = Modifier.height(spacing.medium))

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
