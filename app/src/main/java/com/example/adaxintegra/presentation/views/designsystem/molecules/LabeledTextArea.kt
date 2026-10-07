package com.example.adaxintegra.presentation.views.designsystem.molecules

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextField
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text

//Long free text with its label, its error and how much room is left,
// so the user sees the limit before the backend rejects the text
@Suppress("ktlint:standard:function-naming")
@Composable
fun LabeledTextArea(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    maxLength: Int,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    minLines: Int = 4,
    error: String? = null,
){
    Column(modifier = modifier.fillMaxWidth()){
        Text(
            text = label,
            style = AppTextStyle.BodySmall,
            color = Color.Gray,
            fontWeight = FontWeight.Medium,
        )

        Spacer(modifier = Modifier.height(8.dp))

        AppTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = placeholder,
            singleLine = false,
            minLines = minLines,
        )

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ){
            if (error != null){
                Text(
                    text = error,
                    modifier = Modifier.weight(1f),
                    style = AppTextStyle.LabelSmall,
                    color = MaterialTheme.colorScheme.error,
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }

            Text(
                text = "${value.length}/$maxLength",
                style = AppTextStyle.LabelSmall,
                color = Color.Gray,
            )
        }
    }
}
