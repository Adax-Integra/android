package com.example.adaxintegra.presentation.views.designsystem.atoms

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.ui.theme.Purple

enum class ButtonVariant {
    Primary,
    Outlined,
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: ButtonVariant = ButtonVariant.Primary,
    enabled: Boolean = true,
    isLoading: Boolean = false,
) {
    val shape = RoundedCornerShape(16.dp)

    when (variant) {
        ButtonVariant.Primary -> {
            Button(
                onClick = onClick,
                modifier = modifier.height(50.dp),
                enabled = enabled && !isLoading,
                shape = shape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Purple,
                    contentColor = Color.White,
                ),
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp),
                    )
                } else {
                    Text(
                        text = text,
                        style = AppTextStyle.BodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                    )
                }
            }
        }
        ButtonVariant.Outlined -> {
            OutlinedButton(
                onClick = onClick,
                modifier = modifier.height(50.dp),
                enabled = enabled && !isLoading,
                shape = shape,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Purple,
                ),
                border = BorderStroke(1.dp, Purple),
            ) {
                Text(
                    text = text,
                    style = AppTextStyle.BodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = Purple,
                )
            }
        }
    }
}
