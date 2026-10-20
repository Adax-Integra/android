package com.example.adaxintegra.presentation.views.designsystem.molecules

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.ui.theme.IconGrey
import com.example.adaxintegra.ui.theme.Purple

// G-07: checkbox with a title and a short explanation, the whole row can be tapped
@Suppress("ktlint:standard:function-naming")
@Composable
fun ConsentCheckboxRow(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    errorMessage: String? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .toggleable(
                value = checked,
                onValueChange = onCheckedChange,
                role = Role.Checkbox,
            ),
        verticalAlignment = Alignment.Top,
    ) {
        // The row handles the tap, so the checkbox only draws the state
        Checkbox(
            checked = checked,
            onCheckedChange = null,
            modifier = Modifier.padding(12.dp),
            colors = CheckboxDefaults.colors(
                checkedColor = Purple,
                uncheckedColor = if (errorMessage != null) Color.Red else IconGrey,
            ),
        )

        Column(
            modifier = Modifier.padding(top = 10.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = title,
                style = AppTextStyle.BodyLarge,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = description,
                style = AppTextStyle.BodySmall,
                color = IconGrey,
            )
            if (errorMessage != null) {
                Text(
                    text = errorMessage,
                    style = AppTextStyle.BodySmall,
                    color = Color.Red,
                )
            }
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
private fun ConsentCheckboxRowPreview() {
    ConsentCheckboxRow(
        checked = false,
        onCheckedChange = {},
        title = "La externa autorizó este cambio",
        description = "Confirmo que la externa dio su consentimiento para modificar sus datos.",
        errorMessage = "Confirma que la externa autorizó el cambio",
    )
}
