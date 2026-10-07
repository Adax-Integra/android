package com.example.adaxintegra.presentation.views.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.adaxintegra.presentation.viewmodel.ProfileViewModel
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppButton
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppIcons
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.ButtonVariant
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.presentation.views.designsystem.organisms.AppHeader
import com.example.adaxintegra.presentation.views.designsystem.organisms.ProfileHeaderCard
import com.example.adaxintegra.presentation.views.designsystem.organisms.ProfileOption

@Suppress("ktlint:standard:function-naming")
// profile screen, reached through bottom nav bar option
@Composable
fun ProfileScreen(
    onPersonalDataClick: () -> Unit,
    /*onSecurityClick: () -> Unit,
    onNotificationsClick: () -> Unit,*/
    onLogoutClick: () -> Unit,
    onBack: () -> Unit,
    isAdmin: Boolean = false,
    onManageCollaboratorsClick: () -> Unit = {},
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var showLogoutConfirmation by rememberSaveable {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 32.dp,
            ),
    ) {
        AppHeader(
            title = "Perfil",
            onBack = onBack,
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        } else if (uiState.error != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(16.dp),
                ) {
                    Text(
                        text = uiState.error!!,
                        style = AppTextStyle.BodyMedium,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    AppButton(
                        text = "Reintentar",
                        onClick = { viewModel.loadProfile() },
                    )
                }
            }
        } else {
            val profile = uiState.profile
            if (profile != null) {
                // header card that includes the name of profile
                ProfileHeaderCard(
                    name = "${profile.name} ${profile.lastName}".trim().ifBlank { "Usuario" },
                )

                Spacer(
                    modifier = Modifier.height(20.dp),
                )

                // title before cards
                Text(
                    text = "Información Personal",
                    style = AppTextStyle.LabelMedium,
                    fontWeight = FontWeight.Bold,
                )

                Spacer(
                    modifier = Modifier.height(8.dp),
                )

                // three options for further details
                ProfileOption(
                    icon = AppIcons.Profile,
                    title = "Datos Personales",
                    subtitle = "Nombre, teléfono, correo",
                    onClick = onPersonalDataClick,
                )

                Spacer(
                    modifier = Modifier.weight(1f),
                )

                // G-03: only the admin can manage collaborator accounts
                // (temporary access until the admin section is finished)
                if (isAdmin) {
                    AppButton(
                        text = "Gestión de colaboradoras",
                        onClick = onManageCollaboratorsClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                    )
                }

                AppButton(
                    text = "Cerrar sesión",
                    onClick = { showLogoutConfirmation = true },
                    variant = ButtonVariant.Outlined,
                    modifier = Modifier.fillMaxWidth(),
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "No se encontró la información del perfil.",
                            style = AppTextStyle.BodyMedium,
                            textAlign = TextAlign.Center,
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        AppButton(
                            text = "Reintentar",
                            onClick = { viewModel.loadProfile() },
                        )
                    }
                }
            }
        }

        if (showLogoutConfirmation) {
            AlertDialog(
                onDismissRequest = { showLogoutConfirmation = false },
                title = {
                    Text(
                        text = "Cerrar sesión",
                        style = AppTextStyle.TitleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                },
                text = {
                    Text("¿Quieres cerrar tu sesión?")
                },
                confirmButton = {
                    AppButton(
                        text = "Cerrar sesión",
                        onClick = {
                            showLogoutConfirmation = false
                            onLogoutClick()
                        },
                    )
                },
                dismissButton = {
                    AppButton(
                        text = "Cancelar",
                        onClick = { showLogoutConfirmation = false },
                        variant = ButtonVariant.Outlined,
                    )
                },
            )
        }
    }
}
