package com.example.adaxintegra.presentation.views.designsystem.organisms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.domain.model.ExternalProfileChange
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppButton
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppIcon
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppIcons
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.ButtonVariant
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.presentation.views.designsystem.molecules.ChangeRow
import com.example.adaxintegra.ui.theme.IconGrey

// G-07: shown before saving, only the fields that changed
// and the notice the external user will receive
@OptIn(ExperimentalMaterial3Api::class)
@Suppress("ktlint:standard:function-naming")
@Composable
fun ChangesSummarySheet(
    externalName: String,
    changes: List<ExternalProfileChange>,
    reason: String,
    noticeText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Confirmar cambios",
                    style = AppTextStyle.TitleLarge,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "Revisa los datos de $externalName antes de guardar.",
                    style = AppTextStyle.BodyMedium,
                    color = IconGrey,
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                changes.forEach { change -> ChangeRow(change = change) }
            }

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "MOTIVO",
                    style = AppTextStyle.LabelSmall,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(text = reason, style = AppTextStyle.BodyLarge)
            }

            NoticeLine(icon = AppIcons.CheckMark, text = "La externa autorizó este cambio")
            NoticeLine(icon = AppIcons.Email, text = noticeText)

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                AppButton(
                    text = "Confirmar y guardar",
                    onClick = onConfirm,
                    modifier = Modifier.fillMaxWidth(),
                )
                AppButton(
                    text = "Regresar a editar",
                    onClick = onDismiss,
                    variant = ButtonVariant.Outlined,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
private fun NoticeLine(
    icon: ImageVector,
    text: String,
) {
    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        AppIcon(imageVector = icon, tint = IconGrey)
        Text(
            text = text,
            style = AppTextStyle.BodySmall,
            color = IconGrey,
        )
    }
}
