package com.example.adaxintegra.presentation.views.designsystem.molecules

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.ui.theme.Black
import com.example.adaxintegra.ui.theme.Purple

//NV-01: header of the home, the brand over the greeting
@Suppress("ktlint:standard:function-naming")
@Composable
fun HomeHeader(
    modifier: Modifier = Modifier,
    greeting: String = "¡Hola!",
    message: String = "No estás sola, estamos aquí para acompañarte",
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "ADAX INTEGRA",
            style = AppTextStyle.TitleLarge,
            fontWeight = FontWeight.Black,
            color = Purple,
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = greeting,
            style = AppTextStyle.HeadLineLarge,
            fontWeight = FontWeight.Bold,
            color = Black,
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = message,
            style = AppTextStyle.BodyLarge,
            fontWeight = FontWeight.Normal,
            color = Purple,
        )
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
fun HomeHeaderPreview() {
    HomeHeader()
}
