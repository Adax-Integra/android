package com.example.adaxintegra.presentation.views.designsystem.organisms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.domain.model.MAX_CASE_DESCRIPTION_LENGTH
import com.example.adaxintegra.domain.model.MAX_CASE_HELPS_WANTED_LENGTH
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppButton
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.ButtonVariant
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.presentation.views.designsystem.molecules.LabeledDropdownField
import com.example.adaxintegra.presentation.views.designsystem.molecules.LabeledTextArea
import com.example.adaxintegra.ui.theme.AdaxIntegraTheme

// Labels of the Sí/No dropdown
private const val OPTION_YES = "Sí"
private const val OPTION_NO = "No"

// R-02: "Registrar el caso" form
@Suppress("ktlint:standard:function-naming")
@Composable
fun CreateCaseForm(
    writtenDescription: String,
    writtenHelpsWanted: String,
    hasExternalSupport: Boolean?,
    fieldErrors: Map<String, String>,
    generalError: String?,
    canSave: Boolean,
    isSaving: Boolean,
    onDescriptionChange: (String) -> Unit,
    onHelpsWantedChange: (String) -> Unit,
    onExternalSupportChange: (Boolean) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        LabeledTextArea(
            label = "DESCRIPCIÓN DEL CASO",
            value = writtenDescription,
            onValueChange = onDescriptionChange,
            maxLength = MAX_CASE_DESCRIPTION_LENGTH,
            placeholder = "Cuéntanos qué pasó, con el detalle que quieras compartir.",
            minLines = 6,
            error = fieldErrors["writtenDescription"],
        )

        LabeledTextArea(
            label = "¿QUÉ AYUDA ESPERAS RECIBIR?",
            value = writtenHelpsWanted,
            onValueChange = onHelpsWantedChange,
            maxLength = MAX_CASE_HELPS_WANTED_LENGTH,
            placeholder = "Por ejemplo: asesoría legal, acompañamiento psicológico...",
            minLines = 3,
            error = fieldErrors["writtenHelpsWanted"],
        )

        LabeledDropdownField(
            label = "¿CUENTAS CON APOYO LEGAL EXTERNO?",
            selectedOption = hasExternalSupport?.let { if (it) OPTION_YES else OPTION_NO },
            options = listOf(OPTION_YES, OPTION_NO),
            onOptionSelected = { option -> onExternalSupportChange(option == OPTION_YES) },
            placeholder = "Selecciona Sí o No",
            error = fieldErrors["hasExternalSupport"],
        )

        if (generalError != null) {
            Text(
                text = generalError,
                style = AppTextStyle.BodySmall,
                color = MaterialTheme.colorScheme.error,
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            AppButton(
                text = "Guardar",
                onClick = onSave,
                modifier = Modifier.weight(1f),
                enabled = canSave,
                isLoading = isSaving,
            )

            AppButton(
                text = "Cancelar",
                onClick = onCancel,
                modifier = Modifier.weight(1f),
                variant = ButtonVariant.Outlined,
                enabled = !isSaving,
            )
        }
    }
}

@Suppress("ktlint:standard:function-naming")
// Preview: empty form ("Guardar" disabled)
@Preview(showBackground = true, widthDp = 360)
@Composable
fun CreateCaseFormEmptyPreview() {
    AdaxIntegraTheme(dynamicColor = false) {
        CreateCaseForm(
            writtenDescription = "",
            writtenHelpsWanted = "",
            hasExternalSupport = null,
            fieldErrors = emptyMap(),
            generalError = null,
            canSave = false,
            isSaving = false,
            onDescriptionChange = {},
            onHelpsWantedChange = {},
            onExternalSupportChange = {},
            onSave = {},
            onCancel = {},
        )
    }
}

@Suppress("ktlint:standard:function-naming")
// Preview: form with the fields the server flagged
@Preview(showBackground = true, widthDp = 360)
@Composable
fun CreateCaseFormErrorsPreview() {
    AdaxIntegraTheme(dynamicColor = false) {
        CreateCaseForm(
            writtenDescription = "   ",
            writtenHelpsWanted = "Asesoría legal",
            hasExternalSupport = false,
            fieldErrors = mapOf(
                "writtenDescription" to "La descripción del caso es obligatoria.",
            ),
            generalError = null,
            canSave = false,
            isSaving = false,
            onDescriptionChange = {},
            onHelpsWantedChange = {},
            onExternalSupportChange = {},
            onSave = {},
            onCancel = {},
        )
    }
}
