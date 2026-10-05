package com.example.adaxintegra.presentation.views.designsystem.molecules

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppIcon
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextField
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.IconSize
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.ui.theme.AdaxIntegraTheme

// Molecule for country code selector (+52) combined with phone input field
@Suppress("ktlint:standard:function-naming")
@Composable
fun PhoneField(
    countryCode: String,
    phoneValue: String,
    onPhoneChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Teléfono celular",
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    errorMessage: String? = null,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = AppTextStyle.BodySmall,
            color = if (errorMessage != null) Color.Red else Color.Gray,
            fontWeight = FontWeight.Medium,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                modifier = Modifier.height(56.dp),
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = countryCode,
                        style = AppTextStyle.BodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black,
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    AppIcon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        size = IconSize.RegularIcon,
                        tint = Color.Gray,
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            AppTextField(
                value = phoneValue,
                onValueChange = onPhoneChange,
                placeholder = "4421234567",
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
                isError = errorMessage != null,
                modifier = Modifier.weight(1f),
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
private fun PhoneFieldPreview() {
    AdaxIntegraTheme {
        PhoneField(
            countryCode = "+52",
            phoneValue = "4421234567",
            onPhoneChange = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}
