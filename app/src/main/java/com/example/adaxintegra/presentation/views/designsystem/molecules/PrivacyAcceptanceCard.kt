package com.example.adaxintegra.presentation.views.designsystem.molecules

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.ui.theme.AdaxIntegraTheme
import com.example.adaxintegra.ui.theme.Purple

@Suppress("ktlint:standard:function-naming")
@Composable
fun PrivacyAcceptanceCard(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    onPrivacyClick: () -> Unit = {},
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onCheckedChange(!checked) }
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Checkbox(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = Purple,
                    uncheckedColor = Color(0xFF9E9E9E),
                    checkmarkColor = Color.White
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 4.dp)
            ) {
                Text(
                    text = "He leído y acepto el",
                    style = AppTextStyle.BodyMedium,
                    color = Color.Black,
                    fontWeight = FontWeight.Normal
                )

                Text(
                    text = "Aviso de privacidad",
                    style = AppTextStyle.BodyMedium,
                    color = Purple,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable { onPrivacyClick() }
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Al seleccionar esta casilla acepto el tratamiento de mis datos personales conforme al aviso.",
                    style = AppTextStyle.BodySmall,
                    color = Color(0xFF757575)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PrivacyAcceptanceCardPreview() {
    AdaxIntegraTheme {
        PrivacyAcceptanceCard(
            checked = false,
            onCheckedChange = {},
            onPrivacyClick = {}
        )
    }
}
