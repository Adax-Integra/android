package com.example.adaxintegra.presentation.views.designsystem.molecules

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextField
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text

@Suppress("ktlint:standard:function-naming")
@Composable
fun LabeledTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = AppTextStyle.BodySmall,
            color = Color.Gray,
            fontWeight = FontWeight.Medium,
        )
        Spacer(modifier = Modifier.height(8.dp))
        AppTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = placeholder,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
        )
    }
}
