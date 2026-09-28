package com.example.adaxintegra.presentation.views.designsystem.organisms

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppButton
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppIcon
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.ButtonVariant
import com.example.adaxintegra.presentation.views.designsystem.atoms.IconSize
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.presentation.views.designsystem.molecules.LabeledTextField
import com.example.adaxintegra.ui.theme.LightPurple

@Suppress("ktlint:standard:function-naming")
@Composable
fun LoginForm(
    emailValue: String,
    onEmailChange: (String) -> Unit,
    passwordValue: String,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    errorMessage: String? = null,
) {
    var passwordVisible by remember { mutableStateOf(value = false) }

    Column(modifier = modifier.fillMaxWidth()) {
        LabeledTextField(
            label = "Correo electrónico",
            value = emailValue,
            onValueChange = onEmailChange,
            placeholder = "tu@correo.com",
            leadingIcon = {
                AppIcon(
                    imageVector = Icons.Default.Mail,
                    contentDescription = null,
                    size = IconSize.RegularIcon,
                    tint = Color.Gray,
                )
            },
        )

        Spacer(modifier = Modifier.height(16.dp))

        LabeledTextField(
            label = "Contraseña",
            value = passwordValue,
            onValueChange = onPasswordChange,
            leadingIcon = {
                AppIcon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    size = IconSize.RegularIcon,
                    tint = Color.Gray,
                )
            },
            trailingIcon = {
                val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    AppIcon(
                        imageVector = image,
                        contentDescription = null,
                        size = IconSize.RegularIcon,
                        tint = Color.Gray,
                    )
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        )

        Spacer(modifier = Modifier.height(40.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            AppButton(
                text = "Iniciar Sesión",
                onClick = onLoginClick,
                variant = ButtonVariant.Primary,
                isLoading = isLoading,
                modifier = Modifier.weight(1f),
            )

            AppButton(
                text = "Registrarse",
                onClick = onRegisterClick,
                variant = ButtonVariant.Outlined,
                enabled = !isLoading,
                modifier = Modifier.weight(1f),
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Recuperar contraseña",
            style = AppTextStyle.BodySmall,
            color = LightPurple,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onForgotPasswordClick() },
        )

        errorMessage?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = it,
                style = AppTextStyle.BodyMedium,
                color = Color.Red,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
