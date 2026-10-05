package com.example.adaxintegra.presentation.views.designsystem.atoms

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.R

// Atom component displaying the official Adax Integra logo image resource
@Suppress("ktlint:standard:function-naming")
@Composable
fun AdaxLogo(
    modifier: Modifier = Modifier,
    size: Dp = 100.dp,
) {
    Image(
        painter = painterResource(id = R.drawable.ic_adax_logo),
        contentDescription = "Logo ADAX INTEGRA",
        modifier = modifier.size(size),
    )
}
