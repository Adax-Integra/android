package com.example.adaxintegra.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.adaxintegra.presentation.viewmodel.CasesViewModel
import com.example.adaxintegra.presentation.views.screens.HomeScreen
import com.example.adaxintegra.presentation.views.screens.PrivacyNoticeScreen
import com.example.adaxintegra.presentation.views.screens.ProfileScreen
import com.example.adaxintegra.presentation.views.screens.cases.CasesScreen

//provide values(screens) to BottomNavBar
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val bottomNavRoutes =
        listOf(
            BottomNavBarItem.Inicio.route,
            BottomNavBarItem.MisCasos.route,
            BottomNavBarItem.Perfil.route,
        )
    val showBottomBar = currentRoute in bottomNavRoutes

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavBar(
                    currentRoute = currentRoute,
                    onNavigateToRoute = { route ->
                        navController.navigate(route) {
                            popUpTo(BottomNavBarItem.Inicio.route) {
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
            startDestination = "privacy_notice",
            modifier = Modifier.padding(innerPadding),
        ) {
            composable("privacy_notice") {
                PrivacyNoticeScreen(
                    onBackClick = {
                        if (!navController.popBackStack()) {
                            navController.navigate("home") {
                                popUpTo("privacy_notice") { inclusive = true }
                            }
                        }
                    },
                )
            }

            composable("home") {
                HomeScreen()
            }

            composable("cases") {
                val casesViewModel: CasesViewModel = hiltViewModel()
                val uiState by casesViewModel.uiState.collectAsStateWithLifecycle()

                CasesScreen(
                    uiState = uiState,
                    onBackClick = { navController.popBackStack() },
                    onSearchChange = casesViewModel::searchCases,
                    onUrgencyChange = casesViewModel::filterCases,
                    onClearFilters = casesViewModel::clearFilters,
                    onRetry = casesViewModel::retry,
                    onNextPage = casesViewModel::nextPage,
                    onPreviousPage = casesViewModel::previousPage,
                )
            }

            composable("profile") {
                ProfileScreen()
            }
        }
    }
}
