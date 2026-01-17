package com.attendance.facerec.util

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

object SecurityUtils {
    private const val ANDROID_KEYSTORE = "AndroidKeyStore"
    private const val ENCRYPTION_KEY_ALIAS = "AttendanceAppKey"
    private const val ENCRYPTION_ALGORITHM = "AES/GCM/NoPadding"
    private const val KEYSTORE_ALGORITHM = "AES"
    private const val GCM_TAG_LENGTH = 128

    fun encryptData(context: Context, plainText: String): String {
        val cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM)
        val key = getOrCreateKey()
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val iv = cipher.iv
        val encryptedData = cipher.doFinal(plainText.toByteArray())
        val combined = iv + encryptedData
        return Base64.encodeToString(combined, Base64.DEFAULT)
    }

    fun decryptData(context: Context, encryptedText: String): String {
        val cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM)
        val key = getOrCreateKey()
        val combined = Base64.decode(encryptedText, Base64.DEFAULT)
        val iv = combined.copyOfRange(0, 12)
        val encryptedData = combined.copyOfRange(12, combined.size)
        val spec = GCMParameterSpec(GCM_TAG_LENGTH, iv)
        cipher.init(Cipher.DECRYPT_MODE, key, spec)
        val decryptedData = cipher.doFinal(encryptedData)
        return String(decryptedData)
    }

    private fun getOrCreateKey(): SecretKey {
        val keyStore = java.security.KeyStore.getInstance(ANDROID_KEYSTORE)
        keyStore.load(null)

        return if (keyStore.containsAlias(ENCRYPTION_KEY_ALIAS)) {
            keyStore.getKey(ENCRYPTION_KEY_ALIAS, null) as SecretKey
        } else {
            createKey()
        }
    }

    private fun createKey(): SecretKey {
        val keyGenSpec = KeyGenParameterSpec.Builder(
            ENCRYPTION_KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setRandomizedEncryptionRequired(true)
            .build()

        val keyGenerator = KeyGenerator.getInstance(KEYSTORE_ALGORITHM, ANDROID_KEYSTORE)
        keyGenerator.init(keyGenSpec)
        return keyGenerator.generateKey()
    }

    fun saveEncryptedString(prefs: SharedPreferences, key: String, value: String) {
        val encrypted = Base64.encodeToString(value.toByteArray(), Base64.DEFAULT)
        prefs.edit().putString(key, encrypted).apply()
    }

    fun getEncryptedString(prefs: SharedPreferences, key: String): String? {
        val encrypted = prefs.getString(key, null) ?: return null
        val decrypted = Base64.decode(encrypted, Base64.DEFAULT)
        return String(decrypted)
    }
}
