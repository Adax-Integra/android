package com.example.adaxintegra.presentation.views.screens.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppIcons
import com.example.adaxintegra.presentation.views.designsystem.molecules.RecordMenuOptionCard
import com.example.adaxintegra.presentation.views.designsystem.templates.ScreenTemplate

@Suppress("ktlint:standard:function-naming")
@Composable
fun AdminScreen(
    onManageUsersClick: () -> Unit,
    onManageExpedientsClick: () -> Unit,
    onAuditLogClick: () -> Unit = {},
) {
    ScreenTemplate(
        title = "  Panel de Administración",
        showBackButton = false,
        modifier = Modifier
            .statusBarsPadding(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Column(modifier = Modifier.padding(bottom = 4.dp)) {
                Text(
                    text = "¿Qué deseas gestionar?",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Consulta y gestiona las actividades dentro de ADAX Integra.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            RecordMenuOptionCard(
                title = "Gestion de colaboradoras",
                description = "Administra y crea las cuentas de los colaboradores de la aplicación.",
                icon = AppIcons.People,
                nextScreen = onManageUsersClick,
            )

            RecordMenuOptionCard(
                title = "Gestionar expedientes",
                description = "Administra y crea las cuentas de las usuarias externas.",
                icon = AppIcons.Profile,
                nextScreen = onManageExpedientsClick,
            )

            RecordMenuOptionCard(
                title = "Bitácora de cambios",
                description = "Consulta el historial de actividades e incidencias en el sistema.",
                icon = AppIcons.History,
                nextScreen = onAuditLogClick,
            )
        }
    }
}
