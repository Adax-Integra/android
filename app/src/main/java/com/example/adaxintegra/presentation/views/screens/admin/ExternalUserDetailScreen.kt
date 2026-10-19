package com.example.adaxintegra.presentation.views.screens.admin

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.adaxintegra.domain.model.ExternalUserProfile
import com.example.adaxintegra.domain.model.toForm
import com.example.adaxintegra.presentation.viewmodel.ExternalUserDetailViewModel
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppButton
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.ButtonVariant
import com.example.adaxintegra.presentation.views.designsystem.atoms.SectionTitle
import com.example.adaxintegra.presentation.views.designsystem.atoms.Spacing
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.presentation.views.designsystem.atoms.VerificationStatus
import com.example.adaxintegra.presentation.views.designsystem.molecules.ConsentCheckboxRow
import com.example.adaxintegra.presentation.views.designsystem.molecules.FormActions
import com.example.adaxintegra.presentation.views.designsystem.molecules.InfoBanner
import com.example.adaxintegra.presentation.views.designsystem.molecules.LabeledTextArea
import com.example.adaxintegra.presentation.views.designsystem.molecules.LabeledTextField
import com.example.adaxintegra.presentation.views.designsystem.molecules.PhoneField
import com.example.adaxintegra.presentation.views.designsystem.molecules.ProfileInfoRow
import com.example.adaxintegra.presentation.views.designsystem.molecules.SearchableDropdownField
import com.example.adaxintegra.presentation.views.designsystem.molecules.TopToast
import com.example.adaxintegra.presentation.views.designsystem.organisms.ChangesSummarySheet
import com.example.adaxintegra.presentation.views.designsystem.organisms.ExternalProfileHeaderCard
import com.example.adaxintegra.presentation.views.designsystem.organisms.ProfileSectionCard
import com.example.adaxintegra.presentation.views.designsystem.templates.ScreenTemplate
import com.example.adaxintegra.ui.theme.AdaxIntegraTheme
import com.example.adaxintegra.ui.theme.Purple

// G-07: the admin reviews the data of an external user and edits the email,
// the phone and the address.
// B-02: the internal staff only sees the data and opens the verification review
@Suppress("ktlint:standard:function-naming")
@Composable
fun ExternalUserDetailScreen(
    userId: String,
    onBack: () -> Unit,
    canEdit: Boolean = true,
    decidedStatus: String? = null,
    onReviewClick: () -> Unit = {},
    viewModel: ExternalUserDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(userId) {
        viewModel.loadProfile(userId)
    }

    // Coming back from the review, the badge shows the status that was saved
    LaunchedEffect(decidedStatus) {
        decidedStatus?.let(viewModel::onVerificationDecided)
    }

    // The system back closes the form first, same as the arrow of the header
    BackHandler(enabled = uiState.isEditing && !uiState.isSaving) {
        viewModel.cancelEditing()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        ScreenTemplate(
            title = if (uiState.isEditing) "Editar datos" else "Datos de la externa",
            subtitle = if (uiState.isEditing) "${uiState.displayName} - Externa" else "Gestión de usuarios",
            onBack = {
                if (uiState.isEditing) viewModel.cancelEditing() else onBack()
            },
            isLoading = uiState.profile == null && uiState.loadError == null,
            modifier = Modifier.padding(horizontal = Spacing().medium),
        ) {
            when {
                uiState.profile == null -> LoadErrorContent(
                    message = uiState.loadError.orEmpty(),
                    onRetry = { viewModel.loadProfile(userId) },
                )

                uiState.isEditing -> EditProfileContent(
                    uiState = uiState,
                    viewModel = viewModel,
                )

                else -> ProfileDetailContent(
                    uiState = uiState,
                    canEdit = canEdit,
                    onReviewClick = onReviewClick,
                    onEditClick = viewModel::startEditing,
                )
            }
        }

        // The backdrop absorbs the taps so nothing can be changed while saving
        if (uiState.isSaving) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.15f))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {},
                    ),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(color = Purple)
            }
        }

        TopToast(
            message = "Datos actualizados",
            isVisible = uiState.isShowingSuccessToast,
            onDismiss = viewModel::onToastShown,
            modifier = Modifier.align(Alignment.TopCenter),
        )
    }

    if (uiState.isShowingConfirmation) {
        ChangesSummarySheet(
            externalName = uiState.displayName,
            changes = uiState.changes,
            reason = uiState.trimmedReason,
            noticeText = uiState.noticeText,
            onConfirm = viewModel::confirm,
            onDismiss = viewModel::dismissConfirmation,
        )
    }

    uiState.errorMessage?.let { message ->
        AlertDialog(
            onDismissRequest = viewModel::onErrorDismissed,
            title = {
                Text(
                    text = "Aviso",
                    style = AppTextStyle.TitleMedium,
                    fontWeight = FontWeight.Bold,
                )
            },
            text = { Text(text = message) },
            confirmButton = {
                TextButton(onClick = viewModel::onErrorDismissed) {
                    Text(
                        text = "Aceptar",
                        color = Purple,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            },
        )
    }
}

// G-07: header card, the three sections and the button to edit
@Suppress("ktlint:standard:function-naming")
@Composable
private fun ProfileDetailContent(
    uiState: ExternalUserDetailUiState,
    canEdit: Boolean,
    onReviewClick: () -> Unit,
    onEditClick: () -> Unit,
) {
    val spacing = Spacing()
    val saved = uiState.saved
    val status = VerificationStatus.from(uiState.profile?.verificationStatus)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(vertical = spacing.small),
        verticalArrangement = Arrangement.spacedBy(spacing.medium),
    ) {
        ExternalProfileHeaderCard(
            initials = uiState.initials,
            name = uiState.displayName,
            email = displayValue(saved.email),
            status = status,
        )

        RegistrationSection(uiState = uiState)

        ProfileSectionCard(title = "Contacto") {
            ProfileInfoRow(label = "Correo electrónico", value = displayValue(saved.email))
            ProfileInfoRow(label = "Teléfono celular", value = displayValue(phoneText(saved)))
        }

        // In the same order as the "Registrar externa" form
        ProfileSectionCard(title = "Domicilio") {
            ProfileInfoRow(label = "País", value = displayValue(saved.country))
            ProfileInfoRow(label = "Estado", value = displayValue(saved.state))
            ProfileInfoRow(label = "Municipio/Localidad", value = displayValue(saved.municipality))
        }

        // B-02: only an account under review can be approved or rejected
        if (status == VerificationStatus.IN_REVIEW) {
            AppButton(
                text = "Revisar verificación",
                onClick = onReviewClick,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        // Only the admin can edit, the backend answers 403 to the internal staff
        if (canEdit) {
            AppButton(
                text = "Editar datos",
                onClick = onEditClick,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

// Name, last name and birth date can not be edited, so both pages only show them
@Suppress("ktlint:standard:function-naming")
@Composable
private fun RegistrationSection(uiState: ExternalUserDetailUiState) {
    ProfileSectionCard(title = "Datos de registro", isLocked = true) {
        ProfileInfoRow(label = "Nombre(s)", value = displayValue(uiState.profile?.name))
        ProfileInfoRow(label = "Apellidos", value = displayValue(uiState.profile?.lastName))
        ProfileInfoRow(label = "Fecha de nacimiento", value = uiState.birthDateText)
    }
}

// G-07: form with the contact, the address and the reason of the change
@Suppress("ktlint:standard:function-naming")
@Composable
private fun EditProfileContent(
    uiState: ExternalUserDetailUiState,
    viewModel: ExternalUserDetailViewModel,
) {
    val spacing = Spacing()
    val form = uiState.form

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(vertical = spacing.small),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        InfoBanner(
            text = "Cada cambio queda registrado en la bitácora y se le avisa a la externa por correo electrónico.",
        )

        RegistrationSection(uiState = uiState)

        SectionTitle(text = "Contacto")
        LabeledTextField(
            label = "Correo electrónico",
            value = form.email,
            onValueChange = viewModel::onEmailChange,
            placeholder = "correo@ejemplo.com",
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrectEnabled = false,
                keyboardType = KeyboardType.Email,
            ),
            errorMessage = uiState.emailError,
        )
        PhoneField(
            countryCode = form.countryCode,
            phoneValue = form.phone,
            onPhoneChange = viewModel::onPhoneChange,
            onCountryCodeChange = viewModel::onCountryCodeChange,
            placeholder = "10 dígitos",
            errorMessage = uiState.phoneError,
        )

        // Same fields and order as the "Registrar externa" form
        SectionTitle(text = "Domicilio")
        SearchableDropdownField(
            label = "País",
            prompt = "Selecciona un país",
            options = uiState.countryOptions,
            selectedOption = form.country,
            onOptionSelected = viewModel::selectCountry,
        )
        SearchableDropdownField(
            label = "Estado",
            prompt = if (form.country == null) "Primero elige un país" else "Selecciona un estado",
            options = uiState.stateOptions,
            selectedOption = form.state,
            onOptionSelected = viewModel::selectState,
            enabled = form.country != null,
            errorMessage = uiState.stateError,
        )
        LabeledTextField(
            label = "Municipio/Localidad",
            value = form.municipality,
            onValueChange = viewModel::onMunicipalityChange,
            placeholder = "Municipio o localidad...",
            enabled = form.state != null,
            errorMessage = uiState.municipalityError,
        )

        SectionTitle(text = "Registro del cambio")
        LabeledTextArea(
            label = "Motivo del cambio",
            value = uiState.reason,
            onValueChange = viewModel::onReasonChange,
            maxLength = MAX_REASON_LENGTH,
            placeholder = "Describe por qué se modificaron los datos...",
            error = uiState.reasonError,
        )
        ConsentCheckboxRow(
            checked = uiState.hasConsent,
            onCheckedChange = viewModel::onConsentChange,
            title = "La externa autorizó este cambio",
            description = "Confirmo que la externa dio su consentimiento para modificar sus datos.",
            errorMessage = uiState.consentError,
        )

        // Always enabled, same as the new case form: the tap shows the invalid fields
        FormActions(
            onPrimary = viewModel::onSaveTapped,
            onSecondary = viewModel::cancelEditing,
            isPrimaryEnabled = !uiState.isSaving,
            modifier = Modifier.padding(top = 12.dp),
        )
    }
}

// Shown when the first load fails, the detail can not be drawn without the profile
@Suppress("ktlint:standard:function-naming")
@Composable
private fun LoadErrorContent(
    message: String,
    onRetry: () -> Unit,
) {
    val spacing = Spacing()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(spacing.medium, Alignment.CenterVertically),
    ) {
        Text(text = message, textAlign = TextAlign.Center)
        AppButton(
            text = "Reintentar",
            onClick = onRetry,
            variant = ButtonVariant.Outlined,
        )
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
private fun ProfileDetailContentPreview() {
    val profile = ExternalUserProfile(
        userId = "00000000-0000-4000-8000-000000000000",
        verificationStatus = "EN_REVISION",
        name = "Sandra",
        lastName = "Pérez Ruiz",
        email = "sp.ruiz@ejemplo.com",
        birthDate = "1990-04-12",
        phone = "+524421234567",
        country = "México",
        state = "Querétaro",
        municipality = "Querétaro",
    )

    AdaxIntegraTheme {
        ProfileDetailContent(
            uiState = ExternalUserDetailUiState(profile = profile, saved = profile.toForm()),
            canEdit = true,
            onReviewClick = {},
            onEditClick = {},
        )
    }
}
