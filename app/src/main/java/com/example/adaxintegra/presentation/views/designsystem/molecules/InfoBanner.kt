package com.example.adaxintegra.presentation.views.designsystem.molecules

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppIcon
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppIcons
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.ui.theme.Purple

// G-07 / B-02: light purple box with the info icon, above a form
@Suppress("ktlint:standard:function-naming")
@Composable
fun InfoBanner(
    text: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Purple.copy(alpha = 0.08f), RoundedCornerShape(12.dp))
            .padding(14.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        AppIcon(imageVector = AppIcons.Info, tint = Purple)
        Text(text = text, style = AppTextStyle.BodySmall)
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
private fun InfoBannerPreview() {
    InfoBanner(text = "La decisión queda en la bitácora y se le avisa por correo.")
}
