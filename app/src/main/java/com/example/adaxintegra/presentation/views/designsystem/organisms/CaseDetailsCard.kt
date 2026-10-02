package com.example.adaxintegra.presentation.views.designsystem.organisms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppIcons
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.presentation.views.designsystem.molecules.CaseDetailRow
import com.example.adaxintegra.presentation.views.designsystem.molecules.LawyerStatusRow

@Suppress("ktlint:standard:function-naming")
@Composable
fun CaseDetailCard(
    violenceType: String,
    location: String? = null,
    hasLawyer: Boolean,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = "Detalles del caso",
                style = AppTextStyle.TitleMedium,
                fontWeight = FontWeight.Bold,
            )

            CaseDetailRow(
                icon = AppIcons.Info,
                text = violenceType,
                iconColor = MaterialTheme.colorScheme.primary,
            )

            if (!location.isNullOrBlank()) {
                CaseDetailRow(
                    icon = AppIcons.Location,
                    text = location,
                    iconColor = MaterialTheme.colorScheme.primary,
                )
            }

            LawyerStatusRow(
                hasLawyer = hasLawyer,
            )
        }
    }
}
