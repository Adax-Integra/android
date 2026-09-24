package com.example.adaxintegra.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.adaxintegra.presentation.views.screens.PrivacyNoticeScreen
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.presentation.views.designsystem.organisms.BottomNavBar

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            BottomNavBar(
                currentRoute = currentRoute,
                onNavigateToRoute = { route ->
                    navController.navigate(route)
                }
            )
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = "privacy_notice",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") {
                Text("Home Screen")
            }

            composable("cases"){
            }

            composable("profile"){
            }

            composable("privacy_notice") {
                PrivacyNoticeScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}