package com.example.adaxintegra

import com.example.adaxintegra.data.mapper.toDomain
import com.example.adaxintegra.data.remote.dto.ProfileResponseDto
import com.example.adaxintegra.presentation.views.designsystem.atoms.VerificationStatus
import com.example.adaxintegra.presentation.views.designsystem.organisms.verificationBannerMessage
import com.example.adaxintegra.presentation.views.designsystem.organisms.verificationBannerTitle
import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class VerificationStatusTest {

    // Respuesta de GET /api/profile/:userId con datos inventados.
    private fun profileJson(status: String?): String {
        val statusField = status?.let { ""","verification_status":"$it"""" }.orEmpty()
        return """{"success":true,"data":{"user_id":"user-1","name":"Ana","last_name":"Prueba",""" +
            """"email":"ana@ejemplo.com","created_at":"2026-10-01T00:00:00Z"$statusField}}"""
    }

    private fun parse(json: String) = Gson().fromJson(json, ProfileResponseDto::class.java).data.toDomain()

    @Test
    fun profile_readsTheVerificationStatus() {
        val profile = parse(profileJson("EN_REVISION"))

        assertEquals("EN_REVISION", profile.verificationStatus)
        assertEquals(VerificationStatus.IN_REVIEW, VerificationStatus.from(profile.verificationStatus))
    }

    @Test
    fun profile_withoutStatus_isShownAsNotVerified() {
        val profile = parse(profileJson(null))

        assertNull(profile.verificationStatus)
        assertEquals(VerificationStatus.NOT_VERIFIED, VerificationStatus.from(profile.verificationStatus))
    }

    @Test
    fun status_mapsTheThreeBackendValues() {
        assertEquals(VerificationStatus.NOT_VERIFIED, VerificationStatus.from("NO_VERIFICADO"))
        assertEquals(VerificationStatus.IN_REVIEW, VerificationStatus.from("EN_REVISION"))
        assertEquals(VerificationStatus.VERIFIED, VerificationStatus.from("VERIFICADO"))
        // Un valor desconocido no debe tumbar la pantalla
        assertEquals(VerificationStatus.NOT_VERIFIED, VerificationStatus.from("OTRO"))
    }

    @Test
    fun banner_texts_matchIos() {
        assertEquals("Verifica tu identidad", verificationBannerTitle(VerificationStatus.NOT_VERIFIED))
        assertEquals("Documentos en revisión", verificationBannerTitle(VerificationStatus.IN_REVIEW))
        assertEquals("Cuenta verificada", verificationBannerTitle(VerificationStatus.VERIFIED))

        assertTrue(verificationBannerMessage(VerificationStatus.NOT_VERIFIED).contains("Sube tu identificación (INE)"))
        assertFalse(verificationBannerMessage(VerificationStatus.IN_REVIEW).contains("Sube"))
    }
}
