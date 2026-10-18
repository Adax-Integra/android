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
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.adaxintegra.presentation.viewmodel.ExternalUsersViewModel
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppButton
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.Spacing
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.presentation.views.designsystem.molecules.CollaboratorCard
import com.example.adaxintegra.presentation.views.designsystem.molecules.PageSwitcher
import com.example.adaxintegra.presentation.views.designsystem.molecules.SearchBar
import com.example.adaxintegra.presentation.views.designsystem.organisms.CollaboratorFilterDialog
import com.example.adaxintegra.presentation.views.designsystem.templates.ScreenTemplate
import com.example.adaxintegra.ui.theme.IconGrey
import com.example.adaxintegra.ui.theme.Purple

private fun countText(total: Int): String =
    if (total == 1) "1 Externa" else "$total Externas"

// Message when there is nothing to show
private fun emptyText(searchQuery: String, hasStatusFilter: Boolean): String = when {
    searchQuery.isNotBlank() -> "No hay externas que coincidan con tu búsqueda."
    hasStatusFilter -> "No hay externas con ese estatus."
    else -> "No hay externas registradas."
}

// G-16: admin's list of external accounts, opened from the admin section.
// Same look as the collaborators list, without "Agregar".
// Tapping an account will open her data in G-07
@Suppress("ktlint:standard:function-naming")
@Composable
fun ExternalUsersScreen(
    onBack: () -> Unit = {},
    onExternalUserClick: (String) -> Unit = {},
    viewModel: ExternalUsersViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val spacing = Spacing()
    var showFilterDialog by rememberSaveable { mutableStateOf(false) }

    val showPagination = (!uiState.isLoading) &&
        (uiState.page > 1 || uiState.total > uiState.limit)

    // First load
    LaunchedEffect(Unit) {
        viewModel.loadExternalUsers()
    }

    ScreenTemplate(
        title = "Gestión de Externas",
        onBack = onBack,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(spacing.medium),
            verticalArrangement = Arrangement.spacedBy(spacing.small),
        ) {
            SearchBar(
                query = uiState.searchQuery,
                onQueryChange = viewModel::onSearchQueryChange,
                placeholder = "Buscar por nombre o correo...",
            )

            // Active status filter, with the option to remove it
            if (uiState.hasStatusFilter) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Filtro: ${uiState.statusFilter.label}",
                        style = AppTextStyle.LabelMedium,
                        fontWeight = FontWeight.Medium,
                        color = Purple,
                        modifier = Modifier.weight(1f),
                    )
                    TextButton(onClick = viewModel::clearStatusFilter) {
                        Text(text = "Quitar", color = Purple, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Number of accounts found by the backend, not only the ones on this page
            Text(
                text = countText(uiState.total),
                style = AppTextStyle.LabelMedium,
            )

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

                    // The list could not be loaded
                    uiState.error != null -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(
                                spacing.medium,
                                Alignment.CenterVertically
                            ),
                        ) {
                            Text(
                                text = uiState.error ?: "",
                                style = AppTextStyle.BodyMedium,
                                textAlign = TextAlign.Center,
                            )
                            AppButton(text = "Reintentar", onClick = viewModel::loadExternalUsers)
                        }
                    }

                    uiState.users.isEmpty() -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = emptyText(uiState.searchQuery, uiState.hasStatusFilter),
                                style = AppTextStyle.BodyMedium,
                                color = IconGrey,
                            )
                        }
                    }

                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(spacing.small),
                            contentPadding = PaddingValues(
                                bottom = if (showPagination) 76.dp else spacing.medium,
                            ),
                        ) {
                            // Same card as the collaborators list (initials, name, email and status)
                            items(uiState.users) { user ->
                                CollaboratorCard(
                                    name = user.name,
                                    lastName = user.lastName,
                                    email = user.email,
                                    isActive = user.isActive,
                                    onClick = { onExternalUserClick(user.userId) },
                                )
                            }
                        }
                    }
                }

                if (showPagination) {
                    val totalPages = if ((uiState.limit > 0) && (uiState.total > 0)) {
                        (uiState.total + uiState.limit - 1) / uiState.limit
                    } else {
                        1
                    }

                    PageSwitcher(
                        page = uiState.page,
                        totalPages = totalPages,
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
        CollaboratorFilterDialog(
            initialFilter = uiState.statusFilter,
            onApply = { filter ->
                showFilterDialog = false
                viewModel.applyStatusFilter(filter)
            },
            onDismiss = { showFilterDialog = false },
        )
    }
}
