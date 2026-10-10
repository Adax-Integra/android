package com.example.adaxintegra.presentation.views.designsystem.organisms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text

@Suppress("ktlint:standard:function-naming")
@Composable
fun NotesBox(note: String) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .background(
                    color = androidx.compose.material3.MaterialTheme.colorScheme.secondaryContainer,
                    shape = RoundedCornerShape(8.dp),
                ).padding(8.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = "Notas: ",
                style = AppTextStyle.LabelSmall,
                fontWeight = FontWeight.SemiBold,
            )

            // actual notes left in by collaborator
            Text(
                text = note,
                style = AppTextStyle.BodySmall,
                fontWeight = FontWeight.Normal,
            )
        }
    }
}
