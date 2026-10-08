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
import com.example.adaxintegra.presentation.viewmodel.RecordsViewModel
import com.example.adaxintegra.presentation.viewmodel.RegisterViewModel
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.presentation.views.screens.HomeScreen
import com.example.adaxintegra.presentation.views.screens.LoginScreen
import com.example.adaxintegra.presentation.views.screens.PrivacyPolicyScreen
import com.example.adaxintegra.presentation.views.screens.ProfileScreen
import com.example.adaxintegra.presentation.views.screens.admin.AdminScreen
import com.example.adaxintegra.presentation.views.screens.RegisterScreen
import com.example.adaxintegra.presentation.views.screens.admin.ActivityLogScreen
import com.example.adaxintegra.presentation.views.screens.admin.CollaboratorsScreen
import com.example.adaxintegra.presentation.views.screens.cases.CaseDetailScreen
import com.example.adaxintegra.presentation.views.screens.cases.CaseProgressScreen
import com.example.adaxintegra.presentation.views.screens.cases.CasesScreen
import com.example.adaxintegra.presentation.views.screens.cases.CreateCaseScreen
import com.example.adaxintegra.presentation.views.screens.cases.ExternalCasesScreen
import com.example.adaxintegra.presentation.views.screens.cases.RecordFromUser
import com.example.adaxintegra.presentation.views.screens.records.RecordsMenuScreen
import com.example.adaxintegra.presentation.views.screens.records.RecordsScreen

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
    val hasValidSession =
        !session?.userId.isNullOrBlank() &&
            (isExternal || canViewAllCases)

    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    // Clears the previous navigation history when entering or leaving login
    LaunchedEffect(hasValidSession, currentRoute) {
        val destination =
            when {
                hasValidSession && currentRoute == "login" -> "home"

                !hasValidSession &&
                    currentRoute != null &&
                    currentRoute != "login" &&
                    currentRoute != "register" -> "login"

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
                currentRoute != "login" &&
                currentRoute != "register"
            ) {
                val selectedRoute =
                    when (currentRoute) {
                        // Keep the records tab selected throughout this flow.
                        "collaboratorCases",
                        "allRecords",
                        "recordFromUser/{userId}",
                            -> "records"

                        // G-03: collaborators screen is opened from the admin section (NV-02)
                        "collaborators" -> "admin"

                        // V-06: activity log is opened from the admin section
                        "activityLog" -> "admin"

                        "case/{caseId}" -> {
                            if (isExternal) "cases" else "records"
                        }

                        // V-11 belongs to the internal/admin records flow
                        "caseDetail/{caseId}" -> "records"

                        // R-02 is opened from the external user's case list
                        "createCase" -> "cases"

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
                    isAdmin = isAdmin,
                )
            }
        },
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = "login",
            modifier =
                Modifier
                    .padding(innerPadding)
                    .consumeWindowInsets(innerPadding),
        ) {
            composable("login") {
                val loginViewModel: LoginViewModel = hiltViewModel()

                // G-01 & G-02, Login and register screen
                LoginScreen(
                    viewModel = loginViewModel,
                    onRegisterClick = {
                        navController.navigate("register") {
                            launchSingleTop = true
                        }
                    },
                )
            }

            composable("register") {
                val registerViewModel: RegisterViewModel = hiltViewModel()

                RegisterScreen(
                    viewModel = registerViewModel,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onRegisterSuccess = {
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    },
                )
            }

            composable("home") {
                if (hasValidSession) {
                    HomeScreen(role = role)
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
                        onAllRecordsClick = {
                            navController.navigate("allRecords") {
                                launchSingleTop = true
                            }
                        },
                    )
                } else {
                    Text("No tienes permiso para acceder a esta pantalla.")
                }
            }

            composable("allRecords") {
                // Check access before creating the ViewModel and loading records.
                if (hasValidSession && canViewAllCases) {
                    val recordsViewModel: RecordsViewModel = hiltViewModel()
                    val recordsUiState by recordsViewModel.uiState.collectAsStateWithLifecycle()

                    RecordsScreen(
                        uiState = recordsUiState,
                        onBackClick = {
                            navController.popBackStack()
                        },
                        onSearchChange = recordsViewModel::searchRecords,
                        onApplyFilters = recordsViewModel::applyFilters,
                        onClearFilters = recordsViewModel::clearFilters,
                        onRetry = recordsViewModel::retry,
                        onNextPage = recordsViewModel::nextPage,
                        onPreviousPage = recordsViewModel::previousPage,
                        onRecordClick = { userId ->
                            // V-10 loads the cases belonging to the selected owner.
                            navController.navigate("recordFromUser/$userId") {
                                launchSingleTop = true
                            }
                        },
                    )
                } else {
                    Text("No tienes permiso para acceder a esta pantalla.")
                }
            }

            composable("recordFromUser/{userId}") { entry ->
                if (hasValidSession && canViewAllCases) {
                    val userId = entry.arguments?.getString("userId")

                    if (!userId.isNullOrBlank()) {
                        RecordFromUser(
                            userId = userId,
                            onBackClick = {
                                navController.popBackStack()
                            },
                            onCaseClick = { caseId ->
                                navController.navigate("caseDetail/$caseId") {
                                    launchSingleTop = true
                                }
                            },
                        )
                    } else {
                        Text("No se encontró el identificador del usuario")
                    }
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
                        onCreateCaseClick = {
                            navController.navigate("createCase") {
                                launchSingleTop = true
                            }
                        },
                    )
                } else {
                    Text("No tienes permiso para acceder a esta pantalla.")
                }
            }

            // R-02: the external user registers a new case
            composable("createCase") {
                if (hasValidSession && isExternal) {
                    CreateCaseScreen(
                        onBack = {
                            navController.popBackStack()
                        },
                        onCaseCreated = {
                            navController.popBackStack()
                        },
                    )
                } else {
                    Text("No tienes permiso para acceder a esta pantalla.")
                }
            }

            composable("collaboratorCases") {
                // Checks permission before creating the listing ViewModel
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
                        onBack = {
                            navController.popBackStack()
                        },
                        onPersonalDataClick = {},
                        /*onSecurityClick = {},
                        onNotificationsClick = {},*/
                        onLogoutClick = viewModel::logout,
                        viewModel = hiltViewModel(),
                    )
                }
            }

            // G-03: admin manages collaborator accounts
            composable("collaborators") {
                if (hasValidSession && isAdmin) {
                    CollaboratorsScreen(
                        onBack = {
                            navController.popBackStack()
                        },
                    )
                }
            }
            // V-11: internal/admin case detail screen
            composable("caseDetail/{caseId}") { entry ->
                if (hasValidSession && canViewAllCases) {
                    val caseId = entry.arguments?.getString("caseId")

                    if (!caseId.isNullOrBlank()) {
                        CaseDetailScreen(
                            caseId = caseId,
                            onBack = {
                                navController.popBackStack()
                            },
                        )
                    } else {
                        Text("No se encontró el identificador del caso")
                    }
                } else {
                    Text("No tienes permiso para acceder a esta pantalla.")
                }
            }

            // Existing external-user case progress flow
            composable("privacyPolicy") {
                PrivacyPolicyScreen(
                    onBack = {
                        navController.popBackStack()
                    },
                    onContinue = {
                        navController.popBackStack()
                    },
                )
            }

            composable("case/{caseId}") { entry ->
                if (hasValidSession) {
                    val caseId = entry.arguments?.getString("caseId")

                    if (!caseId.isNullOrBlank()) {
                        // The backend must also verify access to this case.
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

            composable("admin") {
                if (hasValidSession && isAdmin) {
                    AdminScreen(
                        onManageUsersClick = {
                            navController.navigate("collaborators") {
                                launchSingleTop = true
                            }
                        },
                        onManageExpedientsClick = {
                            navController.navigate("records") {
                                launchSingleTop = true
                            }
                        },
                        // V-06: opens the activity log
                        onAuditLogClick = {
                            navController.navigate("activityLog") {
                                launchSingleTop = true
                            }
                        },
                    )
                } else {
                    Text("No tienes permiso para acceder a esta pantalla.")
                }
            }

            // V-06: admin consults the activity log
            composable("activityLog") {
                if (hasValidSession && isAdmin) {
                    ActivityLogScreen(
                        onBack = {
                            navController.popBackStack()
                        },
                    )
                } else {
                    Text("No tienes permiso para acceder a esta pantalla.")
                }
            }
        }
    }
}
