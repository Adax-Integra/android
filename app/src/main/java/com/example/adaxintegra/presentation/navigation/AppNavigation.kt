package com.example.adaxintegra.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.adaxintegra.presentation.viewmodel.LoginViewModel
import com.example.adaxintegra.presentation.views.screens.HomeScreen
import com.example.adaxintegra.presentation.views.screens.LoginScreen
import com.example.adaxintegra.presentation.views.screens.ProfileScreen
import com.example.adaxintegra.presentation.views.screens.cases.CaseProgressScreen
import com.example.adaxintegra.presentation.views.screens.cases.CasesScreen

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
            if (currentRoute != "login") {
                BottomNavBar(
                    currentRoute = currentRoute,
                    onNavigateToRoute = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                )
            }
        },
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = "login",
            modifier = Modifier.padding(innerPadding),
        ) {
            composable("login") {
                val viewModel: LoginViewModel = hiltViewModel()
                LoginScreen(
                    viewModel = viewModel,
                    onNavigateToHome = {
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    },
                )
            }

            composable("home") {
                HomeScreen()
            }

            composable("cases") {
                CasesScreen()
            }

            composable("profile") {
                ProfileScreen()
            }

            composable("case/{caseId}/{caseState}") { backStackEntry ->
                val caseId = backStackEntry.arguments?.getString("caseId")
                val caseState = backStackEntry.arguments?.getString("caseState")

                CaseProgressScreen(
                    caseId = caseId ?: "",
                    caseState = caseState ?: "",
                    onBack = {
                        navController.popBackStack()
                    },
                )
            }
        }
    }
}
