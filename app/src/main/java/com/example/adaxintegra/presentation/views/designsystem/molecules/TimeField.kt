package com.example.adaxintegra.presentation.views.designsystem.molecules

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppIcon
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppIcons
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextField
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Suppress("ktlint:standard:function-naming")
@Composable
fun TimeField(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    value: LocalTime? = null,
    placeholder: String = "hh:mm",
    errorMessage: String? = null,
) {
    val displayValue = if (value != null) {
        value.format(DateTimeFormatter.ofPattern("HH:mm"))
    } else {
        ""
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = AppTextStyle.BodySmall,
            color = if (errorMessage != null) Color.Red else Color.Gray,
            fontWeight = FontWeight.Medium,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier.fillMaxWidth(),
        ) {
            AppTextField(
                value = displayValue,
                onValueChange = {},
                placeholder = placeholder,
                trailingIcon = {
                    IconButton(onClick = onClick) {
                        AppIcon(
                            imageVector = AppIcons.Clock,
                            contentDescription = "Seleccionar hora",
                        )
                    }
                },
                isError = errorMessage != null,
                readOnly = true,
            )
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable { onClick() },
            )
        }
        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = errorMessage,
                style = AppTextStyle.BodySmall,
                color = Color.Red,
                fontWeight = FontWeight.Normal,
            )
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
fun TimeFieldPreview() {
    TimeField(
        label = "Selecciona la hora",
        value = LocalTime.of(14, 30),
        onClick = {},
    )
}
