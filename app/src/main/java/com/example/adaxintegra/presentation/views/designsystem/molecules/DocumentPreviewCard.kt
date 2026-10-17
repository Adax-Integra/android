package com.example.adaxintegra.presentation.views.designsystem.molecules

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.presentation.model.DocumentPreviewState
import com.example.adaxintegra.ui.theme.AdaxIntegraTheme

// V-13: read-only preview of one document of the record
@Suppress("ktlint:standard:function-naming")
@Composable
fun DocumentPreviewCard(
    title: String,
    state: DocumentPreviewState,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )

            when (state) {
                DocumentPreviewState.Empty -> {
                    Text(
                        text = "La usuaria aún no ha subido este documento",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }

                DocumentPreviewState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is DocumentPreviewState.Error -> {
                    Text(
                        text = state.message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error,
                    )
                    TextButton(onClick = onRetry) {
                        Text("Reintentar")
                    }
                }

                is DocumentPreviewState.Ready -> {
                    state.pages.forEachIndexed { index, page ->
                        Image(
                            bitmap = page,
                            contentDescription = if (state.pages.size > 1) {
                                "$title, página ${index + 1}"
                            } else {
                                title
                            },
                            contentScale = ContentScale.FillWidth,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp)),
                        )
                    }
                }
            }
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
private fun DocumentPreviewCardEmptyPreview() {
    AdaxIntegraTheme(dynamicColor = false) {
        DocumentPreviewCard(
            title = "Comprobante de domicilio",
            state = DocumentPreviewState.Empty,
            onRetry = {},
        )
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
private fun DocumentPreviewCardErrorPreview() {
    AdaxIntegraTheme(dynamicColor = false) {
        DocumentPreviewCard(
            title = "INE",
            state = DocumentPreviewState.Error("No se pudo cargar el documento"),
            onRetry = {},
        )
    }
}
