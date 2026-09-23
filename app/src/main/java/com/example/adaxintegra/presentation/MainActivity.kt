package com.example.adaxintegra.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.adaxintegra.presentation.viewmodel.RegisterExpedientViewModel
import com.example.adaxintegra.presentation.views.screens.RegisterExpedientScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint // Requerido por Hilt para inyectar dependencias en Activity
import androidx.activity.enableEdgeToEdge
import com.example.adaxintegra.presentation.navigation.AppNavigation
import com.example.adaxintegra.ui.theme.AdaxIntegraTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // Instanciamos el ViewModel administrado por Hilt
    private val viewModel: RegisterExpedientViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RegisterExpedientScreen(
                viewModel = viewModel,
                onCancel = {
                    Toast.makeText(this, "Registro cancelado", Toast.LENGTH_SHORT).show()
                },
                onSuccess = {
                    Toast.makeText(this, "¡Expediente registrado exitosamente!", Toast.LENGTH_LONG).show()
                }
            )
        }
    }
}
            AdaxIntegraTheme {
                AppNavigation()
            }
        }
    }
}
