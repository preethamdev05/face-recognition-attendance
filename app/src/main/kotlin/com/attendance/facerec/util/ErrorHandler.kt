package com.attendance.facerec.util

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.database.DatabaseException
import com.google.firebase.storage.StorageException
import java.io.IOException

object ErrorHandler {
    fun handleError(context: Context, exception: Exception, callback: (String) -> Unit) {
        val errorMessage = getErrorMessage(exception)
        showErrorDialog(context, errorMessage, callback)
    }

    fun getErrorMessage(exception: Exception): String = when (exception) {
        is FirebaseAuthException -> when (exception.errorCode) {
            "ERROR_USER_NOT_FOUND" -> "User account not found"
            "ERROR_WRONG_PASSWORD" -> "Incorrect password"
            "ERROR_INVALID_EMAIL" -> "Invalid email format"
            "ERROR_USER_DISABLED" -> "User account is disabled"
            "ERROR_EMAIL_ALREADY_IN_USE" -> "Email already registered"
            "ERROR_WEAK_PASSWORD" -> "Password must be at least 6 characters"
            "ERROR_OPERATION_NOT_ALLOWED" -> "Operation not allowed"
            "ERROR_TOO_MANY_REQUESTS" -> "Too many login attempts. Try again later"
            else -> "Authentication failed: ${exception.message}"
        }
        is DatabaseException -> "Database error: ${exception.message}"
        is StorageException -> "Storage operation failed: ${exception.message}"
        is IOException -> "Network error: ${exception.message}"
        is IllegalArgumentException -> "Invalid input: ${exception.message}"
        is SecurityException -> "Permission denied: ${exception.message}"
        else -> "An unexpected error occurred: ${exception.message}"
    }

    private fun showErrorDialog(context: Context, message: String, callback: (String) -> Unit) {
        AlertDialog.Builder(context)
            .setTitle("Error")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                callback(message)
            }
            .setCancelable(false)
            .show()
    }
}
