package com.example.adaxintegra.presentation.navigation

import androidx.compose.foundation.layout.consumeWindowInsets
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
import com.example.adaxintegra.presentation.views.screens.cases.CaseProgressScreen
import com.example.adaxintegra.presentation.views.screens.cases.CasesScreen
import com.example.adaxintegra.presentation.views.screens.records.RecordsMenuScreen

// provide values(screens) to BottomNavBar
// general navigation routes, provides screens
@Suppress("ktlint:standard:function-naming")
@Composable
fun AppNavigation(isInternal: Boolean = true) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            BottomNavBar(
                currentRoute = if (
                    isInternal && currentRoute == "collaboratorCases"
                ) {
                    "records"
                } else {
                    currentRoute
                },
                onNavigateToRoute = { route ->
                    navController.navigate(route) {
                        launchSingleTop = true
                    }
                },
                isInternal = isInternal,
            )
        },
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding),
        ) {
            composable("home") {
            }

            composable("records") {
                RecordsMenuScreen(
                    onAllCasesClick = {
                        navController.navigate("collaboratorCases") {
                            launchSingleTop = true
                        }
                    },
                )
            }

            composable("cases") {
                // Connect the external user's case history here.
            }

            composable("collaboratorCases") {
                val viewModel: CasesViewModel = hiltViewModel()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                CasesScreen(
                    uiState = uiState,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onSearchChange = viewModel::searchCases,
                    onUrgencyChange = viewModel::filterCases,
                    onClearFilters = viewModel::clearFilters,
                    onRetry = viewModel::retry,
                    onNextPage = viewModel::nextPage,
                    onPreviousPage = viewModel::previousPage,
                )
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
