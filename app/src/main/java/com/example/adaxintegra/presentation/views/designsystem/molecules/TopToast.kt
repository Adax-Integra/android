package com.example.adaxintegra.presentation.views.designsystem.molecules

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.ui.theme.Purple
import com.example.adaxintegra.ui.theme.White
import kotlinx.coroutines.delay

private const val TOAST_DURATION_MILLIS = 4_000L

// G-07: short purple message that slides from the top and hides by itself
@Suppress("ktlint:standard:function-naming")
@Composable
fun TopToast(
    message: String,
    isVisible: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LaunchedEffect(isVisible) {
        if (isVisible) {
            delay(TOAST_DURATION_MILLIS)
            onDismiss()
        }
    }

    AnimatedVisibility(
        visible = isVisible,
        modifier = modifier,
        enter = slideInVertically { -it } + fadeIn(),
        exit = slideOutVertically { -it } + fadeOut(),
    ) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Purple,
            shadowElevation = 6.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
            Text(
                text = message,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                style = AppTextStyle.BodyMedium,
                color = White,
            )
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
private fun TopToastPreview() {
    TopToast(message = "Datos actualizados", isVisible = true, onDismiss = {})
}
