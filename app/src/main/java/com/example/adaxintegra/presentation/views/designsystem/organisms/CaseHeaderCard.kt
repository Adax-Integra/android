package com.example.adaxintegra.presentation.views.designsystem.organisms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.CaseStatusBadge
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text

@Suppress("ktlint:standard:function-naming")
@Composable
fun CaseHeaderCard(
    caseNumber: String,
    statusText: String,
    statusColor: Color,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = caseNumber,
                style = AppTextStyle.TitleMedium,
                fontWeight = FontWeight.Bold,
            )

            CaseStatusBadge(
                text = statusText,
                color = statusColor,
            )
        }
    }
}
