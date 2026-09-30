package com.example.adaxintegra.presentation.views.screens.records

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppIcons
import com.example.adaxintegra.presentation.views.designsystem.molecules.RecordMenuOptionCard
import com.example.adaxintegra.ui.theme.AdaxIntegraTheme

@Suppress("ktlint:standard:function-naming")
@Composable
fun RecordsMenuScreen(
    onAllCasesClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Text(
            text = "Expedientes",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
        )

        Text(
            text = "Consulta toda tu información aquí",
            style = MaterialTheme.typography.bodyMedium,
        )

        RecordMenuOptionCard(
            title = "Todos los Casos",
            description = "Consulta los casos por urgencia",
            nextScreen = onAllCasesClick,
            icon = AppIcons.Folder,
        )
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
private fun RecordsMenuScreenPreview() {
    AdaxIntegraTheme(dynamicColor = false) {
        RecordsMenuScreen(onAllCasesClick = {})
    }
}
