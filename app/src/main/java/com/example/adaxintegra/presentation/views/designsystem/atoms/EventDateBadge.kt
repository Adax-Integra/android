package com.example.adaxintegra.presentation.views.designsystem.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.ui.theme.Purple
import com.example.adaxintegra.ui.theme.White

// E-01: day and month of the event shown over its image
@Suppress("ktlint:standard:function-naming")
@Composable
fun EventDateBadge(
    day: String,
    month: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(White)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = day,
            style = AppTextStyle.TitleMedium,
            fontWeight = FontWeight.Bold,
            color = Purple,
        )
        Text(
            text = month,
            style = AppTextStyle.LabelSmall,
            fontWeight = FontWeight.SemiBold,
            color = Purple,
        )
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview
@Composable
fun EventDateBadgePreview() {
    EventDateBadge(day = "25", month = "OCT")
}
