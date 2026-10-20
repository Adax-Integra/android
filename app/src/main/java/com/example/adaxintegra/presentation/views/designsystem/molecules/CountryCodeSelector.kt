package com.example.adaxintegra.presentation.views.designsystem.molecules

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
    modifier: Modifier = Modifier,
    label: String = "Código",
    options: List<CountryCodeOption> = CountryCodeOptions,
    errorMessage: String? = null,
    enabled: Boolean = true,
) {
    var expanded by remember { mutableStateOf(false) }

    val shape = RoundedCornerShape(16.dp)
    val colors = MaterialTheme.colorScheme

    val borderColor = when {
        errorMessage != null -> Color.Red
        !enabled -> Color.LightGray
        else -> Color.LightGray
    }

    val backgroundColor = if (enabled) {
        Color.White
    } else {
        Color(0xFFF2F2F2)
    }

    val contentColor = if (enabled) {
        Color.Black
    } else {
        Color.Gray
    }

    Column (modifier = modifier) {
        Text(
            text = label,
            style = AppTextStyle.BodySmall,
            color = if (errorMessage != null) Color.Red
            else Color.Gray,
            fontWeight = FontWeight.Medium,
        )

        Spacer(modifier = Modifier.height(8.dp))
        Box {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(shape)
                    .background(backgroundColor)
                    .border(
                        width = 1.dp,
                        color = borderColor,
                        shape = shape,
                    )
                    .clickable(enabled = enabled) {
                        expanded = true
                    }
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = selectedCode,
                    style = AppTextStyle.BodyMedium,
                    color = contentColor,
                    fontWeight = FontWeight.Medium,
                )

                Spacer(modifier = Modifier.width(4.dp))

                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded,
                )
            }

            DropdownMenu(
                expanded = expanded && enabled,
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

        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = errorMessage,
                style = AppTextStyle.BodySmall,
                color = Color.Red,
            )
        }
    }
}
