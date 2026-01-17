package com.attendance.facerec.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

fun <T> logResult(tag: String, result: com.attendance.facerec.domain.model.Result<T>): com.attendance.facerec.domain.model.Result<T> {
    when (result) {
        is com.attendance.facerec.domain.model.Result.Success -> Log.d(tag, "Success: ${result.data}")
        is com.attendance.facerec.domain.model.Result.Error -> Log.e(tag, "Error: ${result.exception.message}")
        is com.attendance.facerec.domain.model.Result.Loading -> Log.d(tag, "Loading...")
    }
    return result
}

fun String.isValidEmail(): Boolean {
    val emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$".toRegex()
    return this.matches(emailRegex)
}

fun String.isValidPhoneNumber(): Boolean {
    return this.length >= 10 && this.all { it.isDigit() || it == '+' || it == '-' || it == ' ' }
}

fun Bitmap.rotateBitmap(degrees: Float): Bitmap {
    val matrix = Matrix().apply {
        postRotate(degrees)
    }
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}

fun Bitmap.resizeBitmap(targetWidth: Int, targetHeight: Int): Bitmap {
    return Bitmap.createScaledBitmap(this, targetWidth, targetHeight, true)
}

fun Float.similarity(other: Float, threshold: Float = 0.6f): Boolean {
    return kotlin.math.abs(this - other) <= (1f - threshold)
}

fun FloatArray.cosineSimilarity(other: FloatArray): Float {
    if (this.size != other.size) return 0f

    var dotProduct = 0f
    var normA = 0f
    var normB = 0f

    for (i in indices) {
        dotProduct += this[i] * other[i]
        normA += this[i] * this[i]
        normB += other[i] * other[i]
    }

    val magnitude = kotlin.math.sqrt(normA) * kotlin.math.sqrt(normB)
    return if (magnitude > 0f) dotProduct / magnitude else 0f
}

fun getCurrentDateString(): String {
    val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
    return sdf.format(java.util.Date())
}

fun getFormattedTime(timeMillis: Long): String {
    val sdf = java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.getDefault())
    return sdf.format(java.util.Date(timeMillis))
}

fun getFormattedDateTime(timeMillis: Long): String {
    val sdf = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault())
    return sdf.format(java.util.Date(timeMillis))
}

suspend inline fun <T> retryWithBackoff(
    maxRetries: Int = 3,
    initialDelayMs: Long = 1000,
    maxDelayMs: Long = 8000,
    multiplier: Double = 2.0,
    crossinline block: suspend () -> com.attendance.facerec.domain.model.Result<T>
): com.attendance.facerec.domain.model.Result<T> {
    var currentDelay = initialDelayMs
    var lastException: Exception? = null

    repeat(maxRetries) { attempt ->
        try {
            val result = block()
            if (result is com.attendance.facerec.domain.model.Result.Success) {
                return result
            }
            if (result is com.attendance.facerec.domain.model.Result.Error) {
                lastException = result.exception
                if (attempt < maxRetries - 1) {
                    kotlinx.coroutines.delay(currentDelay)
                    currentDelay = (currentDelay * multiplier).toLong().coerceAtMost(maxDelayMs)
                }
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            lastException = e
            if (attempt < maxRetries - 1) {
                kotlinx.coroutines.delay(currentDelay)
                currentDelay = (currentDelay * multiplier).toLong().coerceAtMost(maxDelayMs)
            }
        }
    }

    return com.attendance.facerec.domain.model.Result.Error(
        lastException ?: Exception("Max retries exceeded")
    )
}
