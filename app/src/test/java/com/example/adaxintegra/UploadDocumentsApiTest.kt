
package com.example.adaxintegra

import com.example.adaxintegra.data.remote.api.UploadDocumentsApi
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Retrofit

class UploadDocumentsApiTest {

    @Test
    fun uploadDocuments_sendsCorrectData() {
        var requestPath = ""
        var requestMethod = ""
        var authorization = ""
        var documentBody: MultipartBody? = null

        // Simulamos la respuesta del backend.
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request()

                requestPath = request.url.encodedPath
                requestMethod = request.method
                authorization = request.header("Authorization") ?: ""
                documentBody = request.body as? MultipartBody

                Response.Builder()
                    .request(request)
                    .protocol(Protocol.HTTP_1_1)
                    .code(200)
                    .message("OK")
                    .body("{}".toResponseBody())
                    .build()
            }
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://example.invalid/")
            .client(client)
            .build()

        val api = retrofit.create(UploadDocumentsApi::class.java)

        val identity = MultipartBody.Part.createFormData(
            "identity_document",
            "identidad_prueba.jpg",
            "foto-ficticia".toByteArray()
                .toRequestBody("image/jpeg".toMediaType()),
        )

        val address = MultipartBody.Part.createFormData(
            "proof_of_address",
            "domicilio_prueba.pdf",
            "pdf-ficticio".toByteArray()
                .toRequestBody("application/pdf".toMediaType()),
        )

        val response = runBlocking {
            api.uploadDocuments(
                userId = "usuario-prueba",
                authorization = "Bearer token-ficticio",
                identityDocument = identity,
                proofOfAddress = address,
            )
        }

        assertTrue(response.isSuccessful)
        assertEquals("PATCH", requestMethod)
        assertEquals(
            "/api/external-users/usuario-prueba/documents",
            requestPath,
        )
        assertEquals("Bearer token-ficticio", authorization)

        val parts = requireNotNull(documentBody).parts
        assertEquals(2, parts.size)

        assertTrue(
            parts[0].headers?.get("Content-Disposition")
                ?.contains("name=\"identity_document\"") == true,
        )

        assertTrue(
            parts[1].headers?.get("Content-Disposition")
                ?.contains("name=\"proof_of_address\"") == true,
        )

        assertEquals(
            "image/jpeg",
            parts[0].body.contentType().toString(),
        )

        assertEquals(
            "application/pdf",
            parts[1].body.contentType().toString(),
        )

        response.body()?.close()
    }
}
