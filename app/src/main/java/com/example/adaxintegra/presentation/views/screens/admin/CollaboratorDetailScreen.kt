package com.example.adaxintegra.presentation.views.screens.admin

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.adaxintegra.presentation.viewmodel.CollaboratorDetailViewModel
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppButton
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.Spacing
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.presentation.views.designsystem.organisms.CollaboratorDetailContent
import com.example.adaxintegra.presentation.views.designsystem.templates.ScreenTemplate
import com.example.adaxintegra.ui.theme.Purple

// G-10: detail of a collaborator, opened from the G-06 list (admin only).
// Loads the data and decides what to show, the content draws it
@Suppress("ktlint:standard:function-naming")
@Composable
fun CollaboratorDetailScreen(
    userId: String,
    onBack: () -> Unit,
    viewModel: CollaboratorDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val spacing = Spacing()
    val context = LocalContext.current

    // Same clipboard manager used in OtpCodeInput
    @Suppress("DEPRECATION")
    val clipboardManager = LocalClipboardManager.current

    val collaborator = uiState.collaborator
    val errorMessage = uiState.error

    // Reloads the collaborator whenever the selected one changes
    LaunchedEffect(userId) {
        viewModel.loadCollaborator(userId)
    }

    // Copies the text and confirms it with a short message
    val copyText: (String) -> Unit = { text ->
        clipboardManager.setText(AnnotatedString(text))
        Toast.makeText(context, "Copiado", Toast.LENGTH_SHORT).show()
    }

    ScreenTemplate(
        title = "Detalle de colaboradora",
        onBack = onBack,
        modifier = Modifier.padding(horizontal = spacing.medium),
    ) {
        when {
            uiState.isLoading && collaborator == null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(color = Purple)
                }
            }

            errorMessage != null && collaborator == null -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(spacing.medium, Alignment.CenterVertically),
                ) {
                    Text(
                        text = errorMessage,
                        style = AppTextStyle.BodyMedium,
                        textAlign = TextAlign.Center,
                    )
                    AppButton(text = "Reintentar", onClick = viewModel::retry)
                }
            }

            collaborator != null -> {
                CollaboratorDetailContent(
                    collaborator = collaborator,
                    onCopy = copyText,
                )
            }
        }
    }
}
