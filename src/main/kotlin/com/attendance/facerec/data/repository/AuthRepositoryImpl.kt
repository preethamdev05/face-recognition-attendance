package com.attendance.facerec.data.repository

import com.attendance.facerec.domain.model.Result
import com.attendance.facerec.domain.model.User
import com.attendance.facerec.domain.model.UserRole
import com.attendance.facerec.util.SecurityUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase
) {

    suspend fun loginWithEmail(email: String, password: String): Result<User> =
        withContext(Dispatchers.IO) {
            try {
                val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
                val firebaseUser = authResult.user ?: throw Exception("Login failed")

                val user = fetchUserFromDatabase(firebaseUser.uid)
                Result.Success(user)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    suspend fun registerWithEmail(email: String, password: String, name: String): Result<User> =
        withContext(Dispatchers.IO) {
            try {
                val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
                val firebaseUser = authResult.user ?: throw Exception("Registration failed")

                // Update profile
                val profileUpdate = UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build()
                firebaseUser.updateProfile(profileUpdate).await()

                // Create user in database
                val user = User(
                    id = firebaseUser.uid,
                    email = email,
                    name = name,
                    role = UserRole.STUDENT,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                )

                saveUserToDatabase(user)
                Result.Success(user)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    suspend fun logout(): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                firebaseAuth.signOut()
                Result.Success(Unit)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    suspend fun getCurrentUser(): Result<User> =
        withContext(Dispatchers.IO) {
            try {
                val firebaseUser = firebaseAuth.currentUser ?: throw Exception("No user logged in")
                val user = fetchUserFromDatabase(firebaseUser.uid)
                Result.Success(user)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    suspend fun verifyPhoneOtp(verificationId: String, otp: String): Result<User> =
        withContext(Dispatchers.IO) {
            try {
                val credential = PhoneAuthProvider.getCredential(verificationId, otp)
                val authResult = firebaseAuth.signInWithCredential(credential).await()
                val firebaseUser = authResult.user ?: throw Exception("Phone verification failed")

                val user = fetchUserFromDatabase(firebaseUser.uid)
                Result.Success(user)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    suspend fun resetPassword(email: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                firebaseAuth.sendPasswordResetEmail(email).await()
                Result.Success(Unit)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    suspend fun updateUserProfile(user: User): Result<User> =
        withContext(Dispatchers.IO) {
            try {
                saveUserToDatabase(user)
                Result.Success(user)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    private suspend fun fetchUserFromDatabase(userId: String): User =
        suspendCancellableCoroutine { continuation ->
            firebaseDatabase.reference.child("users").child(userId).get()
                .addOnSuccessListener { snapshot ->
                    try {
                        val user = snapshot.getValue(User::class.java) ?: User(id = userId)
                        continuation.resume(user)
                    } catch (e: Exception) {
                        continuation.resumeWithException(e)
                    }
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }

    private suspend fun saveUserToDatabase(user: User): Unit =
        withContext(Dispatchers.IO) {
            val userMap = mapOf(
                "id" to user.id,
                "email" to user.email,
                "name" to user.name,
                "role" to user.role.name,
                "isActive" to user.isActive,
                "createdAt" to user.createdAt,
                "updatedAt" to System.currentTimeMillis()
            )
            firebaseDatabase.reference.child("users").child(user.id).setValue(userMap).await()
        }
}
