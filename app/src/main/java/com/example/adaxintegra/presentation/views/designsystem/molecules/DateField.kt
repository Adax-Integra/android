package com.example.adaxintegra.presentation.views.designsystem.molecules

import android.text.format.DateFormat
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
import java.util.Calendar

@Suppress("ktlint:standard:function-naming")
@Composable
fun DateField(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    value: Calendar? = null,
    placeholder: String = "dd/mm/aaaa",
    errorMessage: String? = null,
) {
    val displayValue = if (value != null) {
        DateFormat.format("dd/MM/yyyy", value).toString()
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
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() },
        ) {
            AppTextField(
                value = displayValue,
                onValueChange = {},
                placeholder = placeholder,
                trailingIcon = {
                    IconButton(onClick = onClick) {
                        AppIcon(
                            imageVector = AppIcons.Calendar,
                            contentDescription = "Seleccionar fecha",
                        )
                    }
                },
                isError = errorMessage != null,
                readOnly = true,
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
fun DateFieldPreview() {
    DateField(
        label = "Selecciona la fecha",
        value = Calendar.getInstance(),
        onClick = {},
    )
}
