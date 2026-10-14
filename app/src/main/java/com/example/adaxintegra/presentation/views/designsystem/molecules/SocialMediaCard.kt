package com.example.adaxintegra.presentation.views.designsystem.molecules

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.R
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.ui.theme.Purple
import com.example.adaxintegra.ui.theme.White

// NV-01: card of the home that links to the social networks as a row of logos
@Suppress("ktlint:standard:function-naming")
@Composable
fun SocialMediaCard(
    onFacebookClick: () -> Unit,
    onInstagramClick: () -> Unit,
    onXClick: () -> Unit,
    onTikTokClick: () -> Unit,
    modifier: Modifier = Modifier,
    description: String = "Consulta nuestras redes sociales para conocer nuestras actividades y novedades.",
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(White)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = description,
            style = AppTextStyle.BodyLarge,
            fontWeight = FontWeight.Normal,
            color = Purple,
        )

        Row(modifier = Modifier.fillMaxWidth()) {
            SocialMediaButton(
                logo = R.drawable.ic_facebook,
                name = "Facebook",
                onClick = onFacebookClick,
                modifier = Modifier.weight(1f),
            )

            SocialMediaButton(
                logo = R.drawable.ic_instagram,
                name = "Instagram",
                onClick = onInstagramClick,
                modifier = Modifier.weight(1f),
            )

            SocialMediaButton(
                logo = R.drawable.ic_x,
                name = "X",
                onClick = onXClick,
                modifier = Modifier.weight(1f),
            )

            SocialMediaButton(
                logo = R.drawable.ic_tiktok,
                name = "TikTok",
                onClick = onTikTokClick,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

// NV-01: one logo of the row, the touch target is the whole cell and not only the 32dp image
@Suppress("ktlint:standard:function-naming")
@Composable
private fun SocialMediaButton(
    @DrawableRes logo: Int,
    name: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .height(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(
                onClickLabel = "abrir $name",
                role = Role.Button,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(id = logo),
            // The logo is the only content of the button, so it carries the name
            contentDescription = name,
            modifier = Modifier.size(32.dp),
        )
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
fun SocialMediaCardPreview() {
    SocialMediaCard(
        onFacebookClick = {},
        onInstagramClick = {},
        onXClick = {},
        onTikTokClick = {},
    )
}
