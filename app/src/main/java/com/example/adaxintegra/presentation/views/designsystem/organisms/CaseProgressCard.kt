package com.example.adaxintegra.presentation.views.designsystem.organisms

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.PillBadge
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text

//progress cards shown when clicking on case details
@Composable
fun CaseProgressCard(
    stepNumber: Int,
    title: String,
    status: String,
    description: String,
    note: String? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        //title and status
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "$stepNumber.",
                    style = AppTextStyle.BodyMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = title,
                    style = AppTextStyle.BodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            PillBadge(
                text = status
            )
        }

        //description
        Text(
            text = description,
            style = AppTextStyle.BodySmall,
            fontWeight = FontWeight.Normal
        )

        //notes IF and ONLY IF left by collaborator
        if (note != null) {
            NotesBox(
                note = note
            )
        }
    }
}