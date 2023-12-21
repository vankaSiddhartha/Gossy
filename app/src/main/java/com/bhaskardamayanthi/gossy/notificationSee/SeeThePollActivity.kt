package com.bhaskardamayanthi.gossy.notificationSee


import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.Manifest
import android.content.Context
import android.graphics.Canvas
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bhaskardamayanthi.gossy.R
import com.bhaskardamayanthi.gossy.databinding.ActivitySeetThePollBinding
import com.bhaskardamayanthi.gossy.localStore.StoreManager
import java.io.File
import java.io.FileOutputStream
import java.io.IOException




class SeeThePollActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySeetThePollBinding
    private val WRITE_EXTERNAL_STORAGE_REQUEST = 123

// Check if permission is not granted

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeetThePollBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)

        val storeManager = StoreManager(this)
        binding.cardView6.setOnClickListener {
         permission()
        }
        val name = storeManager.getString("name", "")
        val getGenderTittle = intent.getStringExtra("sex")
        val getQuestion = intent.getStringExtra("question")
        binding.questionTv.text = getQuestion
        binding.genderTitle.text = getGenderTittle
        binding.tagTv.text = "@" + name
        if (getGenderTittle == "male") {


            val color = ContextCompat.getColor(this, R.color.blue) // Use your color resource
            binding.layout.setBackgroundColor(color)

            binding.emojiId.text = "🧑‍🦱"
        } else {

            binding.emojiId.text = "👩"
            val color = ContextCompat.getColor(this, R.color.pink) // Use your color resource
            binding.layout.setBackgroundColor(color)
        }




        binding.snapchat.setOnClickListener {

            takeScreenshotAndShareToSnapchat()
        }
        binding.instagram.setOnClickListener {
            takeScreenshotAndShareToInstagram()
        }

    }

//    private fun takeScreenshotAndShareToInstagram() {
//        val rootView: View = window.decorView.rootView
//
//        // Capture the current screen content as a bitmap
//        rootView.isDrawingCacheEnabled = true
//        val screenshotBitmap = Bitmap.createBitmap(rootView.drawingCache)
//        rootView.isDrawingCacheEnabled = false
//        // Share the screenshot to Instagram Stories
//        shareToInstagramStory(screenshotBitmap)
//    }
private fun takeScreenshotAndShareToInstagram() {
    permission()
    val rootView: View = window.decorView.rootView

    // Create a bitmap of the current screen content
    val screenshotBitmap = Bitmap.createBitmap(rootView.width, rootView.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(screenshotBitmap)
    rootView.draw(canvas)

    // Share the screenshot to Instagram Stories
    shareToInstagramStory(screenshotBitmap)
}


    private fun shareToInstagramStory(screenshotBitmap: Bitmap) {

        // Convert the bitmap to a Uri
        val imageUri: Uri = getImageUri(screenshotBitmap)!!

        // Create an Intent with ACTION_SEND and set the package to Instagram
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_STREAM, imageUri)
        intent.setPackage("com.instagram.android")

        // Grant permission to read the Uri
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        // Verify if Instagram is installed
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            // Instagram is not installed, handle this case
            // Prompt the user to install Instagram from the Play Store
         //   Toast.makeText(this, "nooo", Toast.LENGTH_SHORT).show()
        }
    }

//    private fun getImageUri(bitmap: Bitmap): Uri? {
//        val imagesCollection =
//            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
//
//        val contentValues = ContentValues().apply {
//            put(MediaStore.Images.Media.DISPLAY_NAME, "screenshot") // Set your desired display name
//            put(MediaStore.Images.Media.MIME_TYPE, "image/png") // Set appropriate MIME type
//        }
//
//        // Create a write request to insert an image into MediaStore
//        val imageUri = contentResolver.insert(imagesCollection, contentValues)
//
//        return imageUri?.let { uri ->
//            try {
//                contentResolver.openOutputStream(uri)?.use { outputStream ->
//                    // Compress bitmap and write it to the output stream
//                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
//                }
//                uri // Return the URI if the image is successfully saved
//            } catch (e: IOException) {
//                e.printStackTrace()
//                null // Return null if there's an exception while writing the image
//            }
//        }
//    }
//
private fun getImageUri(bitmap: Bitmap): Uri? {
    val displayName = "screenshot" // Set your desired display name
    val mimeType = "image/png" // Set appropriate MIME type

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        // For Android Q (API 29) and above
        val imagesCollection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)

        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, displayName)
            put(MediaStore.Images.Media.MIME_TYPE, mimeType)
        }

        val imageUri = contentResolver.insert(imagesCollection, contentValues)

        return imageUri?.let { uri ->
            try {
                contentResolver.openOutputStream(uri)?.use { outputStream ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                }
                uri
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }
    } else {
        // For versions below Android Q
        val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val image = File(imagesDir, "$displayName.png")

        return try {
            FileOutputStream(image).use { fos ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            }
            Uri.fromFile(image)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}

    private fun shareToSnapchat(message: String, contentUri: Uri) {
        // Create an Intent with ACTION_SEND and set the package to Snapchat
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "image/*" // Change the type if sharing a different content type
        intent.putExtra(Intent.EXTRA_STREAM, contentUri)
        intent.putExtra(Intent.EXTRA_TEXT, message) // Add a text message if needed
        intent.setPackage("com.snapchat.android")

        // Grant permission to read the Uri
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        // Verify if Snapchat is installed
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            // Snapchat is not installed, handle this case
            // Prompt the user to install Snapchat from the Play Store
        }
    }

    private fun takeScreenshotAndShareToSnapchat() {
    permission()
        val rootView: View = window.decorView.rootView

        // Create a bitmap of the current screen content
        val screenshotBitmap = Bitmap.createBitmap(rootView.width, rootView.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(screenshotBitmap)
        rootView.draw(canvas)

        // Share the screenshot to Snapchat
        shareToSnapchat("Your text message", getImageUri(screenshotBitmap)!!)
    }




// Check if permission is not granted


    // Handle the permission result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, you can proceed with writing to external storage

            } else {
                // Permission denied, handle accordingly (e.g., show a message to the user)
                openAppSettings(this)
                Toast.makeText(this, "Please grant permission friends", Toast.LENGTH_SHORT).show()
            }
        }


    }
    private fun permission(){
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request the permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                WRITE_EXTERNAL_STORAGE_REQUEST
            )
        }
    }
    fun openAppSettings(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        startActivityForResult(intent, 10001)
    }
}
