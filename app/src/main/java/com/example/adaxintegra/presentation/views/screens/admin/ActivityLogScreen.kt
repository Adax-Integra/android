package com.example.adaxintegra.presentation.views.screens.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.adaxintegra.presentation.views.designsystem.molecules.SearchBar
import com.example.adaxintegra.presentation.views.designsystem.organisms.ActivityLogFilterDialog
import com.example.adaxintegra.presentation.views.designsystem.organisms.ActivityLogTimelineItem
import com.example.adaxintegra.presentation.views.designsystem.templates.ScreenTemplate
import com.example.adaxintegra.ui.theme.IconGrey
import com.example.adaxintegra.ui.theme.Purple
import java.util.Date

// Text of the active filter
private fun filterText(from: Date?, to: Date?): String =
    "Filtro: ${DateFormatter.day(from)}, de ${DateFormatter.time(from)} a ${DateFormatter.time(to)}"

// Message when there is nothing to show
private fun emptyText(searchQuery: String, hasDateFilter: Boolean): String = when {
    searchQuery.isNotBlank() -> "No hay movimientos que coincidan con tu búsqueda."
    hasDateFilter -> "No hay movimientos en esa fecha y horario."
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

    ScreenTemplate(
        title = "Bitácora de cambios",
        subtitle = "Movimientos realizados en la aplicación",
        onBack = onBack,
        modifier = Modifier.padding(horizontal = spacing.medium),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = spacing.small),
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
                    placeholder = "Buscar por usuaria o acción...",
                    modifier = Modifier.weight(1f),
                )
                AppButton(
                    text = "Filtrar",
                    onClick = { showFilterDialog = true },
                    variant = ButtonVariant.Outlined,
                )
            }
            // Active date and time filter, with the option to remove it
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
            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator(color = Purple)
                    }
                }
                // The first page could not be loaded
                uiState.error != null && uiState.entries.isEmpty() -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(spacing.medium, Alignment.CenterVertically),
                    ) {
                        Text(text = uiState.error ?: "", style = AppTextStyle.BodyMedium)
                        AppButton(text = "Reintentar", onClick = viewModel::loadFirstPage)
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
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
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
                            // The next page is requested when the last entry appears
                            if (index == entries.lastIndex) {
                                LaunchedEffect(entries.size) {
                                    viewModel.loadNextPage()
                                }
                            }
                        }
                        if (uiState.isLoadingMore) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(spacing.medium),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    CircularProgressIndicator(
                                        color = Purple,
                                        modifier = Modifier.size(24.dp),
                                    )
                                }
                            }
                        }
                        // Error while loading more pages: the list stays visible
                        if (uiState.error != null) {
                            item {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(spacing.medium),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                ) {
                                    Text(
                                        text = uiState.error ?: "",
                                        style = AppTextStyle.LabelMedium,
                                        color = MaterialTheme.colorScheme.error,
                                    )
                                    TextButton(onClick = viewModel::loadNextPage) {
                                        Text(text = "Reintentar", color = Purple, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
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
