package com.bhaskardamayanthi.gossy.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bhaskardamayanthi.gossy.R
import com.bhaskardamayanthi.gossy.databinding.ActivityPermissionBinding
import com.bhaskardamayanthi.gossy.databinding.ActivityPhoneAuthBinding
import com.bhaskardamayanthi.gossy.localStore.StoreManager

class PhoneAuthActivity : AppCompatActivity() {
    private lateinit var binding:ActivityPhoneAuthBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityPhoneAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val storeManager = StoreManager(this)
        binding.upload.setOnClickListener {
            if (binding.numberEt.text.toString().isNotEmpty()){
                val intent  =  Intent(this,OTPActivity::class.java)
                intent.putExtra("number",binding.numberEt.text.toString())
                startActivity(intent)
            }
        }
        binding.back.setOnClickListener {
            if (storeManager.getBoolean("new",false)){
                val fragmentManager = supportFragmentManager
                val fragmentCount = fragmentManager.backStackEntryCount

                if (fragmentCount > 0) {
                    // Pop the current fragment to go back to the previous fragment
                    fragmentManager.popBackStack()
                } else {
                    // If no fragments are in the back stack, handle as needed (e.g., finish the activity)
                    finish()
                }

            }else{
                startActivity(Intent(this,WelcomeAccountActivity::class.java))
            }
        }
    }
}