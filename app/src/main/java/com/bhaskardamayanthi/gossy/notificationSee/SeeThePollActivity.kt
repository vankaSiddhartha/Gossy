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
import com.bhaskardamayanthi.gossy.MainActivity
import com.bhaskardamayanthi.gossy.R
import com.bhaskardamayanthi.gossy.databinding.ActivitySeetThePollBinding
import com.bhaskardamayanthi.gossy.fireMode.FireModeActivity
import com.bhaskardamayanthi.gossy.loading.Loading
import com.bhaskardamayanthi.gossy.localStore.StoreManager
import com.bhaskardamayanthi.gossy.model.PremiumModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date


class SeeThePollActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySeetThePollBinding
    private val WRITE_EXTERNAL_STORAGE_REQUEST = 123

// Check if permission is not granted

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeetThePollBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)

        binding.back.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("key","noti")
            startActivity(intent)
        }
        val from = intent.getStringExtra("from")
        val storeManager = StoreManager(this)
        val number = storeManager.getString("number","")
        binding.cardView6.setOnClickListener {
         permission()
        }
        val name = storeManager.getString("name", "")
        val getGenderTittle = intent.getStringExtra("sex")
        val getQuestion = intent.getStringExtra("question")
        binding.questionTv.text = getQuestion
        binding.genderTitle.text = getGenderTittle
        binding.tagTv.text = "@" + name
        if (getGenderTittle != null) {
            if (getGenderTittle.contains("boy")) {


                val color = ContextCompat.getColor(this, R.color.blue) // Use your color resource
                binding.layout.setBackgroundColor(color)

                binding.emojiId.text = "🧑‍🦱"
            } else {

                binding.emojiId.text = "👩"
                val color = ContextCompat.getColor(this, R.color.pink) // Use your color resource
                binding.layout.setBackgroundColor(color)
            }
        }




        binding.snapchat.setOnClickListener {
            Loading.showAlertDialogForLoading(this)
            takeScreenshotAndShareToSnapchat()
           // permission()
        }
        binding.instagram.setOnClickListener {
            Loading.showAlertDialogForLoading(this)
            takeScreenshotAndShareToInstagram()
        }
        binding.firemode.setOnClickListener {
            if (from != null) {
                getPaymentStatus(number,from)
            }else{
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("key","noti")
                startActivity(intent)
            }
        }


    }


private fun takeScreenshotAndShareToInstagram() {

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
            Loading.dismissDialogForLoading()
            startActivity(intent)
        } else {
            Loading.dismissDialogForLoading()
            Toast.makeText(this, "Instagram is not installed!!", Toast.LENGTH_SHORT).show()
        }
    }


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
            Loading.dismissDialogForLoading()
            startActivity(intent)

        } else {
            Loading.dismissDialogForLoading()
            Toast.makeText(this, "Install Snapchat!!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun takeScreenshotAndShareToSnapchat() {
      //  permission()
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
    private fun getUser(phone:String){
        val ref =Firebase.database("https://gossy-fbbcf-default-rtdb.asia-southeast1.firebasedatabase.app/").reference.child("users").child(phone).child("name")
             ref.addListenerForSingleValueEvent(object :ValueEventListener{
                 override fun onDataChange(snapshot: DataSnapshot) {
                     val name = snapshot.value.toString()
                     binding.genderTitle.text = "From $name"
                 }

                 override fun onCancelled(error: DatabaseError) {

                 }

             })

    }
    fun getPaymentStatus(phone:String,from:String){

        val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
        val currentDateAndTime: String = sdf.format(Date())
        val database =
            Firebase.database("https://gossy-fbbcf-default-rtdb.asia-southeast1.firebasedatabase.app/").reference.child(
                "payments"
            )
        database.child(phone).addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                // Toast.makeText(this@FireModeActivity, "$phone", Toast.LENGTH_SHORT).show()
                if (snapshot.exists()){


                    val data = snapshot.getValue(PremiumModel::class.java)
                    //  Toast.makeText(this@FireModeActivity, "$data", Toast.LENGTH_SHORT).show()

                    if (data?.status.toString().equals("nice")){
                        if (compareDates(data?.date.toString(),currentDateAndTime)<0){
                           ///get name
                            getUser(from)

                        }else{
                           //intent to Firemode

                            val intent = Intent(this@SeeThePollActivity, FireModeActivity::class.java)
                            startActivity(intent)
                        }

                    }else if (data?.status.toString().equals("reject")){
                        val intent = Intent(this@SeeThePollActivity, FireModeActivity::class.java)
                        startActivity(intent)
                        //intent to firemode
                    }

                    else{
                        val intent = Intent(this@SeeThePollActivity, FireModeActivity::class.java)
                        startActivity(intent)

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
    fun compareDates(dateText1: String, dateText2: String): Int {
        val pattern = "dd-MM-yyyy HH:mm:ss"
        val dateFormat = SimpleDateFormat(pattern)

        val date1: Date = dateFormat.parse(dateText1) ?: Date()
        val date2: Date = dateFormat.parse(dateText2) ?: Date()

        return date1.compareTo(date2)
    }
}
