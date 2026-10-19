package com.example.adaxintegra.presentation.views.designsystem.molecules

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.ui.theme.IconGrey

// G-07: read only value with its label on top (e.g. "PAÍS" / "México"), like the iOS InfoRow
@Suppress("ktlint:standard:function-naming")
@Composable
fun ProfileInfoRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Text(
            text = label.uppercase(),
            style = AppTextStyle.BodyMedium,
            color = IconGrey,
        )
        Text(
            text = value,
            style = AppTextStyle.BodyLarge,
        )
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
private fun ProfileInfoRowPreview() {
    ProfileInfoRow(label = "Municipio/Localidad", value = "Querétaro")
}
