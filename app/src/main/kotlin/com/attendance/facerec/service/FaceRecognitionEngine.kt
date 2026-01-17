package com.attendance.facerec.service

import android.content.Context
import android.graphics.Bitmap
import com.attendance.facerec.domain.model.FaceEncoding
import com.attendance.facerec.domain.model.FaceMatchResult
import com.attendance.facerec.ml.TensorFlowFaceNet
import com.attendance.facerec.util.cosineSimilarity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import java.nio.ByteBuffer
import java.nio.ByteOrder

class FaceRecognitionEngine(context: Context) {

    private val modelPath = "facenet_model.tflite"
    private val inputSize = 160
    private val embeddingSize = 128

    private var interpreter: Interpreter? = null

    init {
        try {
            val modelBuffer = FileUtil.loadMappedFile(context, modelPath)
            interpreter = Interpreter(modelBuffer)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun generateFaceEmbedding(bitmap: Bitmap): FloatArray =
        withContext(Dispatchers.Default) {
            try {
                val resizedBitmap = Bitmap.createScaledBitmap(bitmap, inputSize, inputSize, true)
                val inputBuffer = bitmapToByteBuffer(resizedBitmap)
                val outputBuffer = Array(1) { FloatArray(embeddingSize) }

                interpreter?.run(inputBuffer, outputBuffer)
                outputBuffer[0]
            } catch (e: Exception) {
                FloatArray(embeddingSize)
            }
        }

    suspend fun compareFaceEncodings(
        encoding1: FloatArray,
        encoding2: FloatArray,
        threshold: Float = 0.6f
    ): Float = withContext(Dispatchers.Default) {
        encoding1.cosineSimilarity(encoding2)
    }

    suspend fun findBestMatch(
        studentEncoding: FloatArray,
        registeredEncodings: List<FaceEncoding>,
        threshold: Float = 0.6f
    ): FaceMatchResult? = withContext(Dispatchers.Default) {
        var bestMatch: FaceMatchResult? = null
        var bestSimilarity = 0f

        registeredEncodings.forEach { registered ->
            val similarity = compareFaceEncodings(studentEncoding, registered.encoding, threshold)
            if (similarity > bestSimilarity && similarity >= threshold) {
                bestSimilarity = similarity
                bestMatch = FaceMatchResult(
                    studentId = registered.studentId,
                    similarity = similarity,
                    isConfident = similarity >= threshold
                )
            }
        }

        bestMatch
    }

    private fun bitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val byteBuffer = ByteBuffer.allocateDirect(1 * inputSize * inputSize * 3 * 4)
        byteBuffer.order(ByteOrder.nativeOrder())
        byteBuffer.rewind()

        val intValues = IntArray(inputSize * inputSize)
        bitmap.getPixels(intValues, 0, inputSize, 0, 0, inputSize, inputSize)

        for (pixelValue in intValues) {
            byteBuffer.putFloat(((pixelValue shr 16) and 0xFF) / 255.0f)
            byteBuffer.putFloat(((pixelValue shr 8) and 0xFF) / 255.0f)
            byteBuffer.putFloat((pixelValue and 0xFF) / 255.0f)
        }

        byteBuffer.rewind()
        return byteBuffer
    }

    fun close() {
        interpreter?.close()
    }
}
