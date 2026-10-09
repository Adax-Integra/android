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
import com.example.adaxintegra.presentation.viewmodel.PrivacyPolicyViewModel
import com.example.adaxintegra.presentation.viewmodel.CasesViewModel
import com.example.adaxintegra.presentation.viewmodel.LoginViewModel
import com.example.adaxintegra.presentation.viewmodel.PendingVerificationViewModel
import com.example.adaxintegra.presentation.viewmodel.RecordsViewModel
import com.example.adaxintegra.presentation.viewmodel.RegisterExpedientViewModel
import com.example.adaxintegra.presentation.viewmodel.RegisterViewModel
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.presentation.views.screens.ForgotPasswordScreen
import com.example.adaxintegra.presentation.views.screens.HomeScreen
import com.example.adaxintegra.presentation.views.screens.LoginScreen
import com.example.adaxintegra.presentation.views.screens.PendingVerificationScreen
import com.example.adaxintegra.presentation.views.screens.PrivacyPolicyScreen
import com.example.adaxintegra.presentation.views.screens.ProfileScreen
import com.example.adaxintegra.presentation.views.screens.profile.ChangePasswordScreen
import com.example.adaxintegra.presentation.views.screens.RegisterScreen
import com.example.adaxintegra.presentation.views.screens.ResetPasswordScreen
import com.example.adaxintegra.presentation.views.screens.VerificationSuccessScreen
import com.example.adaxintegra.presentation.views.screens.RegisterExpedientScreen
import com.example.adaxintegra.presentation.views.screens.admin.AdminScreen
import com.example.adaxintegra.presentation.views.screens.admin.ActivityLogScreen
import com.example.adaxintegra.presentation.views.screens.admin.CollaboratorsScreen
import com.example.adaxintegra.presentation.views.screens.cases.CaseDetailScreen
import com.example.adaxintegra.presentation.views.screens.cases.CaseProgressScreen
import com.example.adaxintegra.presentation.views.screens.cases.CasesScreen
import com.example.adaxintegra.presentation.views.screens.cases.CreateCaseScreen
import com.example.adaxintegra.presentation.views.screens.cases.ExternalCasesScreen
import com.example.adaxintegra.presentation.views.screens.cases.RecordFromUser
import com.example.adaxintegra.presentation.views.screens.home.ExternalHomeScreen
import com.example.adaxintegra.presentation.views.screens.records.RecordsMenuScreen
import android.net.Uri
import com.example.adaxintegra.presentation.views.screens.records.RecordsScreen
import java.net.URLDecoder
import java.net.URLEncoder
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.TextButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.res.painterResource
import com.example.adaxintegra.R

// general navigation routes, provides screens
@Suppress("ktlint:standard:function-naming")
@Composable
fun AppNavigation(
    deepLinkUri: Uri? = null,
    viewModel: AppViewModel = hiltViewModel(),
) {
    val isRestoringSession by viewModel.isRestoringSession.collectAsStateWithLifecycle()

    val restoreError by viewModel.restoreError.collectAsStateWithLifecycle()

    val logoutError by viewModel.logoutError.collectAsStateWithLifecycle()

// Avoid opening login before the stored session has been read.
    if (isRestoringSession) {
        SessionLoadingScreen()
        return
    }

    if (restoreError != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = restoreError.orEmpty())

            TextButton(onClick = viewModel::retryRestoreSession) {
                Text(text = "Reintentar")
            }
        }
        return
    }

    if (logoutError != null) {
        AlertDialog(
            onDismissRequest = viewModel::dismissLogoutError,
            title = {
                Text(text = "No se pudo cerrar sesión")
            },
            text = {
                Text(text = logoutError.orEmpty())
            },
            confirmButton = {
                TextButton(onClick = viewModel::logout) {
                    Text(text = "Reintentar")
                }
            },
            dismissButton = {
                TextButton(onClick = viewModel::dismissLogoutError) {
                    Text(text = "Cancelar")
                }
            },
        )
    }

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

    //handle Supabase password recovery deep link
    LaunchedEffect(deepLinkUri) {
        if (
            deepLinkUri?.scheme == "adax" &&
            deepLinkUri.host == "auth" &&
            deepLinkUri.path == "/callback"
        ) {
            navController.navigate("resetPassword") {
                launchSingleTop = true
            }
        }
    }

    val needsPrivacyConsent by viewModel.needsPrivacyConsent.collectAsStateWithLifecycle()

    LaunchedEffect(hasValidSession, isExternal) {
        if (hasValidSession && isExternal) {
            viewModel.checkPrivacyConsent()
        }
    }

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    // Clears the previous navigation history when entering or leaving login
    LaunchedEffect(hasValidSession, currentRoute, needsPrivacyConsent) {
        val destination =
            when {
                hasValidSession && needsPrivacyConsent && currentRoute != "privacyPolicy" -> "privacyPolicy"

                hasValidSession && !needsPrivacyConsent && (currentRoute == "login" || currentRoute == "session") -> "home"

                !hasValidSession && currentRoute == "session" -> "login"

                !hasValidSession &&
                    currentRoute != null &&
                    currentRoute != "login" &&
                    currentRoute != "register" &&
                    currentRoute != "forgotPassword" &&
                    currentRoute != "resetPassword" &&
                    currentRoute != "verificationSuccess" &&
                    !currentRoute.startsWith("pendingVerification") -> "login"

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
                currentRoute != "session" &&
                currentRoute != "login" &&
                currentRoute != "register" &&
                currentRoute != "verificationSuccess" &&
                !currentRoute.startsWith("pendingVerification")
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
            startDestination = "session",
            modifier =
                Modifier
                    .padding(innerPadding)
                    .consumeWindowInsets(innerPadding),
        ) {
            composable("session") {
                SessionLoadingScreen()
            }

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
                    onVerifyUnconfirmedAccount = { email ->
                        val encodedEmail = URLEncoder.encode(email, "UTF-8")
                        navController.navigate("pendingVerification/$encodedEmail") {
                            launchSingleTop = true
                        }
                    },
                    onForgotPasswordClick = {
                        navController.navigate("forgotPassword") {
                            launchSingleTop = true
                        }
                    },
                )
            }

            //forgot password screen
            composable("forgotPassword") {

                ForgotPasswordScreen(
                    onBack = {
                        navController.popBackStack() },
                    viewModel = hiltViewModel()
                )
            }

            //reset password screen
            composable("resetPassword") { entry ->

                    ResetPasswordScreen(
                        onBack = {
                            navController.popBackStack()
                        },
                        onResetPasswordSuccess = {
                            navController.navigate("login") {
                                popUpTo("login") {
                                    inclusive = true
                                }
                            }
                        }
                    )
                }

            composable("register") {
                val registerViewModel: RegisterViewModel = hiltViewModel()

                RegisterScreen(
                    viewModel = registerViewModel,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onRegisterSuccess = { email ->
                        val encodedEmail = URLEncoder.encode(email, "UTF-8")
                        navController.navigate("pendingVerification/$encodedEmail") {
                            popUpTo("register") { inclusive = false }
                        }
                    },
                )
            }

            composable("pendingVerification/{email}") { backStackEntry ->
                val rawEmail = backStackEntry.arguments?.getString("email") ?: ""
                val email = try {
                    URLDecoder.decode(rawEmail, "UTF-8")
                } catch (_: Exception) {
                    rawEmail
                }
                val pendingVerificationViewModel: PendingVerificationViewModel = hiltViewModel()

                PendingVerificationScreen(
                    email = email,
                    viewModel = pendingVerificationViewModel,
                    onBackToLogin = {
                        navController.navigate("login") {
                            popUpTo("login") { inclusive = true }
                        }
                    },
                    onEditRegisterData = {
                        navController.popBackStack("register", inclusive = false)
                    },
                    onVerificationSuccess = {
                        navController.navigate("verificationSuccess") {
                            popUpTo("login") { inclusive = false }
                        }
                    },
                )
            }

            composable("verificationSuccess") {
                VerificationSuccessScreen(
                    onContinueClick = {
                        navController.navigate("login") {
                            popUpTo("login") { inclusive = true }
                        }
                    },
                )
            }

            composable("home") {
                if (hasValidSession) {
                    //NV-01: the external user gets her own home, the rest keep the role probe
                    if (isExternal) {
                        ExternalHomeScreen(
                            onCaseClick = { caseId ->
                                navController.navigate("case/$caseId") {
                                    launchSingleTop = true
                                }
                            },
                            onRegisterCaseClick = {
                                navController.navigate("createCase") {
                                    launchSingleTop = true
                                }
                            },
                        )
                    } else {
                        HomeScreen(role = role ?: "sin rol")
                    }
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
                        onRegisterRecordClick = {
                            navController.navigate("registerExpedient") {
                                launchSingleTop = true
                            }
                        }

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
                ProfileScreen(
                    onBack = {
                        navController.popBackStack()
                    },
                    onPersonalDataClick = {},
                    onSecurityClick = {
                        navController.navigate("changePassword") {
                            launchSingleTop = true
                        }
                    },
                    onLogoutClick = viewModel::logout,
                    isAdmin = isAdmin,
                    onManageCollaboratorsClick = {
                        navController.navigate("collaborators") {
                            launchSingleTop = true
                        }
                    },
                    viewModel = hiltViewModel(),
                )
            }

            composable("changePassword") {
                ChangePasswordScreen(
                    onBack = {
                        navController.popBackStack()
                    },
                )
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
                val privacyViewModel: PrivacyPolicyViewModel = hiltViewModel()
                PrivacyPolicyScreen(
                    viewModel = privacyViewModel,
                    onBack = {
                        viewModel.logout()
                    },
                    onContinue = {
                        privacyViewModel.acceptPolicy {
                            viewModel.setPrivacyConsentAccepted()
                            navController.navigate("home") {
                                popUpTo("privacyPolicy") { inclusive = true }
                            }
                        }
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

            composable("registerExpedient") {
                if (hasValidSession && canViewAllCases) {
                    val registerViewModel: RegisterExpedientViewModel = hiltViewModel()
                    RegisterExpedientScreen(
                        viewModel = registerViewModel,
                        userToken = session?.token ?: "", // Pasa el token de tu objeto session (o session?.accessToken según tu modelo)
                        onCancel = {
                            navController.popBackStack()
                        },
                        onSuccess = {
                            navController.popBackStack()
                        }
                    )
                } else {
                    Text("No tienes permiso para acceder a esta pantalla.")
                }
            }
        }
    }
}

@Composable
private fun SessionLoadingScreen() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(
                space = 24.dp,
                alignment = Alignment.CenterVertically,
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(R.drawable.ic_adax_logo),
                contentDescription = "ADAX",
                modifier = Modifier.size(220.dp),
            )

            CircularProgressIndicator(
                modifier = Modifier.size(32.dp),
            )
        }
    }
}
