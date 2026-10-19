package com.example.adaxintegra

import com.example.adaxintegra.domain.model.Country
import com.example.adaxintegra.domain.model.ExternalUserProfile
import com.example.adaxintegra.domain.model.toForm
import com.example.adaxintegra.presentation.views.screens.admin.ExternalUserDetailUiState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExternalProfileFormTest {

    // Externa de prueba, con datos ficticios.
    private val profile = ExternalUserProfile(
        userId = "00000000-0000-4000-8000-000000000000",
        verificationStatus = "VERIFICADO",
        name = "Sandra",
        lastName = "Pérez Ruiz",
        email = "sp.ruiz@ejemplo.com",
        birthDate = "1990-04-12",
        phone = "+524421234567",
        country = "México",
        state = "Querétaro",
        municipality = "Querétaro",
    )

    private val countries = listOf(
        Country(name = "México", phoneCode = "+52", states = listOf("Querétaro", "Jalisco")),
        Country(name = "Colombia", phoneCode = "+57", states = listOf("Antioquia")),
    )

    private fun editing(): ExternalUserDetailUiState {
        val saved = profile.toForm()
        return ExternalUserDetailUiState(
            profile = profile,
            saved = saved,
            form = saved,
            countries = countries,
            isEditing = true,
        )
    }

    @Test
    fun savedPhone_isSplitInCodeAndTenDigits() {
        val form = profile.toForm()

        assertEquals("+52", form.countryCode)
        assertEquals("4421234567", form.phone)
        assertEquals("+524421234567", form.fullPhone)
    }

    @Test
    fun withoutChanges_thereIsNothingToConfirm() {
        assertTrue(editing().changes.isEmpty())
    }

    @Test
    fun changes_onlyListTheEditedFields() {
        val state = editing().let {
            it.copy(form = it.form.copy(email = " Nuevo@Ejemplo.com ", phone = "4429876543"))
        }

        val changes = state.changes

        assertEquals(listOf("Correo electrónico", "Teléfono celular"), changes.map { it.label })
        assertEquals("nuevo@ejemplo.com", changes[0].newValue)
        assertEquals("+52 442 123 4567", changes[1].oldValue)
        assertEquals("+52 442 987 6543", changes[1].newValue)
    }

    @Test
    fun errors_areHiddenUntilSaveIsTapped() {
        val state = editing().let { it.copy(form = it.form.copy(email = "correo-invalido")) }

        assertNull(state.emailError)
        assertNull(state.reasonError)
        assertEquals("Ingresa un correo electrónico válido", state.copy(showErrors = true).emailError)
        assertEquals("Este campo es obligatorio", state.copy(showErrors = true).reasonError)
    }

    @Test
    fun form_needsReasonAndConsent() {
        val changed = editing().let { it.copy(form = it.form.copy(municipality = "Corregidora")) }

        assertFalse(changed.isFormValid)
        assertFalse(changed.copy(reason = "Se mudó").isFormValid)
        assertTrue(changed.copy(reason = "Se mudó", hasConsent = true).isFormValid)
    }

    @Test
    fun newCountry_requiresStateAndMunicipality() {
        val state = editing().let {
            it.copy(
                form = it.form.copy(country = "Colombia", state = null, municipality = ""),
                reason = "Se mudó",
                hasConsent = true,
                showErrors = true,
            )
        }

        assertEquals(listOf("Antioquia"), state.stateOptions)
        assertEquals("Selecciona un estado", state.stateError)
        assertFalse(state.isFormValid)

        val withState = state.copy(form = state.form.copy(state = "Antioquia"))
        assertEquals("Escribe el municipio o localidad", withState.municipalityError)
    }

    @Test
    fun takenEmail_isMarkedInTheEmailField() {
        val state = editing().let {
            it.copy(
                form = it.form.copy(email = "otra@ejemplo.com"),
                takenEmail = "otra@ejemplo.com",
                showErrors = true,
            )
        }

        assertEquals("Este correo ya está registrado en otra cuenta.", state.emailError)
    }

    @Test
    fun update_onlySendsTheChangedFields() {
        val state = editing().let {
            it.copy(
                form = it.form.copy(municipality = " Corregidora "),
                reason = "  Se mudó  ",
                hasConsent = true,
            )
        }

        val update = state.toUpdate()

        assertNull(update.email)
        assertNull(update.phone)
        assertNull(update.country)
        assertNull(update.state)
        assertEquals("Corregidora", update.municipality)
        assertEquals("Se mudó", update.reason)
        assertTrue(update.consentConfirmed)
    }

    @Test
    fun newEmail_noticeGoesToBothInboxes() {
        val state = editing().let { it.copy(form = it.form.copy(email = "nuevo@ejemplo.com")) }

        assertEquals(
            "Se enviará un aviso a sp.ruiz@ejemplo.com y a nuevo@ejemplo.com sin mostrar los datos nuevos.",
            state.noticeText,
        )
    }

    @Test
    fun detail_showsBirthDateAndInitials() {
        val state = editing()

        assertEquals("12/04/1990", state.birthDateText)
        assertEquals("SP", state.initials)
        assertEquals("Sandra Pérez Ruiz", state.displayName)
    }
}
