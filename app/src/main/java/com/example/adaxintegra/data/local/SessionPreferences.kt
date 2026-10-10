package com.example.adaxintegra.data.local

import android.content.Context
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
) {
    private val preferences = context.getSharedPreferences(
        "session_preferences",
        Context.MODE_PRIVATE,
    )

    private val gson = Gson()

    // Saves the session on disk without blocking the main thread.
    suspend fun save(session: UserSession) = withContext(Dispatchers.IO) {
        val json = gson.toJson(session)

        val saved = preferences.edit()
            .putString(SESSION_KEY, json)
            .commit()

        check(saved) { "Could not save the session." }
    }

    // Returns the stored session, or null when no usable data exists.
    suspend fun load(): UserSession? = withContext(Dispatchers.IO) {
        val json = preferences.getString(SESSION_KEY, null)
            ?: return@withContext null

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

        UserSession(
            token = token,
            userId = userId,
            role = role,
        )
    }

    // Removes the session from disk so it cannot be restored.
    suspend fun clear() = withContext(Dispatchers.IO) {
        val removed = preferences.edit()
            .remove(SESSION_KEY)
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
    }
}
