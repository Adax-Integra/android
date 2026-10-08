package com.example.adaxintegra.presentation.views.designsystem.molecules

import android.text.format.DateFormat
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppIcon
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppIcons
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextField
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import java.util.Calendar

@Suppress("ktlint:standard:function-naming")
@Composable
fun DateField(
    label: String,
    modifier: Modifier = Modifier,
    value: Calendar? = null,
    onValueChange: (String) -> Unit,
    placeholderDate: Calendar? = null,
    errorMessage: String? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // Text that appears above the date field
        Text(
            text = label,
            style = AppTextStyle.BodySmall,
            color = if (errorMessage != null) Color.Red else Color.Gray,
            fontWeight = FontWeight.Medium,
        )
        Spacer(modifier = Modifier.height(8.dp))
        // Formatter that transforms the Calendar object into readable dates
        AppTextField(
            value = DateFormat.format("dd/MM/yyyy", value).toString(),
            onValueChange = onValueChange,
            placeholder = placeholderDate?.toInstant()?.atZone(java.time.ZoneId.systemDefault())
                ?.toLocalDate()
                .toString(),
            trailingIcon = trailingIcon,
            visualTransformation = visualTransformation,
            isError = errorMessage != null,
        )
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
fun DateFieldPreview() {
    DateField(
        label = "Selecciona la fecha",
        value = Calendar.Builder().setDate(2026, 10, 10).build(),
        onValueChange = {},
        placeholderDate = Calendar.Builder().setDate(2023, 10, 10).build(),
        trailingIcon = {
            AppIcon(
                imageVector = AppIcons.Calendar,
            )
        },
    )
}
