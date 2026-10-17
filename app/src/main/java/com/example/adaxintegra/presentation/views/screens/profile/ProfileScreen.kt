package com.example.adaxintegra.presentation.views.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
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
import com.example.adaxintegra.presentation.views.designsystem.organisms.MenuOptionCard
import com.example.adaxintegra.presentation.views.designsystem.organisms.ProfileHeaderCard
import com.example.adaxintegra.ui.theme.LightOrange
import com.example.adaxintegra.ui.theme.Orange

@Suppress("ktlint:standard:function-naming")
// profile screen, reached through bottom nav bar option
@Composable
fun ProfileScreen(
    onPersonalDataClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onBack: () -> Unit,
    onSecurityClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    isAdmin: Boolean = false,
    onManageCollaboratorsClick: () -> Unit = {},
    viewModel: ProfileViewModel = hiltViewModel(),
    onUploadDocumentsClick: () -> Unit = {},
    isExternal: Boolean = false,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val profile = uiState.profile

    var showLogoutConfirmation by rememberSaveable {
        mutableStateOf(false)
    }

    // G-08: confirmation before deactivating the account
    var showDeactivateConfirmation by rememberSaveable {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }

    // G-08: once the account is deactivated the session is closed and the app goes to login
    LaunchedEffect(uiState.isDeactivated) {
        if (uiState.isDeactivated) {
            showDeactivateConfirmation = false
            onLogoutClick()
        }
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
        } else {
            PullToRefreshBox(
                isRefreshing = uiState.isRefreshing,
                onRefresh = viewModel::refresh,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                ) {
                    when {
                        profile != null -> {
                            // header card that includes the name of profile
                            ProfileHeaderCard(
                                name = "${profile.name} ${profile.lastName}".trim().ifBlank { "Usuario" },
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            // title before cards
                            Text(
                                text = "Información Personal",
                                style = AppTextStyle.LabelMedium,
                                fontWeight = FontWeight.Bold,
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // options for further details
                            MenuOptionCard(
                                icon = AppIcons.Profile,
                                title = "Datos Personales",
                                subtitle = if (isExternal) "Nombre, teléfono, dirección" else "Nombre, teléfono",
                                onClick = onPersonalDataClick,
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            MenuOptionCard(
                                icon = AppIcons.Lock,
                                title = "Seguridad",
                                subtitle = "Cambio de contraseña",
                                onClick = onSecurityClick,
                            )

                            if (isExternal) {
                                Spacer(modifier = Modifier.height(12.dp))

                                MenuOptionCard(
                                    icon = AppIcons.IdCard,
                                    title = "Mis documentos",
                                    subtitle = "INE y comprobante de domicilio",
                                    onClick = onUploadDocumentsClick,
                                )
                            }

                            // G-08: only external and internal users can deactivate their account
                            if (!isAdmin) {
                                Spacer(modifier = Modifier.height(20.dp))

                                Text(
                                    text = "Acciones",
                                    style = AppTextStyle.LabelMedium,
                                    fontWeight = FontWeight.Bold,
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                // the background must be solid, a transparent one shows the card shadow
                                MenuOptionCard(
                                    icon = AppIcons.Delete,
                                    title = "Desactivar cuenta",
                                    subtitle = "Esta acción no se puede deshacer",
                                    onClick = { showDeactivateConfirmation = true },
                                    containerColor = LightOrange,
                                    contentColor = Orange,
                                    subtitleColor = Orange,
                                )
                            }
                        }

                        else -> {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 32.dp),
                            ) {
                                Text(
                                    text = uiState.error ?: "No se encontró la información del perfil.",
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
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        AppButton(
            text = "Cerrar sesión",
            onClick = { showLogoutConfirmation = true },
            modifier = Modifier.fillMaxWidth(),
        )

        // logout confirmation
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

        // G-08: deactivate account confirmation, it cannot be closed while the request is running
        if (showDeactivateConfirmation) {
            AlertDialog(
                onDismissRequest = {
                    if (!uiState.isDeactivating) {
                        showDeactivateConfirmation = false
                        viewModel.dismissDeactivateError()
                    }
                },
                title = {
                    Text(
                        text = "Desactivar cuenta",
                        style = AppTextStyle.TitleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                },
                text = {
                    Column {
                        Text(
                            "¿Seguro que quieres desactivar tu cuenta? " +
                                "Ya no podrás iniciar sesión y esta acción no se puede deshacer.",
                        )

                        // error from the request, the user can try again
                        uiState.deactivateError?.let { message ->
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = message,
                                style = AppTextStyle.BodySmall,
                                color = MaterialTheme.colorScheme.error,
                            )
                        }
                    }
                },
                confirmButton = {
                    AppButton(
                        text = "Desactivar",
                        onClick = { viewModel.deactivateAccount() },
                        isLoading = uiState.isDeactivating,
                    )
                },
                dismissButton = {
                    AppButton(
                        text = "Cancelar",
                        onClick = {
                            showDeactivateConfirmation = false
                            viewModel.dismissDeactivateError()
                        },
                        variant = ButtonVariant.Outlined,
                        enabled = !uiState.isDeactivating,
                    )
                },
            )
        }
    }
}
