package com.example.adaxintegra.presentation.views.designsystem.organisms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.PillBadge
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.presentation.views.designsystem.molecules.SearchBar
import com.example.adaxintegra.ui.theme.IconGrey

@Suppress("ktlint:standard:function-naming")
@Composable
fun CasesListHeader(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    count: Int,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SearchBar(query = searchQuery, onQueryChange = onSearchQueryChange)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "ORDENADO POR FECHA",
                style = AppTextStyle.LabelSmall,
                fontWeight = FontWeight.Medium,
                color = IconGrey,
            )
            PillBadge(text = "$count Casos")
        }
    }
}
