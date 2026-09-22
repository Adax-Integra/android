package com.example.adaxintegra.presentation.views.screens.cases

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.remember
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
import com.example.adaxintegra.presentation.views.designsystem.molecules.CaseCard
import com.example.adaxintegra.ui.theme.AdaxIntegraTheme
import java.text.Normalizer
import java.util.Locale

@Suppress("ktlint:standard:function-naming")
@Composable
fun CasesScreen(
    cases: List<Case>,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // Save simple UI state when the device rotates.
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var selectedUrgency by rememberSaveable { mutableStateOf("Todas") }
    var pendingUrgency by rememberSaveable { mutableStateOf("Todas") }
    var showFilters by rememberSaveable { mutableStateOf(false) }

    // This first version searches the supplied local list, not the entire API.
    val visibleCases = remember(cases, searchQuery, selectedUrgency) {
        val query = normalizeSearch(searchQuery.trim())
        cases.filter { case ->
            val matchesSearch = normalizeSearch(case.name).contains(query) ||
                normalizeSearch(case.caseId).contains(query)
            val matchesUrgency = selectedUrgency == "Todas" ||
                case.urgency == selectedUrgency
            matchesSearch && matchesUrgency
        }.sortedByDescending { it.severity ?: 0 }
    }

    Scaffold(modifier = modifier) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
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
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    placeholder = {
                        Text(
                            text = "Buscar por nombre o caso",
                            fontSize = 14.sp,
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
            if (searchQuery.isNotEmpty() || selectedUrgency != "Todas") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Urgencia: $selectedUrgency",
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.bodySmall,
                    )
                    TextButton(onClick = {
                        searchQuery = ""
                        selectedUrgency = "Todas"
                    }) {
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
                        text = "${visibleCases.size} ${if (visibleCases.size == 1) "caso" else "casos"}",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        style = MaterialTheme.typography.labelSmall,
                    )
                }
            }
            if (visibleCases.isEmpty()) {
                Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(
                        text = if (cases.isEmpty()) {
                            "No hay casos disponibles."
                        } else {
                            "No hay casos que coincidan con tu búsqueda y filtro."
                        },
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    items(items = visibleCases, key = { it.caseId }) { case ->
                        CaseCard(case = case)
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
                            modifier = Modifier.fillMaxWidth()
                                .selectable(
                                    selected = pendingUrgency == urgency,
                                    onClick = { pendingUrgency = urgency },
                                    role = Role.RadioButton,
                                )
                                .padding(vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            RadioButton(selected = pendingUrgency == urgency, onClick = null)
                            Text(urgency)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    selectedUrgency = pendingUrgency
                    showFilters = false
                }) {
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

// Let searches such as "Maria" match names such as "María".
private fun normalizeSearch(value: String): String = Normalizer.normalize(value, Normalizer.Form.NFD)
    .replace(Regex("\\p{M}+"), "")
    .lowercase(Locale.ROOT)

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true, widthDp = 400, heightDp = 850)
@Composable
private fun CasesScreenPreview() {
    AdaxIntegraTheme(dynamicColor = false) {
        CasesScreen(
            cases = Case.getMockData(),
            onBackClick = {}, // When connecting to navigation system: navController.popBackStack()
        )
    }
}
