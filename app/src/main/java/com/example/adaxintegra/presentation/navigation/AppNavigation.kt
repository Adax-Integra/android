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
import com.example.adaxintegra.presentation.viewmodel.LoginViewModel
import com.example.adaxintegra.presentation.views.screens.HomeScreen
import com.example.adaxintegra.presentation.views.screens.LoginScreen
import com.example.adaxintegra.presentation.views.screens.ProfileScreen
import com.example.adaxintegra.presentation.views.screens.cases.CaseProgressScreen
import com.example.adaxintegra.presentation.views.screens.cases.CasesScreen
import com.example.adaxintegra.presentation.views.screens.cases.ExternalCasesScreen

// TODO: replace with the authenticated user's role once login is ready.
// false: collaborator case list (V-03). true: external user case list (V-04).
// Temporary: set to true to test V-04 locally.
private const val IS_EXTERNAL_USER = false

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
                    onNavigateToHome = { userRole ->
                        val targetRoute = if (!userRole.isNullOrBlank()) "home/$userRole" else "home"
                        navController.navigate(targetRoute) {
                            popUpTo("login") { inclusive = true }
                        }
                    },
                )
            }

            composable("home") {
                HomeScreen(role = "sin rol")
            }

            composable("home/{role}") { backStackEntry ->
                val role = backStackEntry.arguments?.getString("role") ?: "sin rol"
                HomeScreen(role = role)
            }

            composable("cases") {
                if (IS_EXTERNAL_USER) {
                    ExternalCasesScreen(
                        onCaseClick = { caseId ->
                            navController.navigate("case/$caseId")
                        },
                    )
                } else {
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
            }

            composable("profile") {
                ProfileScreen()
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
