package com.example.adaxintegra.presentation.views.designsystem.molecules

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppIcon
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppIcons
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.ui.theme.White

// search bar
@Suppress("ktlint:standard:function-naming")
@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text(text = "Buscar por nombre o caso...", fontWeight = FontWeight.Normal) },
        leadingIcon = { AppIcon(imageVector = AppIcons.Search, contentDescription = null) },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors =
            OutlinedTextFieldDefaults.colors(
                focusedContainerColor = White,
                unfocusedContainerColor = White,
            ),
        modifier = Modifier.fillMaxWidth(),
    )
}
