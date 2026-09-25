package com.example.adaxintegra.presentation.navigation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppIcon
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.IconSize
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text

// construction of bottom navigation bar
@Suppress("ktlint:standard:function-naming")
@Composable
fun BottomNavBar(
    currentRoute: String?,
    onNavigateToRoute: (String) -> Unit,
    modifier: Modifier = Modifier,
    isInternal: Boolean = false,
) {
    val items =
        listOf(
            BottomNavBarItem.Inicio,
            if (isInternal) {
                BottomNavBarItem.MisExpedientes
            } else {
                BottomNavBarItem.MisCasos
            },
            BottomNavBarItem.Perfil,
        )

    NavigationBar(
        modifier = modifier.fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        items.forEach { item ->
            val isSelected = currentRoute == item.route

            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigateToRoute(item.route) },
                icon = {
                    AppIcon(
                        imageVector = item.icon,
                        contentDescription = item.title,
                        size = IconSize.LargeIcon,
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        style = AppTextStyle.LabelMedium,
                        fontWeight =
                        if (isSelected) {
                            FontWeight.Bold
                        } else {
                            FontWeight.Medium
                        },
                    )
                },
                alwaysShowLabel = true,
                colors =
                NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = Color.Transparent,
                ),
            )
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
fun BottomNavBarPreviewInicio() {
    BottomNavBar(
        currentRoute = BottomNavBarItem.Inicio.route,
        onNavigateToRoute = {},
    )
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
fun BottomNavBarPreviewMisCasos() {
    BottomNavBar(
        currentRoute = BottomNavBarItem.MisCasos.route,
        onNavigateToRoute = {},
    )
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
fun BottomNavBarPreviewPerfil() {
    BottomNavBar(
        currentRoute = BottomNavBarItem.Perfil.route,
        onNavigateToRoute = {},
    )
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
fun BottomNavBarPreviewMisExpedientes() {
    BottomNavBar(
        currentRoute = BottomNavBarItem.MisExpedientes.route,
        onNavigateToRoute = {},
        isInternal = true,
    )
}
