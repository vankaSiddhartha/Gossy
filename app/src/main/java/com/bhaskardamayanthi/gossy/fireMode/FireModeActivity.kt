package com.bhaskardamayanthi.gossy.fireMode

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bhaskardamayanthi.gossy.databinding.ActivityFireModeBinding
import com.bhaskardamayanthi.gossy.loading.Loading
import com.bhaskardamayanthi.gossy.localStore.StoreManager
import com.bhaskardamayanthi.gossy.model.PremiumModel
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionText
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.UUID


class FireModeActivity() : AppCompatActivity() {
    private lateinit var storeManager: StoreManager
    private val storage = FirebaseStorage.getInstance()
    private val storageReference = storage.reference
    private lateinit var myProfile: String
    private lateinit var selectedImgUri: Uri
    private var permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { map ->
            var isGranted = true
            for (item in map) {
                if (!item.value) {
                    isGranted = false
                }
            }
            if (isGranted) {
                openGallery() // If permissions granted, open the gallery to pick an image
            } else {
                Toast.makeText(this, "Permission is denied", Toast.LENGTH_SHORT).show()
                openAppSettings(this)
            }
        }

    // Pick image result launcher
    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val selectedImageUri = data?.data
                if (selectedImageUri != null) {
                      //readTextFromImageUri(this,selectedImageUri)
//                   val img = FirebaseVisionImage.fromFilePath(this,selectedImageUri)
//                    FirebaseVision.getInstance().onDeviceTextRecognizer.processImage(img).addOnSuccessListener { firebaseVisionText ->
//                        recognizeText(firebaseVisionText)
                    getProvider { provider->
                        readTextFromImageUri(this,selectedImageUri,provider)
                    }

//                    }.addOnFailureListener{
//                        Toast.makeText(this, "Yes", Toast.LENGTH_SHORT).show()
//                    }
                } else {
                    Toast.makeText(this, "Select again", Toast.LENGTH_SHORT).show()
                }

            }
        }

//    private fun recognizeText(firebaseVisionText: FirebaseVisionText) {
//        if(firebaseVisionText.textBlocks.size == 0){
//            Toast.makeText(this, "No text", Toast.LENGTH_SHORT).show()
//            return
//        }
//        for( block in firebaseVisionText.textBlocks){
//            val text = block.text
//          if (text.toString().contains("Vanka")){
//              Toast.makeText(this, "Yess", Toast.LENGTH_SHORT).show()
//          }
//        }
//    }

    private lateinit var binding: ActivityFireModeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFireModeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.upload.setOnClickListener {
            val intent = Intent(this, QRActivity::class.java)
            startActivity(intent)
        }
        binding.uploadPayment.setOnClickListener {
            readPermission()
        }
        val storeManager = StoreManager(this)
        val phone = storeManager.getString("number","")
        getPaymentStatus(phone)


    }
    private fun getProvider(callbacks: (String)->Unit){
        val ref = Firebase.database("https://gossy-fbbcf-default-rtdb.asia-southeast1.firebasedatabase.app/").reference.child(
                "provider"
            )
        ref.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val provider = snapshot.value.toString()
                callbacks(provider)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
    fun uploadImageToFirebase(imageUri: Uri, requireContext: Context,status:String) {
         val storeManager = StoreManager(this)
        val number = storeManager.getString("number", "")



        // Create a storage reference for the image with a unique name
        val storageRef = storageReference.child("payments/${UUID.randomUUID()}")

        // Upload the image file to Firebase Storage
        storageRef.putFile(imageUri)
            .addOnSuccessListener {
                // Once the image is successfully uploaded, get its download URL
                storageRef.downloadUrl.addOnSuccessListener { imgLink ->
                    myProfile = imgLink.toString()
                    //  Loading.dismissDialogForLoading()
                    // Glide.with(requireContext).load(imgLink).into(binding.circleImageView)

                    // storeManager.saveString("profile",imgLink.toString())
                    updateProfile(number, imgLink.toString(),status)


                }
            }
            .addOnFailureListener {
                // If image upload fails, show an error message
                Toast.makeText(requireContext, it.message, Toast.LENGTH_SHORT).show()
                Loading.dismissDialogForLoading()
            }
    }

    private fun readPermission() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        if (!hasPermissionCheck(permission[0])) {
            permissionLauncher.launch(permission)
        } else {
            openGallery() // If permission already granted, open the gallery

        }
    }

    // Method to check if a permission is granted
    private fun hasPermissionCheck(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        pickImageLauncher.launch(intent)
    }

    private fun updateProfile(number: String, profile: String,status:String) {

        val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
        val currentDateAndTime: String = sdf.format(Date())
        val data = PremiumModel(number, currentDateAndTime,profile,status)
        val database =
            Firebase.database("https://gossy-fbbcf-default-rtdb.asia-southeast1.firebasedatabase.app/").reference.child(
                "payments"
            )
        database.child(number).setValue(data).addOnSuccessListener {
            getPaymentStatus(number)
            Loading.dismissDialogForLoading()


        }
    }

    fun openAppSettings(context: Context) {
        Toast.makeText(this, "give permission", Toast.LENGTH_SHORT).show()
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        startActivityForResult(intent, 10001)
    }

    fun readTextFromImageUri(context: Context, imageUri: Uri, provider: String) {
       Loading.showAlertDialogForLoading(this)
        val firebaseVisionImage: FirebaseVisionImage = FirebaseVisionImage.fromFilePath(context,imageUri)

        val firebaseVision: FirebaseVision = FirebaseVision.getInstance()
        val firebaseVisionTextRecognizer: FirebaseVisionTextRecognizer =
            firebaseVision.getOnDeviceTextRecognizer()

        //Process the Image

        //Process the Image
        val task: Task<FirebaseVisionText> =
            firebaseVisionTextRecognizer.processImage(firebaseVisionImage)

        task.addOnSuccessListener(OnSuccessListener<FirebaseVisionText> { firebaseVisionText: FirebaseVisionText ->
            //Set recognized text from image in our TextView
            val text: String = firebaseVisionText.getText()
            if (text.toString().contains(provider)){
                uploadImageToFirebase(imageUri,this,"nice")
            }else{
                Loading.dismissDialogForLoading()
                uploadImageToFirebase(imageUri,this,"review")
            }

        })
        task.addOnFailureListener { e: Exception ->
            Toast.makeText(
                this,
                e.message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    fun getPaymentStatus(phone:String){
     Loading.showAlertDialogForLoading(this)
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

                    binding.statusText.visibility= View.VISIBLE
                    val data = snapshot.getValue(PremiumModel::class.java)
                 //  Toast.makeText(this@FireModeActivity, "$data", Toast.LENGTH_SHORT).show()
                    if (data?.status.toString().equals("nice")){
                        if (compareDates(data?.date.toString(),currentDateAndTime)<0){

                            binding.statusText.text = "Firemode activated"
                        }else{
                            binding.statusText.text = "Renew your subscription"

                        }

                    }else if (data?.status.toString().equals("reject")){

                        binding.statusText.text = "Sorry, Payment rejected try again"
                    }

                        else{
                        binding.statusText.text = "Firemode will activated in 10minutes "

                    }
                }
                Loading.dismissDialogForLoading()

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

    override fun onStart() {
        super.onStart()

    }

}