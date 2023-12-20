package com.bhaskardamayanthi.gossy.notificationSee

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bhaskardamayanthi.gossy.R
import com.bhaskardamayanthi.gossy.aiNamesAndImage.AiImage
import com.bhaskardamayanthi.gossy.databinding.ActivitySeetThePollBinding
import com.bhaskardamayanthi.gossy.localStore.StoreManager
import com.bumptech.glide.Glide
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.random.Random

class SeeThePollActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySeetThePollBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeetThePollBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)
        val aiImage = AiImage()
        val storeManager = StoreManager(this)
        val gender = storeManager.getString("sex","")
        val  name = storeManager.getString("name","")
        val getGenderTittle = intent.getStringExtra("sex")
        val getQuestion =intent.getStringExtra("question")
        binding.questionTv.text= getQuestion
        binding.genderTitle.text = getGenderTittle
        binding.tagTv.text = "@Siddhartha"
        if (gender == "male"){
            val generatedMaleImageId = Random.nextInt(aiImage.maleImg.size)
            val boyAvatar = aiImage.maleImg.get(generatedMaleImageId)
            val color = ContextCompat.getColor(this, R.color.blue) // Use your color resource
            binding.layout.setBackgroundColor(color)

            binding.emojiId.text = "🧑‍🦱"
        }else{
            val generatedMaleImageId = Random.nextInt(aiImage.femaleImg.size)
            val boyAvatar = aiImage.femaleImg.get(generatedMaleImageId)
            binding.emojiId.text ="👩"
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
    private fun takeScreenshotAndShareToInstagram() {
        val rootView: View = window.decorView.rootView

        // Capture the current screen content as a bitmap
        rootView.isDrawingCacheEnabled = true
        val screenshotBitmap = Bitmap.createBitmap(rootView.drawingCache)
        rootView.isDrawingCacheEnabled = false
        // Share the screenshot to Instagram Stories
        shareToInstagramStory(screenshotBitmap)
    }

    private fun shareToInstagramStory(screenshotBitmap: Bitmap) {

        // Convert the bitmap to a Uri
        val imageUri: Uri = getImageUri(screenshotBitmap)

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
            Toast.makeText(this, "nooo", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getImageUri(bitmap: Bitmap): Uri {
        // Get the Uri from the bitmap
        Toast.makeText(this, "4", Toast.LENGTH_SHORT).show()
        val path = MediaStore.Images.Media.insertImage(
            contentResolver,
            bitmap,
            "screenshot",
            null
        )
        return Uri.parse(path)
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
        val rootView: View = window.decorView.rootView

        // Capture the current screen content as a bitmap
        rootView.isDrawingCacheEnabled = true
        val screenshotBitmap = Bitmap.createBitmap(rootView.drawingCache)
        rootView.isDrawingCacheEnabled = false

        // Share the screenshot to Snapchat
        shareToSnapchat("Your text message", getImageUri(screenshotBitmap))
    }
}
