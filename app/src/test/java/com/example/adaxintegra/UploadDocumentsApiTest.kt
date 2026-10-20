package com.example.adaxintegra

import com.example.adaxintegra.data.mapper.toDomain
import com.example.adaxintegra.data.remote.api.UploadDocumentsApi
import com.example.adaxintegra.domain.model.RecordDocumentType
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UploadDocumentsApiTest {

    // Simulamos la respuesta del backend sin salir a la red.
    private fun apiReturning(
        body: String,
        onRequest: (Request) -> Unit,
    ): UploadDocumentsApi {
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request()
                onRequest(request)

                Response.Builder()
                    .request(request)
                    .protocol(Protocol.HTTP_1_1)
                    .code(200)
                    .message("OK")
                    .body(body.toResponseBody())
                    .build()
            }
            .build()

        return Retrofit.Builder()
            .baseUrl("https://example.invalid/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UploadDocumentsApi::class.java)
    }

    private fun imagePart(
        name: String,
        fileName: String,
        mimeType: String,
    ): MultipartBody.Part = MultipartBody.Part.createFormData(
        name,
        fileName,
        "foto-ficticia".toByteArray().toRequestBody(mimeType.toMediaType()),
    )

    @Test
    fun uploadDocuments_sendsCorrectData() {
        var requestPath = ""
        var requestMethod = ""
        var authorization = ""
        var documentBody: MultipartBody? = null

        val api = apiReturning("{}") { request ->
            requestPath = request.url.encodedPath
            requestMethod = request.method
            authorization = request.header("Authorization") ?: ""
            documentBody = request.body as? MultipartBody
        }

        val response = runBlocking {
            api.uploadDocuments(
                userId = "usuario-prueba",
                authorization = "Bearer token-ficticio",
                identityDocument = imagePart("identity_document", "identidad_prueba.jpg", "image/jpeg"),
                proofOfAddress = imagePart("proof_of_address", "domicilio_prueba.png", "image/png"),
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

        assertEquals("image/jpeg", parts[0].body.contentType().toString())
        assertEquals("image/png", parts[1].body.contentType().toString())

        response.body()?.close()
    }

    @Test
    fun uploadDocuments_sendsOnlyThePickedDocument() {
        var documentBody: MultipartBody? = null

        val api = apiReturning("{}") { request ->
            documentBody = request.body as? MultipartBody
        }

        val response = runBlocking {
            api.uploadDocuments(
                userId = "usuario-prueba",
                authorization = "Bearer token-ficticio",
                identityDocument = null,
                proofOfAddress = imagePart("proof_of_address", "comprobante-domicilio.jpg", "image/jpeg"),
            )
        }

        assertTrue(response.isSuccessful)

        // R-06: one document per request, the other one stays the same in the backend
        val parts = requireNotNull(documentBody).parts
        assertEquals(1, parts.size)
        assertTrue(
            parts[0].headers?.get("Content-Disposition")
                ?.contains("name=\"proof_of_address\"") == true,
        )

        response.body()?.close()
    }

    @Test
    fun getDocuments_sendsTokenAndParsesDocuments() {
        var requestPath = ""
        var requestMethod = ""
        var authorization = ""

        val api = apiReturning(
            """
            {"success":true,"data":{"user_id":"usuario-prueba",
             "identity_document":{"url":"https://signed/ine","file_type":"image"},
             "proof_of_address":null}}
            """.trimIndent(),
        ) { request ->
            requestPath = request.url.encodedPath
            requestMethod = request.method
            authorization = request.header("Authorization") ?: ""
        }

        val response = runBlocking {
            api.getDocuments(
                userId = "usuario-prueba",
                authorization = "Bearer token-ficticio",
            )
        }

        assertTrue(response.isSuccessful)
        assertEquals("GET", requestMethod)
        assertEquals(
            "/api/external-users/usuario-prueba/documents",
            requestPath,
        )
        assertEquals("Bearer token-ficticio", authorization)

        val documents = requireNotNull(response.body()?.data).toDomain()
        assertEquals("https://signed/ine", documents.identityDocument?.url)
        assertEquals(RecordDocumentType.IMAGE, documents.identityDocument?.fileType)
        assertNull(documents.proofOfAddress)
    }

    @Test
    fun downloadDocument_usesSignedUrlWithoutAuthorization() {
        var requestUrl = ""
        var authorization: String? = "no-revisado"

        val api = apiReturning("bytes-ficticios") { request ->
            requestUrl = request.url.toString()
            authorization = request.header("Authorization")
        }

        val response = runBlocking {
            api.downloadDocument("https://storage.example.invalid/ine.jpg?token=firmado")
        }

        assertTrue(response.isSuccessful)
        assertEquals("https://storage.example.invalid/ine.jpg?token=firmado", requestUrl)
        // The signed URL carries its own token, the app token must not travel to Storage
        assertNull(authorization)

        response.body()?.close()
    }
}
