package com.example.adaxintegra.presentation.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppIcons

//bottom navigation bar items (icon and label)
sealed class BottomNavBarItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    data object Inicio : BottomNavBarItem(
        route = "home",
        title = "Inicio",
        icon = AppIcons.Home
    )

    data object MisCasos : BottomNavBarItem(
        route = "cases",
        title = "Mis Casos",
        icon = AppIcons.Folder
    )

    data object Perfil : BottomNavBarItem(
        route = "profile",
        title = "Perfil",
        icon = AppIcons.Profile
    )
}