package com.example.adaxintegra.presentation.views.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.adaxintegra.presentation.viewmodel.ProfileViewModel
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppButton
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppIcons
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
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


    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }

    //must load all the info before displaying anything
    if (uiState.isLoading) {
        CircularProgressIndicator()
    } else if (uiState.error != null) {
        Text(text = uiState.error!!)
    } else {
        val profile = uiState.profile
        if (profile != null) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 32.dp
                    )
            ) {

                AppHeader(
                    title = "Perfil",
                    onBack = onBack,
                )

                //format date by cutting down string
                val formattedDate = profile.createdAt
                    .substring(0, 7)

                //header card that includes the name of profile
                ProfileHeaderCard(
                    icon = AppIcons.Account,
                    name = "${profile.name} ${profile.lastName}",
                    createdAt = "Desde $formattedDate",
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                //title before cards
                Text(
                    text = "Información Personal",
                    style = AppTextStyle.LabelMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                //three options for further details
                ProfileOption(
                    icon = AppIcons.Profile,
                    title = "Datos Personales",
                    subtitle = "Nombre, teléfono, correo",
                    onClick = onPersonalDataClick
                )

                Spacer(
                    modifier = Modifier.height(24.dp)
                )

                /*ProfileOption(
                    icon = AppIcons.Lock,
                    title = "Seguridad",
                    subtitle = "Cambio de contraseña",
                    onClick = onSecurityClick
                )

                Spacer(
                    modifier = Modifier.height(24.dp)
                )

                ProfileOption(
                    icon = AppIcons.Bell,
                    title = "Notificaciones",
                    subtitle = "Preferencia de notificaciones",
                    onClick = onNotificationsClick
                )*/

                Spacer(
                    modifier = Modifier.weight(1f)
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

                Button(
                    onClick = onLogoutClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cerrar sesión")
                }
            }
        }
    }
}
