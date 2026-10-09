package com.example.adaxintegra.presentation.views.designsystem.atoms

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.adaxintegra.ui.theme.Black

//Nv-01: heading that opens a section of the home ("Eventos", "Podcast")
@Suppress("ktlint:standard:function-naming")
@Composable
fun SectionTitle(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        modifier = modifier,
        style = AppTextStyle.TitleLarge,
        fontWeight = FontWeight.Bold,
        color = Black,
    )
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
fun SectionTitlePreview() {
    Column {
        SectionTitle(text = "Eventos")
    }
}
