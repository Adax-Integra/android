package com.example.adaxintegra.presentation.views.designsystem.organisms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppIcons
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.presentation.views.designsystem.molecules.DocumentUploadCard
import com.example.adaxintegra.presentation.views.designsystem.molecules.UploadedFileRow
import com.example.adaxintegra.ui.theme.AdaxIntegraTheme
import com.example.adaxintegra.ui.theme.Purple

// R-06: one document of "Mis documentos"
// Once the document is uploaded the dashed box is replaced by an UploadedFileRow
@Suppress("ktlint:standard:function-naming")
@Composable
fun DocumentUploadSection(
    title: String,
    note: String,
    fileName: String,
    isUploaded: Boolean,
    onPickFromGallery: () -> Unit,
    onPickFromFiles: () -> Unit,
    onDownload: () -> Unit,
    modifier: Modifier = Modifier,
    isUploading: Boolean = false,
    isDownloading: Boolean = false,
) {
    var isShowingNote by remember { mutableStateOf(false) }

    // The saved document stays until the new one is uploaded
    var isReplacing by remember { mutableStateOf(false) }

    // When the upload ends, success or not, the saved document is shown again
    LaunchedEffect(isUploading) {
        if (!isUploading) {
            isReplacing = false
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = title,
                style = AppTextStyle.LabelMedium,
                fontWeight = FontWeight.Bold,
            )

            IconButton(onClick = { isShowingNote = true }) {
                Icon(
                    imageVector = AppIcons.Info,
                    contentDescription = "Más información de $title",
                    tint = Purple,
                    modifier = Modifier.size(16.dp),
                )
            }
        }

        if (isUploaded && !isReplacing) {
            UploadedFileRow(
                fileName = fileName,
                onDownload = onDownload,
                onReplace = { isReplacing = true },
                isDownloading = isDownloading,
            )
        } else {
            DocumentUploadCard(
                onPickFromGallery = onPickFromGallery,
                onPickFromFiles = onPickFromFiles,
                isUploading = isUploading,
            )

            if (isReplacing && !isUploading) {
                TextButton(
                    onClick = { isReplacing = false },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = "Cancelar",
                        style = AppTextStyle.LabelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Purple,
                    )
                }
            }
        }
    }

    if (isShowingNote) {
        AlertDialog(
            onDismissRequest = { isShowingNote = false },
            title = {
                Text(
                    text = title,
                    style = AppTextStyle.TitleMedium,
                    fontWeight = FontWeight.Bold,
                )
            },
            text = { Text(text = note) },
            confirmButton = {
                TextButton(onClick = { isShowingNote = false }) {
                    Text(
                        text = "Entendido",
                        color = Purple,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            },
        )
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
private fun DocumentUploadSectionPreview() {
    AdaxIntegraTheme(dynamicColor = false) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp),
        ) {
            DocumentUploadSection(
                title = "Comprobante de identidad",
                note = "Puedes usar tu INE, pasaporte o cualquier documento que compruebe tu identidad",
                fileName = "comprobante-identidad.jpg",
                isUploaded = false,
                onPickFromGallery = {},
                onPickFromFiles = {},
                onDownload = {},
            )

            DocumentUploadSection(
                title = "Comprobante de domicilio",
                note = "Usa un comprobante de domicilio que contenga tu dirección de vivienda",
                fileName = "comprobante-domicilio.jpg",
                isUploaded = true,
                onPickFromGallery = {},
                onPickFromFiles = {},
                onDownload = {},
            )
        }
    }
}
