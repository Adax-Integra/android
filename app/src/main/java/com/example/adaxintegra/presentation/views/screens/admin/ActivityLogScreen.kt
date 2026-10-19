package com.example.adaxintegra.presentation.views.screens.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.adaxintegra.presentation.util.DateFormatter
import com.example.adaxintegra.presentation.viewmodel.ActivityLogViewModel
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppButton
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.ButtonVariant
import com.example.adaxintegra.presentation.views.designsystem.atoms.Spacing
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.presentation.views.designsystem.molecules.PageSwitcher
import com.example.adaxintegra.presentation.views.designsystem.molecules.SearchBar
import com.example.adaxintegra.presentation.views.designsystem.organisms.ActivityLogFilterDialog
import com.example.adaxintegra.presentation.views.designsystem.organisms.ActivityLogTimelineItem
import com.example.adaxintegra.presentation.views.designsystem.templates.ScreenTemplate
import com.example.adaxintegra.ui.theme.IconGrey
import com.example.adaxintegra.ui.theme.Purple
import java.util.Date

// Text of the active filter, ex. "Filtro: del 01/10/2026 al 08/10/2026"
private fun filterText(from: Date?, to: Date?): String {
    val fromText = DateFormatter.day(from)
    val toText = DateFormatter.day(to)
    return if (fromText == toText) {
        "Filtro: $fromText"
    } else {
        "Filtro: del $fromText al $toText"
    }
}

// Message when there is nothing to show
private fun emptyText(searchQuery: String, hasDateFilter: Boolean): String = when {
    searchQuery.isNotBlank() -> "No hay movimientos que coincidan con tu búsqueda."
    hasDateFilter -> "No hay movimientos en ese rango de fechas."
    else -> "No hay movimientos registrados."
}

// V-06: the admin consults the activity log
// opened from the admin section
@Suppress("ktlint:standard:function-naming")
@Composable
fun ActivityLogScreen(
    onBack: () -> Unit,
    viewModel: ActivityLogViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showFilterDialog by rememberSaveable { mutableStateOf(false) }
    val spacing = Spacing()
    val entries = uiState.visibleEntries

    // V-06: the page card is only shown when there is more than one page
    val showPagination = !uiState.isLoading && uiState.lastPage > 1

    // Same template and padding as the other lists
    ScreenTemplate(
        title = "Bitácora de cambios",
        subtitle = "Movimientos realizados en la aplicación",
        onBack = onBack,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(spacing.medium),
            verticalArrangement = Arrangement.spacedBy(spacing.small),
        ) {
            // Search box and Filtrar button.
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(spacing.small),
            ) {
                SearchBar(
                    query = uiState.searchQuery,
                    onQueryChange = viewModel::onSearchQueryChange,
                    placeholder = "Buscar por usuaria",
                    modifier = Modifier.weight(1f),
                )
                AppButton(
                    text = "Filtrar",
                    onClick = { showFilterDialog = true },
                    variant = ButtonVariant.Outlined,
                )
            }

            // Active date filter, with the option to remove it
            if (uiState.hasDateFilter) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = filterText(uiState.fromDate, uiState.toDate),
                        style = AppTextStyle.LabelMedium,
                        fontWeight = FontWeight.Medium,
                        color = Purple,
                        modifier = Modifier.weight(1f),
                    )
                    TextButton(onClick = viewModel::clearDateFilter) {
                        Text(text = "Quitar", color = Purple, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // The list takes the free space and the page card floats over its bottom
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            ) {
                when {
                    uiState.isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularProgressIndicator(color = Purple)
                        }
                    }

                    // The page could not be loaded
                    uiState.error != null -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(spacing.medium, Alignment.CenterVertically),
                        ) {
                            Text(text = uiState.error ?: "", style = AppTextStyle.BodyMedium)
                            AppButton(text = "Reintentar", onClick = viewModel::retry)
                        }
                    }

                    entries.isEmpty() -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = emptyText(uiState.searchQuery, uiState.hasDateFilter),
                                style = AppTextStyle.BodyMedium,
                                color = IconGrey,
                            )
                        }
                    }

                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            // Space so the last entry is not hidden behind the page card
                            contentPadding = PaddingValues(
                                bottom = if (showPagination) 76.dp else spacing.medium,
                            ),
                        ) {
                            itemsIndexed(entries) { index, entry ->
                                // Date header when the day changes ("Hoy", "Ayer" or the date)
                                val previous = if (index > 0) entries[index - 1] else null
                                if (previous == null || !DateFormatter.isSameDay(previous.createdAt, entry.createdAt)) {
                                    Text(
                                        text = DateFormatter.dayLabel(entry.createdAt),
                                        style = AppTextStyle.LabelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = IconGrey,
                                        modifier = Modifier.padding(top = spacing.small, bottom = spacing.extraSmall),
                                    )
                                }
                                ActivityLogTimelineItem(entry = entry)
                            }
                        }
                    }
                }

                // V-06: pages of 10 entries, same floating card as the other lists
                if (showPagination) {
                    PageSwitcher(
                        page = uiState.page,
                        totalPages = uiState.lastPage,
                        canGoBack = uiState.canGoBack,
                        canGoNext = uiState.canGoNext,
                        onPreviousPage = viewModel::previousPage,
                        onNextPage = viewModel::nextPage,
                        modifier = Modifier.align(Alignment.BottomCenter),
                    )
                }
            }
        }
    }

    if (showFilterDialog) {
        ActivityLogFilterDialog(
            initialFrom = uiState.fromDate,
            initialTo = uiState.toDate,
            onApply = { from, to ->
                showFilterDialog = false
                viewModel.applyDateFilter(from, to)
            },
            onClear = {
                showFilterDialog = false
                viewModel.clearDateFilter()
            },
            onDismiss = { showFilterDialog = false },
        )
    }
}
