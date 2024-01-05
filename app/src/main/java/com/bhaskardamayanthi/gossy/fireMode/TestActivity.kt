package com.bhaskardamayanthi.gossy.fireMode
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bhaskardamayanthi.gossy.R
import com.bhaskardamayanthi.gossy.databinding.ActivityTestBinding
import com.google.zxing.integration.android.IntentIntegrator

class TestActivity : AppCompatActivity() {
private lateinit var binding:ActivityTestBinding
    private val REQUEST_CAMERA_PERMISSION = 100
    private lateinit var resultTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val scanButton: Button = findViewById(R.id.scanButton)
        scanButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    REQUEST_CAMERA_PERMISSION
                )
            } else {
                initiateQRScan()
            }
        }
    }

    private fun initiateQRScan() {
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt("Scan a QR Code")
        integrator.setCameraId(0) // Use a specific camera (0 for back camera)
        integrator.setBeepEnabled(false)
        integrator.setBarcodeImageEnabled(true)
        integrator.initiateScan()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initiateQRScan()
            } else {
                // Handle permission denied
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                // Handle cancelled scan
                Toast.makeText(this, "Scan cancelled", Toast.LENGTH_SHORT).show()
            } else {
                // Handle scan result (result.contents)
                Toast.makeText(this,  "Scan result: ${result.contents}", Toast.LENGTH_SHORT).show()
                val url = "https://www.example.com"

                // Create an Intent with ACTION_VIEW and the URL
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(result.contents))

                // Verify if there's an app available to handle this intent
                if (intent.resolveActivity(packageManager) != null) {
                    // Start the activity if there's an app available
                    startActivity(intent)
                } else {
                    // Handle the case where there's no app to handle the intent
                    // For example, display a message to the user or handle the situation accordingly
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
