package com.example.adaxintegra.data.local

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.adaxintegra.domain.model.UserSession
import com.google.gson.Gson
import com.google.gson.JsonParseException
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionPreferences @Inject constructor(
    @ApplicationContext context: Context,
    private val sessionCipher: SessionCipher,
) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val preferences = EncryptedSharedPreferences.create(
        context,
        "session_preferences_secure",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    private val gson = Gson()

    // Encrypts the session before saving it and removes the old plaintext value.
    suspend fun save(session: UserSession) = withContext(Dispatchers.IO) {
        val json = gson.toJson(session)
        val encryptedSession = sessionCipher.encrypt(json)

        val saved = preferences.edit()
            .putString(ENCRYPTED_SESSION_KEY, encryptedSession)
            .remove(SESSION_KEY)
            .commit()

        check(saved) { "Could not save the session." }
    }

    // Returns the stored session, or null when no usable data exists.
    suspend fun load(): UserSession? = withContext(Dispatchers.IO) {
        val encryptedSession = preferences.getString(ENCRYPTED_SESSION_KEY, null)

        // Only reads the old plaintext value when no encrypted session exists.
        val json = if (encryptedSession != null) {
            sessionCipher.decrypt(encryptedSession)
        } else {
            preferences.getString(SESSION_KEY, null)
                ?: return@withContext null
        }

        val stored = try {
            gson.fromJson(json, StoredSession::class.java)
        } catch (_: JsonParseException) {
            null
        }

        val token = stored?.token
        val userId = stored?.userId
        val role = stored?.role

        if (
            token.isNullOrBlank() ||
            userId.isNullOrBlank() ||
            role !in listOf("external", "internal", "admin")
        ) {
            clear()
            return@withContext null
        }

        val session = UserSession(
            token = token,
            userId = userId,
            role = role,
        )

        // Migrates an existing session before returning it to the app.
        if (encryptedSession == null) {
            save(session)
        }

        session
    }

    // Removes both encrypted and legacy session values.
    suspend fun clear() = withContext(Dispatchers.IO) {
        val removed = preferences.edit()
            .remove(SESSION_KEY)
            .remove(ENCRYPTED_SESSION_KEY)
            .commit()

        check(removed) { "Could not clear the session." }
    }

    // Nullable fields allow incomplete stored data to be detected.
    private data class StoredSession(
        val token: String? = null,
        val userId: String? = null,
        val role: String? = null,
    )

    private companion object {
        const val SESSION_KEY = "user_session"
        const val ENCRYPTED_SESSION_KEY = "encrypted_user_session"
    }
}
