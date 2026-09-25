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
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.presentation.views.designsystem.organisms.BottomNavBar
import com.example.adaxintegra.presentation.viewmodel.CasesViewModel
import com.example.adaxintegra.presentation.views.screens.cases.CaseProgressScreen
import com.example.adaxintegra.presentation.views.screens.cases.CasesScreen

//provide values(screens) to BottomNavBar
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
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") {
                Text("Home Screen")
            }

            composable("cases") {
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
                //ProfileScreen
            }
        }
    }
}
