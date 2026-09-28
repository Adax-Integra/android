package com.example.adaxintegra.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.adaxintegra.presentation.viewmodel.RegisterExpedientViewModel
import com.example.adaxintegra.presentation.views.screens.RegisterExpedientScreen
import com.example.adaxintegra.ui.theme.AdaxIntegraTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val registerExpedientViewModel: RegisterExpedientViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AdaxIntegraTheme {
                RegisterExpedientScreen(
                    viewModel = registerExpedientViewModel,
                    onCancel = {
                        Toast.makeText(this, "Registro cancelado", Toast.LENGTH_SHORT).show()
                    },
                    onSuccess = {
                        Toast.makeText(this, "¡Expediente guardado exitosamente!", Toast.LENGTH_LONG).show()
                    },
                )
            }
        }
    }
}
