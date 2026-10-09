package com.example.adaxintegra.presentation.views.designsystem.molecules

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppIcons
import com.example.adaxintegra.presentation.views.designsystem.atoms.PillBadge

@Suppress("ktlint:standard:function-naming")
@Composable
fun LawyerStatusRow(
    hasLawyer: Boolean,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CaseDetailRow(
            icon = AppIcons.Profile,
            text = "¿Cuenta con abogado?",
            iconColor = MaterialTheme.colorScheme.primary,
        )

        PillBadge(
            text = if (hasLawyer) "Sí" else "No",
        )
    }
}
