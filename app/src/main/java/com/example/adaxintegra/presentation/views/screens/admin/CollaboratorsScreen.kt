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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.adaxintegra.presentation.viewmodel.AddCollaboratorViewModel
import com.example.adaxintegra.presentation.viewmodel.CollaboratorsViewModel
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppButton
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.ButtonVariant
import com.example.adaxintegra.presentation.views.designsystem.atoms.Spacing
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.presentation.views.designsystem.molecules.CollaboratorCard
import com.example.adaxintegra.presentation.views.designsystem.molecules.PageSwitcher
import com.example.adaxintegra.presentation.views.designsystem.molecules.SearchBar
import com.example.adaxintegra.presentation.views.designsystem.organisms.AddCollaboratorDialog
import com.example.adaxintegra.presentation.views.designsystem.organisms.CollaboratorFilterDialog
import com.example.adaxintegra.presentation.views.designsystem.organisms.SuccessDialog
import com.example.adaxintegra.presentation.views.designsystem.templates.ScreenTemplate
import com.example.adaxintegra.ui.theme.IconGrey
import com.example.adaxintegra.ui.theme.Purple

private fun countText(count: Int): String =
    if (count == 1) "1 Colaboradora" else "$count Colaboradoras"

// Message when there is nothing to show
private fun emptyText(searchQuery: String, hasStatusFilter: Boolean): String = when {
    searchQuery.isNotBlank() -> "No hay colaboradoras que coincidan con tu búsqueda."
    hasStatusFilter -> "No hay colaboradoras con ese estatus."
    else -> "No hay colaboradoras registradas."
}

// G-06: admin's collaborator management screen, opened from the admin section.
// Shows every internal account in pages of 10 and "Agregar colaboradora"
// G-10: tapping a collaborator opens her detail
@Suppress("ktlint:standard:function-naming")
@Composable
fun CollaboratorsScreen(
    onBack: () -> Unit,
    onCollaboratorClick: (String) -> Unit,
    viewModel: AddCollaboratorViewModel = hiltViewModel(),
    listViewModel: CollaboratorsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val listState by listViewModel.uiState.collectAsStateWithLifecycle()
    var showFilterDialog by rememberSaveable { mutableStateOf(false) }
    val spacing = Spacing()
    val collaborators = listState.visibleCollaborators

    // G-04: loads the list when the screen is shown and when coming back from the detail,
    // so a status changed there is updated in the list
    LaunchedEffect(Unit) {
        listViewModel.loadCollaborators()
    }

    // Same template as the other screens, so it follows the light/dark theme
    ScreenTemplate(
        title = "Gestión de Colaboradoras",
        onBack = onBack,
        modifier = Modifier.padding(horizontal = spacing.medium),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = spacing.small),
            verticalArrangement = Arrangement.spacedBy(spacing.small),
        ) {
            // Search by name or email and Filtrar button
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(spacing.small),
            ) {
                SearchBar(
                    query = listState.searchQuery,
                    onQueryChange = listViewModel::onSearchQueryChange,
                    placeholder = "Nombre o correo",
                    modifier = Modifier.weight(1f),
                )
                AppButton(
                    text = "Filtrar",
                    onClick = { showFilterDialog = true },
                    variant = ButtonVariant.Outlined,
                )
            }

            // Active status filter, with the option to remove it
            if (listState.hasStatusFilter) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Filtro: ${listState.statusFilter.label}",
                        style = AppTextStyle.LabelMedium,
                        fontWeight = FontWeight.Medium,
                        color = Purple,
                        modifier = Modifier.weight(1f),
                    )
                    TextButton(onClick = listViewModel::clearStatusFilter) {
                        Text(text = "Quitar", color = Purple, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Number of collaborators found, not only the ones on this page
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = countText(listState.filteredCollaborators.size),
                    style = AppTextStyle.LabelMedium,
                    modifier = Modifier.weight(1f),
                )
                AppButton(
                    text = "Agregar colaboradora",
                    onClick = viewModel::openDialog,
                )
            }

            // The list takes the free space so the page buttons stay at the bottom
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            ) {
                when {
                    // First load, the list is still empty
                    listState.isLoading && listState.collaborators.isEmpty() -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularProgressIndicator(color = Purple)
                        }
                    }
                    // The list could not be loaded
                    listState.error != null && listState.collaborators.isEmpty() -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(spacing.medium, Alignment.CenterVertically),
                        ) {
                            Text(text = listState.error ?: "", style = AppTextStyle.BodyMedium)
                            AppButton(text = "Reintentar", onClick = listViewModel::loadCollaborators)
                        }
                    }

                    collaborators.isEmpty() -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = emptyText(listState.searchQuery, listState.hasStatusFilter),
                                style = AppTextStyle.BodyMedium,
                                color = IconGrey,
                            )
                        }
                    }

                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(spacing.small),
                            contentPadding = PaddingValues(bottom = spacing.medium),
                        ) {
                            items(collaborators) { collaborator ->
                                CollaboratorCard(
                                    name = collaborator.name,
                                    lastName = collaborator.lastName,
                                    email = collaborator.email,
                                    isActive = collaborator.isActive,
                                    onClick = { onCollaboratorClick(collaborator.userId) },
                                )
                            }
                        }
                    }
                }
            }

            // G-06: pages of 10 collaborators, as in the other lists
            if (listState.filteredCollaborators.isNotEmpty()) {
                PageSwitcher(
                    page = listState.currentPage,
                    canGoBack = listState.canGoBack,
                    canGoNext = listState.canGoNext,
                    onPreviousPage = listViewModel::previousPage,
                    onNextPage = listViewModel::nextPage,
                )
            }
        }
    }

    if (showFilterDialog) {
        CollaboratorFilterDialog(
            initialFilter = listState.statusFilter,
            onApply = { filter ->
                showFilterDialog = false
                listViewModel.applyStatusFilter(filter)
            },
            onDismiss = { showFilterDialog = false },
        )
    }

    if (uiState.isDialogOpen) {
        AddCollaboratorDialog(
            name = uiState.name,
            lastName = uiState.lastName,
            email = uiState.email,
            password = uiState.password,
            phone = uiState.phone,
            countryCode = uiState.countryCode,
            fieldErrors = uiState.fieldErrors,
            generalError = uiState.generalError,
            canSave = uiState.canSave,
            isSaving = uiState.isSaving,
            onNameChange = viewModel::onNameChange,
            onLastNameChange = viewModel::onLastNameChange,
            onEmailChange = viewModel::onEmailChange,
            onPasswordChange = viewModel::onPasswordChange,
            onPhoneChange = viewModel::onPhoneChange,
            onCountryCodeChange = viewModel::onCountryCodeChange,
            onSave = viewModel::save,
            onCancel = viewModel::cancel,
        )
    }

    val successMessage = uiState.successMessage
    if (successMessage != null) {
        SuccessDialog(
            title = "Registro completo",
            message = successMessage,
            onConfirm = {
                viewModel.dismissSuccessMessage()
                listViewModel.loadCollaborators()
            },
        )
    }
}
