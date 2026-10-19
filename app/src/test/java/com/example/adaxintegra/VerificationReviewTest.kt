package com.example.adaxintegra

import com.example.adaxintegra.data.remote.dto.ReviewVerificationRequestDto
import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.model.ExternalUserPage
import com.example.adaxintegra.domain.model.ExternalUserProfile
import com.example.adaxintegra.domain.model.ExternalUserProfileUpdate
import com.example.adaxintegra.domain.repository.ExternalUserRepository
import com.example.adaxintegra.domain.usecases.ReviewVerificationUseCase
import com.example.adaxintegra.presentation.views.screens.admin.VerificationReviewUiState
import com.example.adaxintegra.presentation.views.screens.admin.reviewErrorMessage
import com.google.gson.Gson
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class VerificationReviewTest {

    // Repositorio de prueba: guarda lo que se mandó y responde el estado indicado.
    private class FakeExternalUserRepository(
        private val savedStatus: String? = null,
        private val error: Exception? = null,
    ) : ExternalUserRepository {
        var sent: Triple<String, String, String?>? = null

        override suspend fun reviewVerification(userId: String, status: String, reason: String?): String {
            sent = Triple(userId, status, reason)
            error?.let { throw it }
            return savedStatus ?: status
        }

        override suspend fun getExternalUsers(page: Int, search: String, status: String): ExternalUserPage = throw UnsupportedOperationException()

        override suspend fun getExternalProfile(userId: String): ExternalUserProfile = throw UnsupportedOperationException()

        override suspend fun updateExternalProfile(
            userId: String,
            update: ExternalUserProfileUpdate,
        ): ExternalUserProfile = throw UnsupportedOperationException()
    }

    @Test
    fun request_withoutReason_onlySendsTheStatus() {
        val json = Gson().toJson(ReviewVerificationRequestDto(status = "VERIFICADO"))

        assertEquals("""{"status":"VERIFICADO"}""", json)
    }

    @Test
    fun request_withReason_sendsBothFields() {
        val json = Gson().toJson(
            ReviewVerificationRequestDto(status = "NO_VERIFICADO", reason = "La INE no se lee"),
        )

        assertEquals("""{"status":"NO_VERIFICADO","reason":"La INE no se lee"}""", json)
    }

    @Test
    fun useCase_emitsTheStatusSavedByTheBackend() = runBlocking {
        val repository = FakeExternalUserRepository(savedStatus = "VERIFICADO")

        val results = ReviewVerificationUseCase(repository)("user-1", "VERIFICADO", null).toList()

        assertEquals(listOf(Result.Loading, Result.Success("VERIFICADO")), results)
        assertEquals(Triple("user-1", "VERIFICADO", null), repository.sent)
    }

    @Test
    fun useCase_emitsTheErrorOfTheRepository() = runBlocking {
        val error = IllegalStateException("Empty response from server")
        val repository = FakeExternalUserRepository(error = error)

        val results = ReviewVerificationUseCase(repository)("user-1", "NO_VERIFICADO", "Motivo").toList()

        assertEquals(listOf(Result.Loading, Result.Error(error)), results)
        assertEquals(Triple("user-1", "NO_VERIFICADO", "Motivo"), repository.sent)
    }

    @Test
    fun reason_isTrimmed_andAnEmptyOneIsNotSent() {
        assertEquals("Foto borrosa", VerificationReviewUiState(rejectReason = "  Foto borrosa \n").reasonToSend)
        assertNull(VerificationReviewUiState(rejectReason = "   ").reasonToSend)
        assertNull(VerificationReviewUiState().reasonToSend)
    }

    @Test
    fun errorMessages_matchTheBackendAnswers() {
        assertEquals("Esta cuenta ya no está en revisión.", reviewErrorMessage(409))
        assertEquals("No tienes permiso para revisar esta cuenta.", reviewErrorMessage(403))
        assertEquals("La cuenta de la externa no está disponible.", reviewErrorMessage(404))
        assertTrue(reviewErrorMessage(null).contains("Revisa tu conexión"))
        assertTrue(reviewErrorMessage(500).contains("Intenta de nuevo más tarde"))
    }
}
