package com.example.adaxintegra.presentation.views.screens.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppIcons
import com.example.adaxintegra.presentation.views.designsystem.organisms.MenuOptionCard
import com.example.adaxintegra.ui.theme.AdaxIntegraTheme

@Suppress("ktlint:standard:function-naming")
@Composable
fun AdminScreen(
    onManageUsersClick: () -> Unit,
    onManageExpedientsClick: () -> Unit,
    modifier: Modifier = Modifier,
    onAuditLogClick: () -> Unit = {},
    onManageExternalUsersClick: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Text(
            text = "Panel de Administración",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
        )

        Text(
            text = "Consulta y gestiona las actividades dentro de ADAX Integra.",
            style = MaterialTheme.typography.bodyMedium,
        )

        MenuOptionCard(
            title = "Gestion de colaboradoras",
            subtitle = "Administra y crea las cuentas de los colaboradores de la aplicación.",
            icon = AppIcons.People,
            onClick = onManageUsersClick,
        )

        // G-16: list of every external account registered in the system
        MenuOptionCard(
            title = "Gestión de externas",
            subtitle = "Consulta las cuentas de las usuarias externas registradas.",
            icon = AppIcons.Profile,
            onClick = onManageExternalUsersClick,
        )

        MenuOptionCard(
            title = "Bitácora de cambios",
            subtitle = "Consulta el historial de actividades e incidencias en el sistema.",
            icon = AppIcons.History,
            onClick = onAuditLogClick,
        )
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
private fun AdminScreenPreview() {
    AdaxIntegraTheme(dynamicColor = false) {
        AdminScreen(onManageUsersClick = {}, onManageExpedientsClick = {})
    }
}
