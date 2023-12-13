package com.bhaskardamayanthi.gossy.auth

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bhaskardamayanthi.gossy.R
import com.bhaskardamayanthi.gossy.databinding.ActivityFetchDataLoadingBinding
import com.bhaskardamayanthi.gossy.localStore.StoreManager
import com.bhaskardamayanthi.gossy.managers.FirebaseDataManager
import com.bhaskardamayanthi.gossy.managers.TokenManager
import com.bhaskardamayanthi.gossy.model.UserModel
import com.google.firebase.auth.FirebaseAuth

class FetchDataLoadingActivity : AppCompatActivity() {
    private lateinit var binding:ActivityFetchDataLoadingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityFetchDataLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val storeManager = StoreManager(this)
        val userId = FirebaseAuth.getInstance().currentUser?.phoneNumber
        val firebaseDataManager =FirebaseDataManager()
        val token =TokenManager(this)

        if (storeManager.getBoolean("new",false)){

            val name = storeManager.getString("name","")
            val sex = storeManager.getString("sex","")
            val fakeName = storeManager.getString("fakeName","")
            val fakeImg = storeManager.getString("fakeImg","")
            val dob = storeManager.getString("dobYear","")
            val collegeName = storeManager.getString("college","")
            val fcmToken =token.getSavedToken().toString()
            val phoneNumber = storeManager.getString("number","")

            val newUserData = UserModel(collegeName,sex,name,phoneNumber,"",dob,fakeName,fakeImg,fcmToken)
             firebaseDataManager.uploadUserToDatabase(phoneNumber,newUserData,this)
            Toast.makeText(this, userId.toString(), Toast.LENGTH_SHORT).show()


        }else{

        }

    }
}