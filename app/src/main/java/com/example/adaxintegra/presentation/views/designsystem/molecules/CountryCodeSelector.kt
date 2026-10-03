package com.example.adaxintegra.presentation.views.designsystem.molecules

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text

// Country and its phone code
data class CountryCodeOption(
    val country: String,
    val code: String,
)

val CountryCodeOptions = listOf(
    CountryCodeOption(country = "México", code = "+52"),
    CountryCodeOption(country = "Estados Unidos / Canadá", code = "+1"),
    CountryCodeOption(country = "Colombia", code = "+57"),
    CountryCodeOption(country = "Venezuela", code = "+58"),
)

// Shows the selected code and opens the list of countries when tapped
@Suppress("ktlint:standard:function-naming")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryCodeSelector(
    selectedCode: String,
    onCodeSelected: (String) -> Unit,
    options: List<CountryCodeOption> = CountryCodeOptions,
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Row(
            modifier = Modifier
                .clickable { expanded = true }
                .padding(start = 12.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = selectedCode,
                style = AppTextStyle.BodyMedium,
                color = Color.Black,
                fontWeight = FontWeight.Medium,
            )
            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = "${option.country} (${option.code})",
                            style = AppTextStyle.BodyMedium,
                        )
                    },
                    onClick = {
                        onCodeSelected(option.code)
                        expanded = false
                    },
                )
            }
        }
    }
}
