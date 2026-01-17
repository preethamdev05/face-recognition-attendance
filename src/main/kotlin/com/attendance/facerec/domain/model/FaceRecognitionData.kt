package com.attendance.facerec.domain.model

import android.graphics.RectF
import java.io.Serializable

data class FaceDetectionResult(
    val faceId: Int = 0,
    val boundingBox: RectF = RectF(),
    val confidence: Float = 0f,
    val landmarks: List<FaceLandmark> = emptyList(),
    val isSmiling: Boolean = false,
    val rightEyeOpen: Boolean = false,
    val leftEyeOpen: Boolean = false,
    val eulerAngleY: Float = 0f,
    val eulerAngleZ: Float = 0f,
    val eulerAngleX: Float = 0f
) : Serializable

data class FaceLandmark(
    val type: Int = 0,
    val x: Float = 0f,
    val y: Float = 0f
) : Serializable

data class FaceEncoding(
    val studentId: String = "",
    val encoding: FloatArray = FloatArray(0),
    val timestamp: Long = System.currentTimeMillis(),
    val imageUrl: String = ""
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FaceEncoding) return false
        if (studentId != other.studentId) return false
        if (!encoding.contentEquals(other.encoding)) return false
        return true
    }

    override fun hashCode(): Int {
        var result = studentId.hashCode()
        result = 31 * result + encoding.contentHashCode()
        return result
    }
}

data class FaceMatchResult(
    val studentId: String = "",
    val studentName: String = "",
    val similarity: Float = 0f,
    val timestamp: Long = System.currentTimeMillis(),
    val isConfident: Boolean = similarity >= 0.6f
) : Serializable
