package com.example.adaxintegra.presentation.views.designsystem.molecules

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppIcons
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.ui.theme.AdaxIntegraTheme
import com.example.adaxintegra.ui.theme.Purple

// R-06: dashed box to add a document, same as ImagePickerField on iOS.
// Tapping it opens a menu to pick the picture from the gallery or from the files
@Suppress("ktlint:standard:function-naming")
@Composable
fun DocumentUploadCard(
    onPickFromGallery: () -> Unit,
    onPickFromFiles: () -> Unit,
    modifier: Modifier = Modifier,
    // While the document is being sent the box is locked and shows a spinner
    isUploading: Boolean = false,
    placeholder: String = "Agregar documento",
    hint: String = "PNG o JPG (máx. 5 MB)",
) {
    var isMenuOpen by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 150.dp)
                .drawBehind {
                    val cornerRadius = CornerRadius(16.dp.toPx())

                    drawRoundRect(
                        color = Purple.copy(alpha = 0.08f),
                        cornerRadius = cornerRadius,
                    )
                    drawRoundRect(
                        color = Purple,
                        cornerRadius = cornerRadius,
                        style = Stroke(
                            width = 1.5.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(
                                floatArrayOf(6.dp.toPx(), 4.dp.toPx()),
                            ),
                        ),
                    )
                }
                .clip(RoundedCornerShape(16.dp))
                .clickable(
                    enabled = !isUploading,
                    onClickLabel = placeholder,
                ) { isMenuOpen = true }
                .padding(horizontal = 16.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            if (isUploading) {
                CircularProgressIndicator(color = Purple)
            } else {
                Icon(
                    imageVector = AppIcons.Upload,
                    contentDescription = null,
                    tint = Purple,
                    modifier = Modifier.size(28.dp),
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = placeholder,
                    style = AppTextStyle.BodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = Purple,
                )

                Text(
                    text = hint,
                    style = AppTextStyle.BodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
            }
        }

        // Same two options as the menu on iOS
        DropdownMenu(
            expanded = isMenuOpen,
            onDismissRequest = { isMenuOpen = false },
        ) {
            DropdownMenuItem(
                text = { Text(text = "Galería") },
                leadingIcon = {
                    Icon(imageVector = AppIcons.Gallery, contentDescription = null)
                },
                onClick = {
                    isMenuOpen = false
                    onPickFromGallery()
                },
            )

            DropdownMenuItem(
                text = { Text(text = "Archivos") },
                leadingIcon = {
                    Icon(imageVector = AppIcons.Files, contentDescription = null)
                },
                onClick = {
                    isMenuOpen = false
                    onPickFromFiles()
                },
            )
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
private fun DocumentUploadCardPreview() {
    AdaxIntegraTheme(dynamicColor = false) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            DocumentUploadCard(
                onPickFromGallery = {},
                onPickFromFiles = {},
            )

            DocumentUploadCard(
                onPickFromGallery = {},
                onPickFromFiles = {},
                isUploading = true,
            )
        }
    }
}
