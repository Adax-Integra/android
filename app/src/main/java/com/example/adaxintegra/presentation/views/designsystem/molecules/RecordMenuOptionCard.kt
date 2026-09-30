package com.example.adaxintegra.presentation.views.designsystem.molecules

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppIcons

@Suppress("ktlint:standard:function-naming")
@Composable
fun RecordMenuOptionCard(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    nextScreen: () -> Unit,
    icon: ImageVector,
) {
    Card(
        onClick = nextScreen,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                )

                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
            )
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
fun RecordMenuOptionCardPreviewOpt1() {
    RecordMenuOptionCard(
        title = "Todos los Casos",
        description = "Consulta los casos por urgencia",
        nextScreen = {},
        icon = AppIcons.Folder,
    )
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
fun RecordMenuOptionCardPreviewOpt2() {
    RecordMenuOptionCard(
        title = "Todos los Expedientes",
        description = "Consulta una lista con todos los expedientes",
        nextScreen = {},
        icon = AppIcons.UserAttributes,
    )
}
