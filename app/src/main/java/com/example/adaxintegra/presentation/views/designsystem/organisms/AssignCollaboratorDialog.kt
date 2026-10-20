package com.example.adaxintegra.presentation.views.designsystem.organisms

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.adaxintegra.domain.model.CollaboratorSummary
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppButton
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.ButtonVariant
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.presentation.views.designsystem.molecules.SearchBar

@Suppress("ktlint:standard:function-naming")
@Composable
fun AssignCollaboratorDialog(
    collaborators: List<CollaboratorSummary>,
    assignedIds: Set<String>,
    isSaving: Boolean = false,
    onConfirm: (Set<String>) -> Unit,
    onDismiss: () -> Unit,
    errorMessage: String? = null,
) {
    var searchQuery by remember { mutableStateOf("") }
    val selectedIds = remember(assignedIds) {
        mutableStateListOf(*assignedIds.toTypedArray())
    }


    val filteredCollaborators = remember(searchQuery, collaborators) {
        if (searchQuery.isBlank()) {
            collaborators
        } else {
            collaborators.filter {
                val fullName = "${it.name} ${it.lastName}"
                fullName.contains(searchQuery, ignoreCase = true) ||
                    (it.email?.contains(searchQuery, ignoreCase = true) == true)
            }
        }
    }

    val canSave = selectedIds.toSet() != assignedIds && !isSaving
    val buttonText = "Guardar cambios"

    Dialog(onDismissRequest = { if (!isSaving) onDismiss() }) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
            ),
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    text = "Administrar colaboradoras",
                    style = AppTextStyle.TitleMedium,
                    fontWeight = FontWeight.Bold,
                )

                HorizontalDivider()

                SearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    placeholder = "Buscar por nombre",
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 280.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    if (filteredCollaborators.isEmpty()) {
                        item {
                            Text(
                                text = if (collaborators.isEmpty()) "No hay colaboradoras activas" else "Sin resultados",
                                style = AppTextStyle.BodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(vertical = 16.dp),
                            )
                        }
                    } else {
                        items(
                            items = filteredCollaborators,
                            key = { it.userId },
                        ) { collaborator ->
                            val isAlreadyAssigned = collaborator.userId in assignedIds
                            val isChecked = collaborator.userId in selectedIds

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                    color = if (isChecked) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f) else Color.Transparent,
                    shape = RoundedCornerShape(12.dp),
                )
                .border(
                    width = 1.dp,
                    color = if (isChecked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                    shape = RoundedCornerShape(12.dp),
                )
                                    .clickable(enabled = !isSaving) {
                                        if (isChecked) {
                                            selectedIds.remove(collaborator.userId)
                                        } else {
                                            selectedIds.add(collaborator.userId)
                                        }
                                    }
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "${collaborator.name} ${collaborator.lastName}",
                                        style = AppTextStyle.BodyMedium,
                                        fontWeight = FontWeight.Medium,
                                    )
                                    if (isAlreadyAssigned) {
                                        Text(
                                            text =
    when {
        !isChecked -> "Se quitará"
        !collaborator.isActive -> "Asignada (no disponible)"
        else -> "Asignada"
    },
                                            style = AppTextStyle.LabelSmall,
                                            color = MaterialTheme.colorScheme.primary,
                                        )
                                    } else collaborator.email?.let { email ->
                                        Text(
                                            text = email,
                                            style = AppTextStyle.LabelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        )
                                    }
                                }

                                Checkbox(
                                    checked = isChecked,
                                    onCheckedChange = { checked ->
                                        if (!isSaving) {
                                            if (checked) {
                                                selectedIds.add(collaborator.userId)
                                            } else {
                                                selectedIds.remove(collaborator.userId)
                                            }
                                        }
                                    },
                                    enabled = !isSaving,
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = MaterialTheme.colorScheme.primary,
                                    ),
                                )
                            }
                        }
                    }
                }

                if (!errorMessage.isNullOrBlank()) {
                    Text(
                        text = errorMessage,
                        style = AppTextStyle.BodySmall,
                        color = MaterialTheme.colorScheme.error,
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    AppButton(
                        text = buttonText,
                        onClick = { onConfirm(selectedIds.toSet()) },
                        modifier = Modifier.weight(1f),
                        enabled = canSave,
                        isLoading = isSaving,
                    )
                    AppButton(
                        text = "Cancelar",
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        variant = ButtonVariant.Outlined,
                        enabled = !isSaving,
                    )
                }
            }
        }
    }
}
