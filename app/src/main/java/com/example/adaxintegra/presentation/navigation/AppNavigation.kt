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
import com.example.adaxintegra.presentation.views.screens.cases.CaseProgressScreen

// provide values(screens) to BottomNavBar
// general navigation routes, provides screens
@Suppress("ktlint:standard:function-naming")
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
                },
            )
        },
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding),
        ) {
            composable("home") {
            }

            composable("cases") {
            }

            composable("profile") {
                // ProfileScreen
            }

            composable("case/{caseId}") { backStackEntry ->

                val caseId = backStackEntry.arguments?.getString("caseId")

                CaseProgressScreen(
                    caseId = caseId ?: "",
                    onBack = {
                        navController.popBackStack()
                    },
                )
            }
        }
    }
}
