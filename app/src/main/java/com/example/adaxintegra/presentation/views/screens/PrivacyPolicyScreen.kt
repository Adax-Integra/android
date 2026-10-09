package com.example.adaxintegra.presentation.views.screens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.adaxintegra.presentation.viewmodel.PrivacyPolicyViewModel
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppButton
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.ButtonVariant
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.presentation.views.designsystem.organisms.AppHeader
import com.example.adaxintegra.ui.theme.AdaxIntegraTheme
import com.example.adaxintegra.ui.theme.IconGrey
import com.example.adaxintegra.ui.theme.LightPurple
import com.example.adaxintegra.ui.theme.Purple
import com.example.adaxintegra.ui.theme.White

data class PrivacyPolicyItem(
    val number: Int,
    val title: String,
    val description: String,
)

@Suppress("ktlint:standard:function-naming")
@Composable
fun PrivacyPolicyScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    onContinue: () -> Unit = {},
    viewModel: PrivacyPolicyViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var accepted by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val items = listOf(
        PrivacyPolicyItem(
            number = 1,
            title = "¿Qué datos recopilamos?",
            description = "El aviso especifica nombre, edad/fecha de nacimiento, teléfono, correo, domicilio, INE y comprobante de domicilio.",
        ),
        PrivacyPolicyItem(
            number = 2,
            title = "¿Cómo usamos tus datos?",
            description = "Integrar tu expediente y darte seguimiento sí corresponde.",
        ),
        PrivacyPolicyItem(
            number = 3,
            title = "Protección de tu información",
            description = "Aplicamos medidas técnicas y organizativas para proteger tus datos.",
        ),
        PrivacyPolicyItem(
            number = 4,
            title = "Compartición de datos",
            description = "No compartimos tu información con terceros, salvo obligación legal.",
        ),
        PrivacyPolicyItem(
            number = 5,
            title = "Tus derechos",
            description = "Puedes acceder, rectificar o solicitar la eliminación de tus datos en cualquier momento.",
        ),
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        AppHeader(
            title = "Aviso de privacidad",
            onBack = onBack,
        )

        Text(
            text = "Tu información está segura con nosotras",
            style = AppTextStyle.BodyLarge,
            fontWeight = FontWeight.SemiBold,
            color = Purple,
        )

        Text(
            text = "Antes de continuar, es importante que conozcas cómo usamos y protegemos tus datos personales.",
            style = AppTextStyle.BodyMedium,
            color = IconGrey,
        )

        if (uiState.error != null) {
            Text(
                text = uiState.error ?: "",
                style = AppTextStyle.BodyMedium,
                color = Color.Red,
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Privacy Policy items card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                items.forEachIndexed { index, item ->
                    PrivacyPolicyRow(item = item)
                    if (index < items.size - 1) {
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                            thickness = 1.dp,
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Checkbox Card as shown in design
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Checkbox(
                    checked = accepted,
                    onCheckedChange = { accepted = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Purple,
                        uncheckedColor = IconGrey,
                    ),
                )

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    val annotatedString = buildAnnotatedString {
                        append("He leído y acepto el ")
                        pushStringAnnotation(tag = "POLICY", annotation = "policy")
                        withStyle(
                            style = SpanStyle(
                                color = Purple,
                                fontWeight = FontWeight.Bold,
                            ),
                        ) {
                            append("Aviso de privacidad")
                        }
                        pop()
                    }

                    ClickableText(
                        text = annotatedString,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface,
                        ),
                        onClick = { offset ->
                            annotatedString.getStringAnnotations(tag = "POLICY", start = offset, end = offset)
                                .firstOrNull()?.let {
                                    val url = viewModel.getDocumentUrl()
                                    if (!url.isNullOrBlank()) {
                                        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                                        context.startActivity(intent)
                                    }
                                }
                        },
                    )

                    Text(
                        text = "Al seleccionar esta casilla acepto el tratamiento de mis datos personales conforme al aviso.",
                        style = AppTextStyle.BodySmall,
                        color = IconGrey,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            AppButton(
                text = if (uiState.isSubmitting) "Guardando..." else "Continuar",
                onClick = {
                    viewModel.acceptPolicy(onContinue)
                },
                variant = ButtonVariant.Primary,
                enabled = accepted && !uiState.isSubmitting && !uiState.isLoading,
                modifier = Modifier.weight(1f),
            )

            AppButton(
                text = "Cancelar",
                onClick = onBack,
                variant = ButtonVariant.Outlined,
                modifier = Modifier.weight(1f),
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun PrivacyPolicyRow(
    item: PrivacyPolicyItem,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(LightPurple),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = item.number.toString(),
                style = AppTextStyle.BodyMedium,
                fontWeight = FontWeight.Bold,
                color = White,
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = item.title,
                style = AppTextStyle.TitleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Text(
                text = item.description,
                style = AppTextStyle.BodySmall,
                color = IconGrey,
            )
        }
    }
}

@Suppress("ktlint:standard:preview")
@Preview(showBackground = true)
@Composable
private fun PrivacyPolicyScreenPreview() {
    AdaxIntegraTheme(dynamicColor = false) {
        PrivacyPolicyScreen(onBack = {}, onContinue = {})
    }
}
