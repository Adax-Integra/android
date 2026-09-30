package com.example.adaxintegra.presentation.navigation

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.adaxintegra.presentation.viewmodel.AppViewModel
import com.example.adaxintegra.presentation.viewmodel.CasesViewModel
import com.example.adaxintegra.presentation.viewmodel.LoginViewModel
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.presentation.views.screens.HomeScreen
import com.example.adaxintegra.presentation.views.screens.LoginScreen
import com.example.adaxintegra.presentation.views.screens.ProfileScreen
import com.example.adaxintegra.presentation.views.screens.cases.CaseProgressScreen
import com.example.adaxintegra.presentation.views.screens.cases.CasesScreen
import com.example.adaxintegra.presentation.views.screens.cases.ExternalCasesScreen
import com.example.adaxintegra.presentation.views.screens.records.RecordsMenuScreen

// provide values(screens) to BottomNavBar
// general navigation routes, provides screens
@Suppress("ktlint:standard:function-naming")
@Composable
fun AppNavigation(
    viewModel: AppViewModel = hiltViewModel(),
) {
    val session by viewModel.session.collectAsStateWithLifecycle()
    val role = session?.role

    // Keep each role distinct
    val isExternal = (role == "external")
    val isInternal = (role == "internal")
    val isAdmin = (role == "admin")

    val canViewAllCases = isInternal || isAdmin
    val hasValidSession = !session?.userId.isNullOrBlank() && (isExternal || canViewAllCases)

    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    // CLears the previous navigation history when entering or leaving login
    LaunchedEffect(hasValidSession, currentRoute) {
        val destination = when {
            hasValidSession && currentRoute == "login" -> "home"

            !hasValidSession &&
                currentRoute != null && currentRoute != "login" -> "login"

            else -> null
        }

        if (destination != null) {
            navController.navigate(destination) {
                popUpTo(navController.graph.id) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }
    }

    Scaffold(
        bottomBar = {
            if (
                hasValidSession &&
                currentRoute != null &&
                currentRoute != "login"
            ) {
                val selectedRoute = when (currentRoute) {
                    "collaboratorCases" -> "records"

                    "case/{caseId}" -> {
                        if (isExternal) "cases" else "records"
                    }

                    else -> currentRoute
                }

                BottomNavBar(
                    currentRoute = selectedRoute,
                    onNavigateToRoute = { route ->
                        navController.navigate(route) {
                            popUpTo("home")
                            launchSingleTop = true
                        }
                    },
                    showRecords = canViewAllCases,
                )
            }
        },
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = "login",
            modifier = Modifier
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding),
        ) {
            composable("login") {
                val loginViewModel: LoginViewModel = hiltViewModel()

                LoginScreen(viewModel = loginViewModel)
            }

            composable("home") {
                if (hasValidSession) {
                    HomeScreen(role = role.orEmpty())
                }
            }

            composable("records") {
                if (hasValidSession && canViewAllCases) {
                    RecordsMenuScreen(
                        onAllCasesClick = {
                            navController.navigate("collaboratorCases") {
                                launchSingleTop = true
                            }
                        },
                    )
                } else {
                    Text("No tienes permiso para acceder a esta pantalla.")
                }
            }

            composable("cases") {
                if (hasValidSession && isExternal) {
                    ExternalCasesScreen(
                        onCaseClick = { caseId ->
                            navController.navigate("case/$caseId") {
                                launchSingleTop = true
                            }
                        },
                    )
                } else {
                    Text("No tienes permiso para acceder a esta pantalla.")
                }
            }

            composable("collaboratorCases") {
                // Checks permision before creating the listing ViewModel
                if (hasValidSession && canViewAllCases) {
                    val casesViewModel: CasesViewModel = hiltViewModel()
                    val uiState by casesViewModel.uiState.collectAsStateWithLifecycle()

                    CasesScreen(
                        uiState = uiState,
                        onBackClick = {
                            navController.popBackStack()
                        },
                        onSearchChange = casesViewModel::searchCases,
                        onUrgencyChange = casesViewModel::filterCases,
                        onClearFilters = casesViewModel::clearFilters,
                        onRetry = casesViewModel::retry,
                        onNextPage = casesViewModel::nextPage,
                        onPreviousPage = casesViewModel::previousPage,
                    )
                } else {
                    Text("No tienes permiso para acceder a esta pantalla.")
                }
            }

            composable("profile") {
                if (hasValidSession) {
                    ProfileScreen(
                        onLogout = viewModel::logout,
                    )
                }
            }

            composable("case/{caseId}") { entry ->
                if (hasValidSession) {
                    val caseId = entry.arguments?.getString("caseId")

                    if (!caseId.isNullOrBlank()) {
                        // THe backend must also verify access to this case.
                        CaseProgressScreen(
                            caseId = caseId,
                            onBack = {
                                navController.popBackStack()
                            },
                        )
                    } else {
                        Text("No se encontró el identificador del caso")
                    }
                }
            }
        }
    }
}
