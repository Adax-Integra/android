package com.example.adaxintegra.presentation.views.designsystem.organisms

import android.opengl.Visibility
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.presentation.viewmodel.RegisterUiState
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppButton
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppIcon
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.ButtonVariant
import com.example.adaxintegra.presentation.views.designsystem.atoms.IconSize
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.presentation.views.designsystem.molecules.LabeledTextField
import com.example.adaxintegra.presentation.views.designsystem.molecules.PhoneField
import com.example.adaxintegra.ui.theme.AdaxIntegraTheme
import java.util.Locale

@Suppress("ktlint:standard:function-naming")
@Composable
fun RegisterForm(
    countryCode: String,
    phoneValue: String,
    onPhoneChange: (String) -> Unit,
    emailValue: String,
    onEmailChange: (String) -> Unit,
    passwordValue: String,
    onPasswordChange: (String) -> Unit,
    confirmPasswordValue: String,
    onConfirmPasswordChange: (String) -> Unit,
    isPasswordVisible: Boolean,
    onTogglePasswordVisibility: () -> Unit,
    isConfirmPasswordVisible: Boolean,
    onToggleConfirmPasswordVisibility: () -> Unit,
    onRegisterClick: () -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    errorMessage: String? = null,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        PhoneField(
            countryCode = countryCode,
            phoneValue = phoneValue,
            onPhoneChange = onPhoneChange,
        )
        Spacer(modifier = Modifier.height(16.dp))

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
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
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
                val image = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                IconButton(onClick = onTogglePasswordVisibility) {
                    AppIcon(
                        imageVector = image,
                        contentDescription = null,
                        size = IconSize.RegularIcon,
                        tint = Color.Gray,
                    )
                }
            },
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "La Contraseña debe ser de 8 o más carácteres",
            style = AppTextStyle.BodySmall,
            color = Color.Gray,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(16.dp))

        LabeledTextField(
            label = "Confirmar contraseña",
            value = confirmPasswordValue,
            onValueChange = onConfirmPasswordChange,
            leadingIcon = {
                AppIcon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    size = IconSize.RegularIcon,
                    tint = Color.Gray,
                )
            },
            trailingIcon = {
                val image = if (isConfirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                IconButton(onClick = onToggleConfirmPasswordVisibility) {
                    AppIcon(
                        imageVector = image,
                        contentDescription = null,
                        size = IconSize.RegularIcon,
                        tint = Color.Gray,
                    )
                }
            },
            visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        )

        Spacer(modifier = Modifier.height(28.dp))

        errorMessage?.let {
            Text(
                text = it,
                style = AppTextStyle.BodyMedium,
                color = Color.Red,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        AppButton(
            text = "Registrarse",
            onClick = onRegisterClick,
            variant = ButtonVariant.Primary,
            isLoading = isLoading,
            modifier = Modifier.fillMaxWidth(),

        )
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
private fun RegisterFormPreview() {
    AdaxIntegraTheme {
        RegisterForm(
            countryCode = "+52",
            phoneValue = "4421234567",
            onPhoneChange = {},
            emailValue = "usuario@ejemplo.com",
            onEmailChange = {},
            passwordValue = "12345678",
            onPasswordChange = {},
            confirmPasswordValue = "12345678",
            onConfirmPasswordChange = {},
            isPasswordVisible = false,
            onTogglePasswordVisibility = {},
            isConfirmPasswordVisible = false,
            onToggleConfirmPasswordVisibility = {},
            onRegisterClick = {},
            isLoading = false,
            modifier = Modifier.padding(16.dp),
        )
    }
}
