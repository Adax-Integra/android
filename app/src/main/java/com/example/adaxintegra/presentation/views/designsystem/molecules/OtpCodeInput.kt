package com.example.adaxintegra.presentation.views.designsystem.molecules

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.ui.theme.Purple

/**
 * // G-09-VerifyOTP: Molecule component displaying a 6-digit OTP code entry box layout.
 * Features automatic digit focus, custom box borders, and paste support.
 */
@Suppress("ktlint:standard:function-naming")
@Composable
fun OtpCodeInput(
    otpCode: String,
    onOtpCodeChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    codeLength: Int = 6,
) {
    // // G-09-VerifyOTP: Focus requester to manage keyboard input focus on the hidden text field
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        // // G-09-VerifyOTP: Hidden BasicTextField managing numeric input and length constraint
        BasicTextField(
            value = otpCode,
            onValueChange = { newValue ->
                if (newValue.length <= codeLength && newValue.all { it.isDigit() }) {
                    onOtpCodeChange(newValue)
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            modifier = Modifier
                .matchParentSize()
                .focusRequester(focusRequester),
            decorationBox = { },
        )

        // // G-09-VerifyOTP: Visual row of 6 distinct rounded digit boxes
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { focusRequester.requestFocus() },
        ) {
            for (i in 0 until codeLength) {
                val digit = otpCode.getOrNull(i)?.toString() ?: ""
                val isFocused = otpCode.length == i || (i == codeLength - 1 && otpCode.length == codeLength)

                Box(
                    modifier = Modifier
                        .width(48.dp)
                        .height(56.dp)
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
}
