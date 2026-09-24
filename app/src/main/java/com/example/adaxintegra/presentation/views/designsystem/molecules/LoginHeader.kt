package com.example.adaxintegra.presentation.views.designsystem.molecules

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mail
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppIcon
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.IconSize
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.ui.theme.LightPurple
import com.example.adaxintegra.ui.theme.Purple

@Suppress("ktlint:standard:function-naming")
@Composable
fun LoginHeader(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AppIcon(
            imageVector = Icons.Default.Mail,
            contentDescription = "Logo",
            size = IconSize.LargeIcon,
            tint = Purple,
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Bienvenida a",
            style = AppTextStyle.TitleLarge,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.fillMaxWidth(),
        )
        Text(
            text = "ADAX INTEGRA",
            style = AppTextStyle.TitleLarge,
            fontWeight = FontWeight.Black,
            color = Purple,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Accede a tu cuenta para continuar",
            style = AppTextStyle.BodyMedium,
            fontWeight = FontWeight.Normal,
            color = LightPurple,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
