package com.example.adaxintegra.presentation.views.designsystem.molecules

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text

//Label + closed list of options (no free text), like the SÍ/No of R-02
// Unlike AutoCompleteOutlinedTextField the value can only come from the list
@Suppress ("ktlint:standard:function-naming")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabeledDropdownField(
    label: String,
    selectedOption: String?,
    options: List<String>,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Selecciona una opción",
    error: String? = null,
){
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()){
        Text(
            text = label,
            style = AppTextStyle.BodySmall,
            color = Color.Gray,
            fontWeight = FontWeight.Medium,
        )

        Spacer(modifier = Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it },
        ) {
            //readOnly keeps the keyboard closed and the value inside the options
            OutlinedTextField(
                value = selectedOption ?: "",
                onValueChange = {},
                readOnly = true,
                placeholder = {
                    Text(
                        text = placeholder,
                        style = AppTextStyle.BodyMedium,
                        color = Color.Gray,
                    )
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = Color.LightGray,
                    unfocusedBorderColor = Color.LightGray,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                ),
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ){
                options.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(text = option, style = AppTextStyle.BodyMedium)
                        },
                        onClick = {
                            onOptionSelected(option)
                            expanded = false
                        },
                    )
                }
            }
        }

        if (error != null){
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = error,
                style = AppTextStyle.LabelSmall,
                color = MaterialTheme.colorScheme.error,
            )
        }
    }
}
