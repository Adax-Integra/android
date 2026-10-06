package com.example.adaxintegra.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.adaxintegra.presentation.navigation.AppNavigation
import com.example.adaxintegra.presentation.views.screens.admin.AdminScreen
import com.example.adaxintegra.ui.theme.AdaxIntegraTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AdaxIntegraTheme {
                AdminScreen(
                    onManageUsersClick = {},
                    onManageExpedientsClick = {},
                    onAuditLogClick = {},
                )
            }
        }
    }
}
