package com.example.adaxintegra.data.local

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionCipher @Inject constructor() {
    // Creates the key only when it does not already exist.
    @Synchronized
    private fun getOrCreateKey(): SecretKey {
        val keyStore = KeyStore.getInstance(KEYSTORE).apply {
            load(null)
        }

        val existingKey = keyStore.getKey(KEY_ALIAS, null) as? SecretKey
        if (existingKey != null) {
            return existingKey
        }

        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES,
            KEYSTORE,
        )

        val configuration = KeyGenParameterSpec.Builder(
            KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT,
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(256)
            .build()

        keyGenerator.init(configuration)
        return keyGenerator.generateKey()
    }

    // Generates a fresh IV and stores it alongside the encrypted data.
    fun encrypt(plainText: String): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, getOrCreateKey())

        val encrypted = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))
        val iv = Base64.encodeToString(cipher.iv, Base64.NO_WRAP)
        val content = Base64.encodeToString(encrypted, Base64.NO_WRAP)

        return "$iv:$content"
    }

    // Decrypts the data and verifies its integrity.
    fun decrypt(encryptedText: String): String {
        val parts = encryptedText.split(':')
        require(parts.size == 2) { "Invalid encrypted session format." }

        val iv = Base64.decode(parts[0], Base64.NO_WRAP)
        val content = Base64.decode(parts[1], Base64.NO_WRAP)
        require(iv.size == 12) { "Invalid session IV." }

        val keyStore = KeyStore.getInstance(KEYSTORE).apply {
            load(null)
        }

        val key = keyStore.getKey(KEY_ALIAS, null) as? SecretKey
            ?: throw IllegalStateException("Session key is unavailable.")

        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(
            Cipher.DECRYPT_MODE,
            key,
            GCMParameterSpec(128, iv),
        )

        return String(cipher.doFinal(content), Charsets.UTF_8)
    }

    private companion object {
        const val KEYSTORE = "AndroidKeyStore"
        const val KEY_ALIAS = "adax_session_key"
        const val TRANSFORMATION = "AES/GCM/NoPadding"
    }
}
