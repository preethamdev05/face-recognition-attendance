package com.attendance.facerec.util

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.io.File
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

    /**
     * Checks if the device is secure (not rooted, not an emulator).
     * DOES NOT crash or exit - returns boolean for graceful UX handling.
     *
     * @param context Application context
     * @return true if device is secure, false if rooted or emulator detected
     */
    fun isDeviceSecure(context: Context): Boolean {
        return !isDeviceRooted() && !isEmulator()
    }

    /**
     * Detects if device is rooted by checking common root indicators.
     * Does NOT terminate app - caller handles UX flow.
     */
    private fun isDeviceRooted(): Boolean {
        // Check for common root management apps
        val rootApps = arrayOf(
            "com.noshufou.android.su",
            "com.noshufou.android.su.elite",
            "eu.chainfire.supersu",
            "com.koushikdutta.superuser",
            "com.thirdparty.superuser",
            "com.yellowes.su",
            "com.topjohnwu.magisk"
        )

        for (packageName in rootApps) {
            try {
                android.app.Application().packageManager.getPackageInfo(packageName, 0)
                return true // Root app detected
            } catch (e: Exception) {
                // Package not found, continue
            }
        }

        // Check for su binary in common paths
        val suPaths = arrayOf(
            "/system/app/Superuser.apk",
            "/sbin/su",
            "/system/bin/su",
            "/system/xbin/su",
            "/data/local/xbin/su",
            "/data/local/bin/su",
            "/system/sd/xbin/su",
            "/system/bin/failsafe/su",
            "/data/local/su",
            "/su/bin/su"
        )

        for (path in suPaths) {
            if (File(path).exists()) {
                return true // su binary found
            }
        }

        // Check if /system is writable (common in rooted devices)
        val buildTags = Build.TAGS
        if (buildTags != null && buildTags.contains("test-keys")) {
            return true // Device built with test keys
        }

        return false
    }

    /**
     * Detects if app is running on an emulator.
     * Does NOT terminate app - caller handles UX flow.
     */
    private fun isEmulator(): Boolean {
        return (Build.FINGERPRINT.startsWith("generic") ||
            Build.FINGERPRINT.startsWith("unknown") ||
            Build.MODEL.contains("google_sdk") ||
            Build.MODEL.contains("Emulator") ||
            Build.MODEL.contains("Android SDK built for x86") ||
            Build.MANUFACTURER.contains("Genymotion") ||
            Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic") ||
            "google_sdk" == Build.PRODUCT)
    }

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
