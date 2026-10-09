package com.example.adaxintegra.presentation.views.screens.cases

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.example.adaxintegra.domain.entities.Case
import com.example.adaxintegra.presentation.viewmodel.CasesUiState
import com.example.adaxintegra.presentation.views.designsystem.molecules.CaseCard
import com.example.adaxintegra.ui.theme.AdaxIntegraTheme
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight


@Suppress("ktlint:standard:function-naming")
@Composable
fun CasesScreen(
    uiState: CasesUiState,
    onBackClick: () -> Unit,
    onCaseClick: (String) -> Unit,
    onSearchChange: (String) -> Unit,
    onUrgencyChange: (String) -> Unit,
    onClearFilters: () -> Unit,
    onRetry: () -> Unit,
    onNextPage: () -> Unit,
    onPreviousPage: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var pendingUrgency by rememberSaveable { mutableStateOf("Todas") }
    var showFilters by rememberSaveable { mutableStateOf(false) }

    val selectedUrgency = uiState.urgency.ifEmpty { "Todas" }

    Scaffold(modifier = modifier) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp,
                    bottom = 0.dp,
                ),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Regresar",
                    )
                }

                Text(
                    text = "Casos",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                OutlinedTextField(
                    value = uiState.search,
                    onValueChange = onSearchChange,
                    modifier =
                        Modifier
                            .weight(1f)
                            .height(56.dp),
                    placeholder = {
                        Text(
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
                        pendingUrgency = selectedUrgency
                        showFilters = true
                    },
                    modifier = Modifier.height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Text("Filtrar")
                }
            }

            if (uiState.search.isNotEmpty() || selectedUrgency != "Todas") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Urgencia: $selectedUrgency",
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.bodySmall,
                    )

                    TextButton(onClick = onClearFilters) {
                        Text("Limpiar")
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = "ORDENADOS POR URGENCIA",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = RoundedCornerShape(50),
                ) {
                    Text(
                        text = "${uiState.total} ${if (uiState.total == 1) "caso" else "casos"}",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        style = MaterialTheme.typography.labelSmall,
                    )
                }
            }

            Box(
                modifier =
                    Modifier
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
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Text(
                                text = uiState.error ?: "No se pudieron cargar los casos.",
                                style = MaterialTheme.typography.bodyMedium,
                            )

                            OutlinedButton(onClick = onRetry) {
                                Text("Reintentar")
                            }
                        }
                    }

                    uiState.cases.isEmpty() -> {
                        Text(
                            text =
                                if (
                                    uiState.search.isNotBlank() ||
                                    selectedUrgency != "Todas"
                                ) {
                                    "No hay casos que coincidan con tu busqueda y filtro."
                                } else {
                                    "No hay casos disponibles"
                                },
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }

                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(14.dp),
                        ) {
                            items(
                                items = uiState.cases,
                                key = { it.caseId },
                            ) { case ->
                                CaseCard(
                                    case = case,
                                    modifier =
                                        Modifier.clickable {
                                            onCaseClick(case.caseId)
                                        },
                                )
                            }
                        }
                    }
                }
            }

            if (
                !uiState.isLoading &&
                uiState.error == null &&
                uiState.total > uiState.limit
            ) {
                val canGoBack = uiState.page > 1
                val canGoNext =
                    uiState.page.toLong() * uiState.limit < uiState.total

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = 0.dp,
                        bottomEnd = 0.dp,
                    ),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        IconButton(
                            onClick = onPreviousPage,
                            enabled = canGoBack,
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                contentDescription = "Página anterior",
                                modifier = Modifier.size(28.dp),
                                tint = if (canGoBack) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                                },
                            )
                        }

                        Text(
                            text = "Página ${uiState.page}",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface,
                        )

                        IconButton(
                            onClick = onNextPage,
                            enabled = canGoNext,
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = "Página siguiente",
                                modifier = Modifier.size(28.dp),
                                tint = if (canGoNext) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                                },
                            )
                        }
                    }
                }
            }
        }
    }

    if (showFilters) {
        AlertDialog(
            onDismissRequest = { showFilters = false },
            title = { Text("Filtrar por urgencia") },
            text = {
                Column(
                    modifier = Modifier.selectableGroup().verticalScroll(rememberScrollState()),
                ) {
                    listOf("Todas", "Alta", "Media", "Baja", "Sin evaluar").forEach { urgency ->
                        Row(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .selectable(
                                        selected = pendingUrgency == urgency,
                                        onClick = { pendingUrgency = urgency },
                                        role = Role.RadioButton,
                                    )
                                    .padding(vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            RadioButton(
                                selected = pendingUrgency == urgency,
                                onClick = null,
                            )

                            Text(urgency)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onUrgencyChange(
                            if (pendingUrgency == "Todas") "" else pendingUrgency,
                        )
                        showFilters = false
                    },
                ) {
                    Text("Aplicar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showFilters = false }) {
                    Text("Cancelar")
                }
            },
        )
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(
    name = "Casos con paginación",
    showBackground = true,
    widthDp = 360,
    heightDp = 804,
)
@Composable
private fun CasesScreenPreview() {
    val mockCases = Case.getMockData()

    AdaxIntegraTheme(dynamicColor = false) {
        CasesScreen(
            uiState = CasesUiState(
                cases = mockCases,
                total = 45,
                page = 2,
                limit = 20,
            ),
            onBackClick = {},
            onCaseClick = {},
            onSearchChange = {},
            onUrgencyChange = {},
            onClearFilters = {},
            onRetry = {},
            onNextPage = {},
            onPreviousPage = {},
        )
    }
}
