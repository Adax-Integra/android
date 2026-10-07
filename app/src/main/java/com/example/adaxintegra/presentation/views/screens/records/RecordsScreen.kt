package com.example.adaxintegra.presentation.views.screens.records

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.adaxintegra.domain.entities.Record
import com.example.adaxintegra.presentation.model.RecordStatusUi
import com.example.adaxintegra.presentation.viewmodel.RecordsUiState
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppButton
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.ButtonVariant
import com.example.adaxintegra.presentation.views.designsystem.atoms.PillBadge
import com.example.adaxintegra.presentation.views.designsystem.atoms.Spacing
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.presentation.views.designsystem.molecules.RecordCard
import com.example.adaxintegra.presentation.views.designsystem.templates.ScreenTemplate
import com.example.adaxintegra.ui.theme.AdaxIntegraTheme

@Suppress("ktlint:standard:function-naming")
@Composable
fun RecordsScreen(
    uiState: RecordsUiState,
    onBackClick: () -> Unit,
    onSearchChange: (String) -> Unit,
    onApplyFilters: (Boolean?, String?) -> Unit,
    onClearFilters: () -> Unit,
    onRetry: () -> Unit,
    onNextPage: () -> Unit,
    onPreviousPage: () -> Unit,
    onRecordClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val spacing = Spacing()

    var showFilters by rememberSaveable { mutableStateOf(false) }

    // Pending selections are applied only when the user confirms.
    var pendingOpenCases by rememberSaveable {
        mutableStateOf<Boolean?>(null)
    }
    var pendingStatus by rememberSaveable {
        mutableStateOf<String?>(null)
    }

    val hasCriteria = uiState.search.isNotBlank() ||
        uiState.hasOpenCases != null ||
        uiState.status != null

    val openCasesLabel = when (uiState.hasOpenCases) {
        true -> "Con casos abiertos"
        false -> "Sin casos abiertos"
        null -> "Todos los expedientes"
    }

    val statusLabel = if (uiState.status == null) {
        "Todos los estados"
    } else {
        RecordStatusUi.from(uiState.status)?.displayText ?: "Estado desconocido"
    }

    // Keep search and filters visible while the listing loads or fails.
    ScreenTemplate(
        title = "Expedientes",
        onBack = onBackClick,
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(spacing.medium),
            verticalArrangement = Arrangement.spacedBy(spacing.medium),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(spacing.small),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                OutlinedTextField(
                    value = uiState.search,
                    onValueChange = onSearchChange,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    placeholder = {
                        androidx.compose.material3.Text(
                            text = "Buscar...",
                            fontSize = 14.sp,
                            maxLines = 1,
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                )

                OutlinedButton(
                    onClick = {
                        pendingOpenCases = uiState.hasOpenCases
                        pendingStatus = uiState.status
                        showFilters = true
                    },
                    modifier = Modifier.height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Text(text = "Filtrar")
                }
            }

            if (hasCriteria) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "$openCasesLabel · $statusLabel",
                        modifier = Modifier.weight(1f),
                        style = AppTextStyle.BodySmall,
                    )

                    TextButton(onClick = onClearFilters) {
                        Text(text = "Limpiar")
                    }
                }
            }

            // Avoid showing an outdated count during loading or errors.
            if (!uiState.isLoading && uiState.error == null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(spacing.small),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Más recientes primero",
                        modifier = Modifier.weight(1f),
                        style = AppTextStyle.LabelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )

                    PillBadge(
                        text = "${uiState.total} ${
                            if (uiState.total == 1) "expediente" else "expedientes"
                        }",
                    )
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                when {
                    uiState.isLoading -> {
                        CircularProgressIndicator()
                    }

                    uiState.error != null -> {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(spacing.small),
                        ) {
                            Text(
                                text = uiState.error
                                    ?: "No se pudieron cargar los expedientes.",
                            )

                            AppButton(
                                text = "Reintentar",
                                onClick = onRetry,
                                variant = ButtonVariant.Outlined,
                            )
                        }
                    }

                    uiState.records.isEmpty() -> {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(spacing.small),
                        ) {
                            Text(
                                text = when {
                                    uiState.page > 1 ->
                                        "No hay expedientes en esta página."

                                    hasCriteria ->
                                        "No encontramos expedientes con esta búsqueda y filtros."

                                    else ->
                                        "No hay expedientes disponibles"
                                },
                            )

                            if (hasCriteria) {
                                AppButton(
                                    text = "Limpiar filtros",
                                    onClick = onClearFilters,
                                    variant = ButtonVariant.Outlined,
                                )
                            }
                        }
                    }

                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(spacing.medium),
                        ) {
                            items(
                                items = uiState.records,
                                key = { it.recordId },
                            ) { record ->
                                RecordCard(
                                    record = record,
                                    // V-10 receives the selected owner's ID.
                                    onClick = { onRecordClick(record.userId) },
                                )
                            }
                        }
                    }
                }
            }

            // Allow returning from a later page even if its request fails.
            val showPagination = !uiState.isLoading &&
                (uiState.page > 1 || uiState.total > uiState.limit)

            if (showPagination) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    TextButton(
                        onClick = onPreviousPage,
                        enabled = uiState.page > 1,
                    ) {
                        Text(text = "Anterior")
                    }

                    Text(
                        text = "Página ${uiState.page}",
                        style = AppTextStyle.BodySmall,
                    )

                    TextButton(
                        onClick = onNextPage,
                        enabled = uiState.error == null &&
                            uiState.page.toLong() * uiState.limit < uiState.total,
                    ) {
                        Text(text = "Siguiente")
                    }
                }
            }
        }
    }
    if (showFilters) {
        AlertDialog(
            onDismissRequest = { showFilters = false },
            title = { Text(text = "Filtrar expedientes") },
            text = {
                Column(
                    modifier = Modifier
                        .heightIn(max = 400.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(spacing.medium),
                ) {
                    Text(
                        text = "Casos abiertos",
                        fontWeight = FontWeight.Bold,
                    )

                    Column(modifier = Modifier.selectableGroup()) {
                        RecordFilterOption(
                            text = "Todos",
                            selected = pendingOpenCases == null,
                            onClick = { pendingOpenCases = null },
                        )
                        RecordFilterOption(
                            text = "Con casos abiertos",
                            selected = pendingOpenCases == true,
                            onClick = { pendingOpenCases = true },
                        )
                        RecordFilterOption(
                            text = "Sin casos abiertos",
                            selected = pendingOpenCases == false,
                            onClick = { pendingOpenCases = false },
                        )
                    }

                    Text(
                        text = "Estado del expediente",
                        fontWeight = FontWeight.Bold,
                    )

                    Column(modifier = Modifier.selectableGroup()) {
                        RecordFilterOption(
                            text = "Todos",
                            selected = pendingStatus == null,
                            onClick = { pendingStatus = null },
                        )

                        RecordStatusUi.entries.forEach { status ->
                            RecordFilterOption(
                                text = status.displayText,
                                selected = pendingStatus == status.value,
                                onClick = { pendingStatus = status.value },
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onApplyFilters(pendingOpenCases, pendingStatus)
                        showFilters = false
                    },
                ) {
                    Text(text = "Aplicar")
                }
            },
            dismissButton = {
                // Closing the dialog does not change the applied filters.
                TextButton(onClick = { showFilters = false }) {
                    Text(text = "Cancelar")
                }
            },
        )
    }
}

// The entire row is selectable, including its label.
@Suppress("ktlint:standard:function-naming")
@Composable
private fun RecordFilterOption(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val spacing = Spacing()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp)
            .selectable(
                selected = selected,
                onClick = onClick,
                role = Role.RadioButton,
            )
            .padding(vertical = spacing.extraSmall),
        horizontalArrangement = Arrangement.spacedBy(spacing.small),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = selected,
            onClick = null,
        )
        Text(text = text)
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true, widthDp = 360, heightDp = 804)
@Composable
private fun RecordsScreenPreview() {
    // Fictional records allow checking the layout without an API request.
    val records = listOf(
        Record(
            recordId = "preview-record-1",
            userId = "preview-user-1",
            name = "Ana Pérez",
            recordNumber = "EXP-2026-0001",
            status = "EN_REVISION",
            activeCasesCount = 3,
            updatedAt = "2026-10-06T10:30:00",
        ),
        Record(
            recordId = "preview-record-2",
            userId = "preview-user-2",
            name = "María López",
            recordNumber = "EXP-2026-0002",
            status = "SIN_EMPEZAR",
            activeCasesCount = 0,
            updatedAt = "2026-10-05T09:00:00",
        ),
    )

    AdaxIntegraTheme(dynamicColor = false) {
        RecordsScreen(
            uiState = RecordsUiState(
                records = records,
                total = records.size,
            ),
            onBackClick = {},
            onSearchChange = {},
            onApplyFilters = { _, _ -> },
            onClearFilters = {},
            onRetry = {},
            onNextPage = {},
            onPreviousPage = {},
            onRecordClick = {},
        )
    }
}
