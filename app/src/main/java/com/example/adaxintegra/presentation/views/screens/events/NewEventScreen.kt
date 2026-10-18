package com.example.adaxintegra.presentation.views.screens.events

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.presentation.viewmodel.NewEventUiState
import com.example.adaxintegra.presentation.views.designsystem.molecules.AppTimePickerDialog
import com.example.adaxintegra.presentation.views.designsystem.molecules.LabeledTextField
import com.example.adaxintegra.presentation.views.designsystem.templates.ScreenTemplate
import com.example.adaxintegra.ui.theme.AdaxIntegraTheme


@Suppress("ktlint:standard:function-naming")
@Composable
fun CreateEvent(
    modifier: Modifier = Modifier,
    uiState: NewEventUiState = NewEventUiState(),
    // Add the functionality for adding a file, photograph
    onBackClick: () -> Unit = {},
    onTitleChange: (String) -> Unit,

    ) {
    // To know if it needs to show the start date
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    // To know if it needs to show the time pickers
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }

    ScreenTemplate(
        title = "Nuevo evento",
        error = uiState.error,
        onBack = onBackClick,
        modifier = modifier,
    ) {
        val focusManager = LocalFocusManager.current

        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            LabeledTextField(
                label = "Título del evento",
                value = uiState.title,
                onValueChange = onTitleChange,
                placeholder = "Ej. Reunión de equipo",
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                errorMessage = uiState.titleError,
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Row for the date selectors
            Row(
                modifier = modifier,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ){

            }

            Spacer(modifier = Modifier.height(16.dp))

            // Row for the time selectors
            Row(
                modifier = modifier,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ){

            }

        }


    }
}

@Preview(showBackground = true)
@Composable
private fun CreateEventPreview() {
    AdaxIntegraTheme {
        CreateEvent(
            uiState = NewEventUiState(),
            onBackClick = {},
            onTitleChange = {},
        )
    }
}
