package com.example.adaxintegra.presentation.views.designsystem.molecules

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppIcon
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppIcons
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextField
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.ui.theme.Purple
import java.text.Normalizer

// "Querétaro" and "queretaro" match the same option
private fun String.searchKey(): String = Normalizer.normalize(this, Normalizer.Form.NFD)
    .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
    .lowercase()

// G-07: closed list with a search box
// The value can only come from the list, so it always matches the catalog
@Suppress("ktlint:standard:function-naming")
@Composable
fun SearchableDropdownField(
    label: String,
    prompt: String,
    options: List<String>,
    selectedOption: String?,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    // When non-null, the field gets a red outline and the message below it
    errorMessage: String? = null,
) {
    var isOpen by rememberSaveable { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = AppTextStyle.BodySmall,
            color = if (errorMessage != null) Color.Red else Color.Gray,
            fontWeight = FontWeight.Medium,
        )
        Spacer(modifier = Modifier.height(8.dp))

        Surface(
            onClick = { isOpen = true },
            enabled = enabled,
            shape = RoundedCornerShape(16.dp),
            color = if (enabled) Color.White else Color.White.copy(alpha = 0.6f),
            border = BorderStroke(1.dp, if (errorMessage != null) Color.Red else Color.LightGray),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = selectedOption ?: prompt,
                    modifier = Modifier.weight(1f),
                    style = AppTextStyle.BodyMedium,
                    color = if (selectedOption == null || !enabled) Color.Gray else Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    contentDescription = null,
                    tint = Color.Gray,
                )
            }
        }

        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = errorMessage,
                style = AppTextStyle.BodySmall,
                color = Color.Red,
            )
        }
    }

    if (isOpen) {
        OptionsSheet(
            title = label,
            options = options,
            selectedOption = selectedOption,
            onSelect = { option ->
                onOptionSelected(option)
                isOpen = false
            },
            onDismiss = { isOpen = false },
        )
    }
}

// Full height sheet with the search box and the options
@OptIn(ExperimentalMaterial3Api::class)
@Suppress("ktlint:standard:function-naming")
@Composable
private fun OptionsSheet(
    title: String,
    options: List<String>,
    selectedOption: String?,
    onSelect: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var query by rememberSaveable { mutableStateOf("") }
    val filtered = remember(query, options) {
        val key = query.trim().searchKey()
        if (key.isEmpty()) options else options.filter { it.searchKey().contains(key) }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = title,
                    modifier = Modifier.weight(1f),
                    style = AppTextStyle.TitleMedium,
                    fontWeight = FontWeight.Bold,
                )
                TextButton(onClick = onDismiss) {
                    Text(text = "Cancelar", color = Purple, fontWeight = FontWeight.SemiBold)
                }
            }

            AppTextField(
                value = query,
                onValueChange = { query = it },
                placeholder = "Buscar...",
                leadingIcon = {
                    AppIcon(imageVector = AppIcons.Search, contentDescription = null, tint = Color.Gray)
                },
            )

            if (filtered.isEmpty()) {
                Text(
                    text = "Sin resultados",
                    modifier = Modifier.padding(vertical = 16.dp),
                    style = AppTextStyle.BodyMedium,
                    color = Color.Gray,
                )
            }

            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(filtered) { option ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelect(option) }
                            .padding(vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = option,
                            modifier = Modifier.weight(1f),
                            style = AppTextStyle.BodyLarge,
                        )
                        if (option == selectedOption) {
                            AppIcon(imageVector = AppIcons.CheckMark, contentDescription = "Seleccionado", tint = Purple)
                        }
                    }
                    HorizontalDivider()
                }
            }
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
private fun SearchableDropdownFieldPreview() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        SearchableDropdownField(
            label = "País",
            prompt = "Selecciona un país",
            options = listOf("México", "Colombia"),
            selectedOption = "México",
            onOptionSelected = {},
        )
        SearchableDropdownField(
            label = "Estado",
            prompt = "Selecciona un estado",
            options = emptyList(),
            selectedOption = null,
            onOptionSelected = {},
            errorMessage = "Selecciona un estado",
        )
    }
}
