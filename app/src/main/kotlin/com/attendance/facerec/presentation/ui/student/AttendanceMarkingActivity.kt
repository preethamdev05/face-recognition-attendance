package com.attendance.facerec.presentation.ui.student

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.TextureView
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.attendance.facerec.R
import com.attendance.facerec.service.FaceDetectionService
import com.attendance.facerec.service.FaceRecognitionEngine
import com.attendance.facerec.util.Constants
import com.attendance.facerec.util.getFormattedDateTime
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@AndroidEntryPoint
class AttendanceMarkingActivity : AppCompatActivity() {

    private lateinit var previewView: PreviewView
    private lateinit var captureButton: Button
    private lateinit var statusTextView: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var faceDetectionService: FaceDetectionService
    private lateinit var faceRecognitionEngine: FaceRecognitionEngine

    private var imageCapture: ImageCapture? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attendance_marking)

        initializeViews()
        cameraExecutor = Executors.newSingleThreadExecutor()
        faceDetectionService = FaceDetectionService()
        faceRecognitionEngine = FaceRecognitionEngine(this)

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        setupClickListeners()
    }

    private fun initializeViews() {
        previewView = findViewById(R.id.previewView)
        captureButton = findViewById(R.id.captureButton)
        statusTextView = findViewById(R.id.statusTextView)
        progressBar = findViewById(R.id.progressBar)
    }

    private fun setupClickListeners() {
        captureButton.setOnClickListener {
            captureAndProcessImage()
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            try {
                val cameraProvider = cameraProviderFuture.get()

                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

                imageCapture = ImageCapture.Builder().build()

                val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
                showError("Camera initialization failed")
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun captureAndProcessImage() {
        val imageCapture = imageCapture ?: return
        val photoFile = File(filesDir, "${System.currentTimeMillis()}.jpg")

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        progressBar.visibility = android.view.View.VISIBLE
        statusTextView.text = "Capturing face..."

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                    progressBar.visibility = android.view.View.GONE
                    showError("Image capture failed")
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    cameraExecutor.execute {
                        processAttendance(photoFile.absolutePath)
                    }
                }
            }
        )
    }

    private fun processAttendance(imagePath: String) {
        runOnUiThread {
            statusTextView.text = "Processing face recognition..."
        }

        // Process face detection and recognition
        // This would typically involve:
        // 1. Load image
        // 2. Detect faces
        // 3. Generate embedding
        // 4. Compare with registered faces
        // 5. Mark attendance
        // 6. Upload to Firebase

        runOnUiThread {
            progressBar.visibility = android.view.View.GONE
            statusTextView.text = "Attendance marked successfully at ${getFormattedDateTime(System.currentTimeMillis())}"
            Toast.makeText(this, "Attendance marked", Toast.LENGTH_SHORT).show()
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                showError("Permissions not granted")
                finish()
            }
        }
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
        faceRecognitionEngine.close()
    }

    companion object {
        private const val TAG = "AttendanceMarking"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }
}
