package com.example.adaxintegra.presentation.views.designsystem.molecules

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.domain.model.ExternalProfileChange
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.ui.theme.IconGrey
import com.example.adaxintegra.ui.theme.Purple
import androidx.compose.material3.Text as MaterialText

// G-07: the old value crossed out and the new one, shown before saving
@Suppress("ktlint:standard:function-naming")
@Composable
fun ChangeRow(
    change: ExternalProfileChange,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Purple.copy(alpha = 0.06f), RoundedCornerShape(12.dp))
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(
            text = change.label.uppercase(),
            style = AppTextStyle.LabelSmall,
            fontWeight = FontWeight.SemiBold,
            color = IconGrey,
        )

        // Long values (like an email) go to the next line instead of being cut
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            itemVerticalAlignment = Alignment.CenterVertically,
        ) {
            // The Text atom has no decoration, so the crossed out value uses the Material one
            MaterialText(
                text = change.oldValue,
                style = MaterialTheme.typography.bodyMedium.copy(
                    textDecoration = TextDecoration.LineThrough,
                ),
                color = IconGrey,
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "cambia a",
                tint = Purple,
                modifier = Modifier.size(16.dp),
            )
            Text(
                text = change.newValue,
                style = AppTextStyle.BodyMedium,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
private fun ChangeRowPreview() {
    ChangeRow(
        change = ExternalProfileChange(
            label = "Teléfono celular",
            oldValue = "+52 442 123 4567",
            newValue = "+52 442 987 6543",
        ),
    )
}
