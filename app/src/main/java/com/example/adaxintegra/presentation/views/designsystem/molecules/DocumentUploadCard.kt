
package com.example.adaxintegra.presentation.views.designsystem.molecules

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.ui.theme.Purple

@Suppress("ktlint:standard:function-naming")
@Composable
fun DocumentUploadCard(
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(112.dp)
            .drawBehind {
                drawRoundRect(
                    color = Purple,
                    cornerRadius = CornerRadius(12.dp.toPx()),
                    style = Stroke(
                        width = 1.5.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(
                            floatArrayOf(6.dp.toPx(), 4.dp.toPx()),
                        ),
                    ),
                )
            }
            .clickable(onClick = onClick)
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = Icons.Default.FileUpload,
            contentDescription = "Agregar documento",
            tint = Purple,
            modifier = Modifier.size(48.dp),
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Agregar documento",
            style = AppTextStyle.LabelSmall,
            color = Purple,
        )

        Text(
            text = "Formatos permitidos: PDF, PNG o JPG (máx. 4 MB)",
            style = AppTextStyle.BodySmall,
            color = Purple,
        )
    }
}
