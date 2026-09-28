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
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.presentation.views.designsystem.organisms.BottomNavBar

//provide values(screens) to BottomNavBar
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute != "login") {
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
            }
        },
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("login") {
                val viewModel: LoginViewModel = hiltViewModel()
                LoginScreen(
                    viewModel = viewModel,
                    onNavigateToHome = { userRole ->
                        val targetRoute =
                            if (!userRole.isNullOrBlank()) "home/$userRole" else "home"
                        navController.navigate(targetRoute) {
                            popUpTo("login") { inclusive = true }
                        }
                    },
                )
            }

            composable("home") {
                Text("Home Screen")
            }

            composable("cases") {
                //CasesScreen
            }

            composable("profile") {
                //ProfileScreen
            }
        }
    }
}
