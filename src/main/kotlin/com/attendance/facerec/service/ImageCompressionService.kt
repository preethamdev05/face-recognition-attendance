package com.attendance.facerec.service

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class ImageCompressionService {

    suspend fun compressImage(
        inputPath: String,
        outputPath: String,
        quality: Int = 85,
        maxSizeKb: Int = 500
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            var bitmap = BitmapFactory.decodeFile(inputPath)
            bitmap = rotateImageIfNeeded(inputPath, bitmap)

            val maxWidth = 1920
            val maxHeight = 1920
            if (bitmap.width > maxWidth || bitmap.height > maxHeight) {
                val scale = minOf(
                    maxWidth.toFloat() / bitmap.width,
                    maxHeight.toFloat() / bitmap.height
                )
                val newWidth = (bitmap.width * scale).toInt()
                val newHeight = (bitmap.height * scale).toInt()
                bitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
            }

            FileOutputStream(outputPath).use { fos ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fos)
            }

            val file = File(outputPath)
            if (file.length() > maxSizeKb * 1024) {
                return@withContext compressImage(outputPath, outputPath, quality - 5, maxSizeKb)
            }

            bitmap.recycle()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun generateThumbnail(
        inputPath: String,
        outputPath: String,
        width: Int = 150,
        height: Int = 150
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            val bitmap = BitmapFactory.decodeFile(inputPath)
            val thumbnail = Bitmap.createScaledBitmap(bitmap, width, height, true)

            FileOutputStream(outputPath).use { fos ->
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 85, fos)
            }

            bitmap.recycle()
            thumbnail.recycle()
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun rotateImageIfNeeded(imagePath: String, bitmap: Bitmap): Bitmap {
        return try {
            val exif = ExifInterface(imagePath)
            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )

            val rotation = when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90f
                ExifInterface.ORIENTATION_ROTATE_180 -> 180f
                ExifInterface.ORIENTATION_ROTATE_270 -> 270f
                else -> 0f
            }

            if (rotation == 0f) return bitmap

            val matrix = Matrix()
            matrix.postRotate(rotation)
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        } catch (e: Exception) {
            bitmap
        }
    }
}
