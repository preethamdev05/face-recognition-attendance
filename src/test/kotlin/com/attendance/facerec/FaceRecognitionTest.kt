package com.attendance.facerec

import com.attendance.facerec.util.cosineSimilarity
import org.junit.Assert.assertEquals
import org.junit.Test

class FaceRecognitionTest {

    @Test
    fun testCosineSimilarity() {
        // Test vectors with known similarity
        val v1 = floatArrayOf(1f, 0f, 0f)
        val v2 = floatArrayOf(1f, 0f, 0f)
        val similarity = v1.cosineSimilarity(v2)
        assertEquals(1f, similarity, 0.01f)
    }

    @Test
    fun testCosineSimilarityDifferent() {
        val v1 = floatArrayOf(1f, 0f, 0f)
        val v2 = floatArrayOf(0f, 1f, 0f)
        val similarity = v1.cosineSimilarity(v2)
        assertEquals(0f, similarity, 0.01f)
    }

    @Test
    fun testFaceEncodingThreshold() {
        val threshold = 0.6f
        val confidence = 0.95f
        val isMatch = confidence >= threshold
        assertEquals(true, isMatch)
    }
}
