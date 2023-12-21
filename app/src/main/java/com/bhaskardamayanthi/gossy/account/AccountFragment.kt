package com.bhaskardamayanthi.gossy.account

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.media.audiofx.BassBoost
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.bhaskardamayanthi.gossy.R
import com.bhaskardamayanthi.gossy.databinding.FragmentAccountBinding
import com.bhaskardamayanthi.gossy.loading.Loading.dismissDialogForLoading
import com.bhaskardamayanthi.gossy.loading.Loading.showAlertDialogForLoading
import com.bhaskardamayanthi.gossy.localStore.StoreManager
import com.bhaskardamayanthi.gossy.managers.NameToProfileManager
import com.bumptech.glide.Glide
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class AccountFragment : Fragment() {
    private var permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { map ->
        var isGranted = true
        for (item in map) {
            if (!item.value) {
                isGranted = false
            }
        }
        if (isGranted) {
            openGallery() // If permissions granted, open the gallery to pick an image
        } else {
            Toast.makeText(requireContext(), "Permission is denied", Toast.LENGTH_SHORT).show()
            openAppSettings(requireContext())
        }
    }

    // Pick image result launcher
    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val selectedImageUri = data?.data
            binding.circleImageView.setImageURI(selectedImageUri)
         uploadImageToFirebase(selectedImageUri!!, requireContext())
        }
    }

    private lateinit var binding:FragmentAccountBinding
    private val storage = FirebaseStorage.getInstance()
    private val storageReference = storage.reference



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountBinding.inflate(layoutInflater,container,false)
   binding.circleImageView.setOnClickListener {
       readPermission()
   }
        binding.goGalleryBtn.setOnClickListener {
            readPermission()
        }
        val storeManager = StoreManager(requireContext())
        val textColor = Color.GRAY // Text color
        val number = storeManager.getString("number","")
        val backgroundColor = NameToProfileManager.getRandomColor() // Random background color
        val initial = NameToProfileManager.getProfileInitial(storeManager.getString("name",""),)
        val bitmap = NameToProfileManager.textAsBitmap( initial,20f, textColor, backgroundColor)
        binding.circleImageView.setImageBitmap(bitmap)
        binding.name.setText(storeManager.getString("name1",""))
        binding.name2.setText(storeManager.getString("name2",""))
        binding.upload.setOnClickListener {
            val newName = binding.name.text.toString()+" "+binding.name2.text.toString()

            updateName(number,newName)
        }
        return binding.root
    }
    fun uploadImageToFirebase(imageUri: Uri, requireContext: Context) {
        val storeManager = StoreManager(requireContext)
        val number = storeManager.getString("number","")
        showAlertDialogForLoading(requireContext())



        // Create a storage reference for the image with a unique name
        val storageRef = storageReference.child("profile/${UUID.randomUUID()}")

        // Upload the image file to Firebase Storage
        storageRef.putFile(imageUri)
            .addOnSuccessListener {
                // Once the image is successfully uploaded, get its download URL
                storageRef.downloadUrl.addOnSuccessListener { imgLink ->

                   // Glide.with(requireContext).load(imgLink).into(binding.circleImageView)

                    storeManager.saveString("profile",imgLink.toString())
                    updateProfile(number,imgLink.toString())


                }
            }
            .addOnFailureListener {
                // If image upload fails, show an error message
                Toast.makeText(requireContext, "Image upload failed", Toast.LENGTH_SHORT).show()
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
        return ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED
    }
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        pickImageLauncher.launch(intent)
    }
    private fun updateName(number:String,name:String){

       val database = Firebase.database("https://gossy-fbbcf-default-rtdb.asia-southeast1.firebasedatabase.app/").reference.child("users")
        database.child(number).child("name").setValue(name).addOnSuccessListener {
            Toast.makeText(requireContext(), "Name is updated!!", Toast.LENGTH_SHORT).show()
        }
    }
    private fun updateProfile(number: String,profile:String){
        val database = Firebase.database("https://gossy-fbbcf-default-rtdb.asia-southeast1.firebasedatabase.app/").reference.child("users")
        database.child(number).child("profile").setValue(profile).addOnSuccessListener {
            Toast.makeText(requireContext(), "Profile is updated!!", Toast.LENGTH_SHORT).show()
            dismissDialogForLoading()
        }
    }

    fun openAppSettings(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        startActivityForResult(intent, 10001)
    }



}