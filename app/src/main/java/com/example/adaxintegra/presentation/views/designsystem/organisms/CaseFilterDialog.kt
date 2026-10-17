package com.example.adaxintegra.presentation.views.designsystem.organisms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.adaxintegra.domain.entities.CaseStatusFilter
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppButton
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.ButtonVariant
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.presentation.views.designsystem.molecules.FilterRadioOption
import com.example.adaxintegra.ui.theme.AdaxIntegraTheme

private const val ALL_URGENCIES = "Todas"
private val URGENCY_OPTIONS = listOf(ALL_URGENCIES, "Alta", "Media", "Baja", "Sin evaluar")

// V-12: "Filtrar" dialog for case lists. The status section only appears
// when initialStatus is given, so the V-03 list can keep urgency only
@Suppress("ktlint:standard:function-naming")
@Composable
fun CaseFilterDialog(
    initialUrgency: String,
    onApply: (status: CaseStatusFilter?, urgency: String) -> Unit,
    onDismiss: () -> Unit,
    initialStatus: CaseStatusFilter? = null,
) {
    // "" means no urgency filter, shown as "Todas"
    var selectedUrgency by remember { mutableStateOf(initialUrgency.ifEmpty { ALL_URGENCIES }) }
    var selectedStatus by remember { mutableStateOf(initialStatus) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
            ),
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    text = "Filtrar casos",
                    style = AppTextStyle.TitleMedium,
                    fontWeight = FontWeight.Bold,
                )

                HorizontalDivider()

                // Status section (Todos, En curso, Finalizados)
                selectedStatus?.let { current ->
                    Text(
                        text = "Estado",
                        style = AppTextStyle.LabelMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    Column(modifier = Modifier.selectableGroup()) {
                        for (option in CaseStatusFilter.entries) {
                            FilterRadioOption(
                                text = option.label,
                                selected = current == option,
                                onClick = { selectedStatus = option },
                            )
                        }
                    }

                    HorizontalDivider()
                }

                Text(
                    text = "Urgencia",
                    style = AppTextStyle.LabelMedium,
                    fontWeight = FontWeight.Bold,
                )
                Column(modifier = Modifier.selectableGroup()) {
                    for (urgency in URGENCY_OPTIONS) {
                        FilterRadioOption(
                            text = urgency,
                            selected = selectedUrgency == urgency,
                            onClick = { selectedUrgency = urgency },
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    AppButton(
                        text = "Aplicar",
                        onClick = {
                            onApply(
                                selectedStatus,
                                if (selectedUrgency == ALL_URGENCIES) "" else selectedUrgency,
                            )
                        },
                        modifier = Modifier.weight(1f),
                    )
                    AppButton(
                        text = "Cancelar",
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        variant = ButtonVariant.Outlined,
                    )
                }
            }
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
private fun CaseFilterDialogPreview() {
    AdaxIntegraTheme(dynamicColor = false) {
        CaseFilterDialog(
            initialUrgency = "",
            initialStatus = CaseStatusFilter.OPEN,
            onApply = { _, _ -> },
            onDismiss = {},
        )
    }
}
