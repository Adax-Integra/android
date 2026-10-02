package com.example.adaxintegra.presentation.views.designsystem.organisms

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppIcon
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppIcons
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.IconSize
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text

@Suppress("ktlint:standard:function-naming")
@Composable
fun CaseRecordSummaryCard(
    userName: String,
    recordId: String,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AppIcon(
                imageVector = AppIcons.Account,
                contentDescription = null,
                size = IconSize.LargeIcon,
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = userName,
                    style = AppTextStyle.TitleMedium,
                    fontWeight = FontWeight.Bold,
                )

                Text(
                    text = "Expediente: $recordId",
                    style = AppTextStyle.LabelSmall,
                    fontWeight = FontWeight.Normal,
                )
            }
        }
    }
}
