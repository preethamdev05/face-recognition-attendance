package com.attendance.facerec.service

import android.graphics.Bitmap
import android.graphics.RectF
import com.attendance.facerec.domain.model.FaceDetectionResult
import com.attendance.facerec.domain.model.FaceLandmark
import com.attendance.facerec.util.Constants
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FaceDetectionService {

    private val options = FaceDetectorOptions.Builder()
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
        .setMinFaceSize(0.1f)
        .enableTracking()
        .build()

    private val detector = FaceDetection.getClient(options)

    suspend fun detectFaces(bitmap: Bitmap): List<FaceDetectionResult> =
        withContext(Dispatchers.Default) {
            try {
                val inputImage = InputImage.fromBitmap(bitmap, 0)
                val faces = detector.process(inputImage).addOnSuccessListener { detectedFaces ->
                    // Handle in caller
                }.result

                faces.mapIndexed { index, face ->
                    FaceDetectionResult(
                        faceId = index,
                        boundingBox = face.boundingBox,
                        confidence = 0.95f, // ML Kit doesn't provide confidence directly
                        landmarks = face.landmarks.map { landmark ->
                            FaceLandmark(
                                type = landmark.landmarkType,
                                x = landmark.position.x,
                                y = landmark.position.y
                            )
                        },
                        isSmiling = face.smilingProbability ?: 0f > 0.5f,
                        rightEyeOpen = face.rightEyeOpenProbability ?: 0f > 0.5f,
                        leftEyeOpen = face.leftEyeOpenProbability ?: 0f > 0.5f,
                        eulerAngleY = face.headEulerAngleY,
                        eulerAngleZ = face.headEulerAngleZ,
                        eulerAngleX = face.headEulerAngleX
                    )
                }
            } catch (e: Exception) {
                emptyList()
            }
        }

    suspend fun validateFaceRegistration(faceResult: FaceDetectionResult): ValidationResult =
        withContext(Dispatchers.Default) {
            val box = faceResult.boundingBox
            val faceWidth = box.width()
            val faceHeight = box.height()

            when {
                faceWidth < Constants.MIN_FACE_WIDTH || faceHeight < Constants.MIN_FACE_HEIGHT ->
                    ValidationResult.FaceTooSmall
                kotlin.math.abs(faceResult.eulerAngleY) > 45 || kotlin.math.abs(faceResult.eulerAngleX) > 45 ->
                    ValidationResult.FaceAngleTooExtreme
                !faceResult.leftEyeOpen || !faceResult.rightEyeOpen ->
                    ValidationResult.EyesClosed
                else -> ValidationResult.Valid
            }
        }

    sealed class ValidationResult {
        object Valid : ValidationResult()
        object FaceTooSmall : ValidationResult()
        object FaceAngleTooExtreme : ValidationResult()
        object EyesClosed : ValidationResult()
        data class Error(val message: String) : ValidationResult()
    }
}
