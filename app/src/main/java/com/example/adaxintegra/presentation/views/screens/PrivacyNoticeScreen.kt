package com.example.adaxintegra.presentation.views.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppIcon
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppIcons
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.IconSize
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.ui.theme.AdaxIntegraTheme
import com.example.adaxintegra.ui.theme.Purple

data class PrivacyItem(
    val number: String,
    val title: String,
    val description: String
)

@Composable
fun PrivacyNoticeScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val privacyItems = listOf(
        PrivacyItem(
            "1",
            "¿Qué datos recopilamos?",
            "El aviso especifica nombre, edad/fecha de nacimiento, teléfono, correo, domicilio, INE y comprobante de domicilio."
        ),
        PrivacyItem(
            "2",
            "¿Cómo usamos tus datos?",
            "Integrar tu expediente y darte seguimiento sí corresponden"
        ),
        PrivacyItem(
            "3",
            "Protección de tu información",
            "Aplicamos medidas técnicas y organizativas para proteger tus datos."
        ),
        PrivacyItem(
            "4",
            "Compartición de datos",
            "No compartimos tu información con terceros, salvo obligación legal."
        ),
        PrivacyItem(
            "5",
            "Tus derechos",
            "Puedes acceder, rectificar o solicitar la eliminación de tus datos en cualquier momento."
        )
    )

    Scaffold(
        containerColor = Color(0xFFF3F3F3) // Light grey background matching the image
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            // Top Bar with back navigation arrow and Title
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppIcon(
                    imageVector = AppIcons.Return,
                    contentDescription = "Regresar",
                    tint = Purple,
                    size = IconSize.LargeIcon,
                    modifier = Modifier.clickable { onBackClick() }
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Text(
                    text = "Aviso de privacidad",
                    style = AppTextStyle.TitleLarge,
                    color = Color.Black
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Subtitle
            Text(
                text = "Tu información está segura con nosotras",
                style = AppTextStyle.TitleMedium,
                color = Purple,
                modifier = Modifier.padding(start = 32.dp)
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Description paragraph
            Text(
                text = "Antes de continuar, es importante que conozcas cómo usamos y protegemos tus datos personales.",
                style = AppTextStyle.BodyMedium,
                color = Color(0xFF757575),
                modifier = Modifier.padding(start = 32.dp, end = 16.dp)
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Main Content Card containing the numbered list
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    privacyItems.forEachIndexed { index, item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 16.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            // Circular Badge Number
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(color = Color(0xFFC38CBA), shape = CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = item.number,
                                    style = AppTextStyle.BodyMedium,
                                    color = Purple,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                            
                            Spacer(modifier = Modifier.width(16.dp))
                            
                            // Text block (Title and Description)
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = item.title,
                                    style = AppTextStyle.BodyLarge,
                                    color = Color.Black,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                                Text(
                                    text = item.description,
                                    style = AppTextStyle.BodyMedium,
                                    color = Color(0xFF757575)
                                )
                            }
                        }
                        
                        // Thin separator line between items (except the last one)
                        if (index < privacyItems.lastIndex) {
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                thickness = 1.dp,
                                color = Color(0xFFEBEBEB)
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PrivacyNoticeScreenPreview() {
    AdaxIntegraTheme {
        PrivacyNoticeScreen(onBackClick = {})
    }
}
