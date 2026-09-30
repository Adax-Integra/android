package com.example.adaxintegra.presentation.views.designsystem.organisms

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppIcon
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppIcons
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text

@Composable
fun ProfileOption(
    icon: AppIcons,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = AppIcons,
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = title)
                Text(text = subtitle)
            }

            Icon(
                imageVector = AppIcons.ChevronRight,
                contentDescription = "Abrir"
            )
        }
    }
}
