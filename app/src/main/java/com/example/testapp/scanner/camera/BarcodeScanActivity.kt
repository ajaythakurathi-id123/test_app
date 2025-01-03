package com.example.testapp.scanner.camera

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import android.view.SurfaceHolder
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.testapp.databinding.ActivityScanBarcodeBinding
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import java.io.IOException

class BarcodeScanActivity : AppCompatActivity() {

    private lateinit var barcodeDetector: BarcodeDetector
    private lateinit var cameraSource: CameraSource
    private val REQUEST_CAMERA_PERMISSION = 201
    var intentData: String? = ""
    var isEmail = false
    private lateinit var binding: ActivityScanBarcodeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScanBarcodeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()

        initialiseDetectorsAndSources()
    }

    private fun initialiseDetectorsAndSources() {
        Toast.makeText(applicationContext, "Barcode scanner started", Toast.LENGTH_SHORT).show()
        barcodeDetector = BarcodeDetector.Builder(this)
            .setBarcodeFormats(Barcode.ALL_FORMATS)
            .build()
        cameraSource = CameraSource.Builder(this, barcodeDetector)
            .setRequestedPreviewSize(1920, 1080)
            .setAutoFocusEnabled(true) //you should add this feature
            .build()
        binding.surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                try {
                    if (ActivityCompat.checkSelfPermission(
                            applicationContext,
                            Manifest.permission.CAMERA
                        ) === PackageManager.PERMISSION_GRANTED
                    ) {
                        cameraSource.start(binding.surfaceView!!.holder)
                    } else {
                        ActivityCompat.requestPermissions(
                            this@BarcodeScanActivity,
                            arrayOf<String>(
                                Manifest.permission.CAMERA
                            ),
                            REQUEST_CAMERA_PERMISSION
                        )
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraSource.stop()
            }
        })
        barcodeDetector.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {
                Toast.makeText(
                    applicationContext,
                    "To prevent memory leaks barcode scanner has been stopped",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
                val barcodes: SparseArray<Barcode>? = detections.detectedItems
//                if (barcodes?.size() != 0) {
                if (barcodes?.size() != 0) {
                    /*binding.txtBarcodeValue.post {
                        binding.txtBarcodeValue.removeCallbacks(null)
                        intentData = barcodes.valueAt(0).email.address
                        binding.txtBarcodeValue.text = intentData
                        isEmail = true
                        binding.btnAction.text = "ADD CONTENT TO THE MAIL"

                        Log.d(TAG, "receiveDetections: $intentData")
                    }*/
                    binding.txtBarcodeValue.post {
                        if (barcodes?.valueAt(0)?.email != null) {
                            binding.txtBarcodeValue.removeCallbacks(null)
                            intentData = barcodes.valueAt(0).email.address
                            binding.txtBarcodeValue.text = intentData
                            isEmail = true
                            binding.btnAction.text = "ADD CONTENT TO THE MAIL"
                        } else {
                            isEmail = false
                            binding.btnAction.text = "LAUNCH URL"
                            intentData = barcodes?.valueAt(0)?.displayValue
                            binding.txtBarcodeValue.text = intentData
                        }
                        Log.d(TAG, "receiveDetections: $intentData")
                    }
                }
            }
        })
    }

    companion object {
        private const val TAG = "BarcodeScanActivity"
    }
}