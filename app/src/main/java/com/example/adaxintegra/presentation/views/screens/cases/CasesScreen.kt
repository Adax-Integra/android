package com.example.adaxintegra.presentation.views.screens.cases

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.adaxintegra.domain.entities.Case
import com.example.adaxintegra.domain.entities.CaseStatusFilter
import com.example.adaxintegra.presentation.viewmodel.CasesUiState
import com.example.adaxintegra.presentation.views.designsystem.molecules.CaseCard
import com.example.adaxintegra.presentation.views.designsystem.molecules.PageSwitcher
import com.example.adaxintegra.presentation.views.designsystem.organisms.CaseFilterDialog
import com.example.adaxintegra.ui.theme.AdaxIntegraTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox

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
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    title: String = "Casos",
    // V-12: when set, the dialog also filters by status and applies both at once
    onApplyFilters: ((CaseStatusFilter, String) -> Unit)? = null,
) {
    var showFilters by rememberSaveable { mutableStateOf(false) }

    val selectedUrgency = uiState.urgency.ifEmpty { "Todas" }
    val hasStatusFilter = onApplyFilters != null && uiState.status != CaseStatusFilter.ALL
    val hasActiveFilters = uiState.search.isNotEmpty() || selectedUrgency != "Todas" || hasStatusFilter

    Scaffold(modifier = modifier) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
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
                    text = title,
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
                    onClick = { showFilters = true },
                    modifier = Modifier.height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Text("Filtrar")
                }
            }

            if (hasActiveFilters) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = buildString {
                            if (hasStatusFilter) append("Estado: ${uiState.status.label} · ")
                            append("Urgencia: $selectedUrgency")
                        },
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
                    text = if (uiState.status == CaseStatusFilter.CLOSED) {
                        "ORDENADOS POR FECHA"
                    } else {
                        "ORDENADOS POR URGENCIA"
                    },
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

            PullToRefreshBox(
                isRefreshing = uiState.isRefreshing,
                onRefresh = onRefresh,
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
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState()),
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
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState()),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            Text(
                                text =
                                    if (hasActiveFilters) {
                                        "No hay casos que coincidan con tu busqueda y filtro."
                                    } else {
                                        "No hay casos disponibles"
                                    },
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
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

                PageSwitcher(
                    page = uiState.page,
                    canGoBack = canGoBack,
                    canGoNext = canGoNext,
                    onPreviousPage = onPreviousPage,
                    onNextPage = onNextPage,
                )
            }
        }
    }

    if (showFilters) {
        CaseFilterDialog(
            initialUrgency = uiState.urgency,
            // null hides the status section (V-03)
            initialStatus = if (onApplyFilters != null) uiState.status else null,
            onApply = { status, urgency ->
                if (onApplyFilters != null && status != null) {
                    onApplyFilters(status, urgency)
                } else {
                    onUrgencyChange(urgency)
                }
                showFilters = false
            },
            onDismiss = { showFilters = false },
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
    // Preview only cases that belong to the priority listing.
    val mockCases = Case.getMockData().filterNot {
        it.state.trim().lowercase() in listOf("closed", "cerrado")
    }

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
            onRefresh = {},
        )
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(
    name = "Mis casos con finalizados",
    showBackground = true,
    widthDp = 360,
    heightDp = 804,
)
@Composable
private fun MyCasesScreenPreview() {
    AdaxIntegraTheme(dynamicColor = false) {
        CasesScreen(
            uiState = CasesUiState(
                cases = Case.getMockData(),
                total = 4,
                status = CaseStatusFilter.ALL,
            ),
            onBackClick = {},
            onCaseClick = {},
            onSearchChange = {},
            onUrgencyChange = {},
            onClearFilters = {},
            onRetry = {},
            onNextPage = {},
            onPreviousPage = {},
            onRefresh = {},
            title = "Mis Casos",
            onApplyFilters = { _, _ -> },
        )
    }
}
