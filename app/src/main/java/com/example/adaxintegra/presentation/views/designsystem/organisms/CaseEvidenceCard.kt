package com.example.adaxintegra.presentation.views.designsystem.organisms

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text

// displays the evidence section associated with the selected case in V-11
// evidence content will be populated when its backend contract is integrated
@Suppress("ktlint:standard:function-naming")
@Composable
fun CaseEvidenceCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
        ) {
            Text(
                text = "Evidencia del caso",
                style = AppTextStyle.TitleMedium,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}
