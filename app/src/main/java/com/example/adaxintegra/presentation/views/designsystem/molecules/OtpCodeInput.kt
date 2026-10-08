package com.example.adaxintegra.presentation.views.designsystem.molecules

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.ui.theme.LightPurple
import com.example.adaxintegra.ui.theme.Purple

/**
 * // G-09-VerifyOTP: Fully responsive 6-digit OTP code input component.
 * Uses flexible weights (weight(1f)) to scale boxes seamlessly across any device screen size.
 * Includes paste support from clipboard manager.
 */
@Suppress("ktlint:standard:function-naming", "DEPRECATION")
@Composable
fun OtpCodeInput(
    otpCode: String,
    onOtpCodeChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    codeLength: Int = 6,
) {
    val focusRequester = remember { FocusRequester() }
    val clipboardManager = LocalClipboardManager.current

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            // // G-09-VerifyOTP: Hidden text field handling keyboard and pasted numeric input
            BasicTextField(
                value = otpCode,
                onValueChange = { newValue ->
                    val cleanValue = newValue.filter { it.isDigit() }
                    if (cleanValue.length <= codeLength) {
                        onOtpCodeChange(cleanValue)
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                modifier = Modifier
                    .matchParentSize()
                    .focusRequester(focusRequester),
                decorationBox = { },
            )

            // // G-09-VerifyOTP: Responsive row of digit boxes scaling with weight(1f) to prevent overlapping
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { focusRequester.requestFocus() },
            ) {
                for (i in 0 until codeLength) {
                    val digit = otpCode.getOrNull(i)?.toString() ?: ""
                    val isFocused = otpCode.length == i || (i == codeLength - 1 && otpCode.length == codeLength)

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(0.85f)
                            .background(
                                color = Color.White,
                                shape = RoundedCornerShape(12.dp),
                            )
                            .border(
                                width = if (isFocused) 2.dp else 1.dp,
                                color = if (isFocused) Purple else Color(0xFFD1D1D1),
                                shape = RoundedCornerShape(12.dp),
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = digit,
                            style = AppTextStyle.TitleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
        }

        // // G-09-VerifyOTP: Helper paste action if valid 6-digit code is present in clipboard
        val clipboardText = clipboardManager.getText()?.text?.filter { it.isDigit() } ?: ""
        if (clipboardText.length == codeLength && otpCode.isBlank()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Pegar código $clipboardText desde el portapapeles",
                style = AppTextStyle.BodySmall,
                color = LightPurple,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .clickable { onOtpCodeChange(clipboardText) }
                    .padding(vertical = 4.dp, horizontal = 8.dp),
            )
        }
    }
}
