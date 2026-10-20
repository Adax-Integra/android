package com.example.adaxintegra.presentation.views.designsystem.molecules

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppIcons
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.ui.theme.AdaxIntegraTheme
import com.example.adaxintegra.ui.theme.Purple

// R-06: name of an uploaded document with buttons to download or replace it,
// the card looks like MenuOptionCard
@Suppress("ktlint:standard:function-naming")
@Composable
fun UploadedFileRow(
    fileName: String,
    onDownload: () -> Unit,
    onReplace: () -> Unit,
    modifier: Modifier = Modifier,
    // While the file is downloaded its button shows a spinner
    isDownloading: Boolean = false,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 4.dp, top = 4.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = AppIcons.UploadedImage,
                contentDescription = null,
                tint = Purple,
                modifier = Modifier.size(22.dp),
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = fileName,
                modifier = Modifier.weight(1f),
                style = AppTextStyle.BodyMedium,
                fontWeight = FontWeight.Medium,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )

            if (isDownloading) {
                Box(
                    modifier = Modifier.size(48.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Purple,
                        strokeWidth = 2.dp,
                    )
                }
            } else {
                IconButton(onClick = onDownload) {
                    Icon(
                        imageVector = AppIcons.Download,
                        contentDescription = "Descargar $fileName",
                        tint = Purple,
                    )
                }
            }

            IconButton(
                onClick = onReplace,
                enabled = !isDownloading,
            ) {
                Icon(
                    imageVector = AppIcons.Replace,
                    contentDescription = "Reemplazar $fileName",
                    tint = Purple,
                )
            }
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
private fun UploadedFileRowPreview() {
    AdaxIntegraTheme(dynamicColor = false) {
        UploadedFileRow(
            fileName = "comprobante-identidad.jpg",
            onDownload = {},
            onReplace = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}
