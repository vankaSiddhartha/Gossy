package com.bhaskardamayanthi.gossy.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bhaskardamayanthi.gossy.R
import com.bhaskardamayanthi.gossy.databinding.ActivityPermissionBinding
import com.bhaskardamayanthi.gossy.databinding.ActivityPhoneAuthBinding

class PhoneAuthActivity : AppCompatActivity() {
    private lateinit var binding:ActivityPhoneAuthBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityPhoneAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.upload.setOnClickListener {
            if (binding.numberEt.text.toString().isNotEmpty()){
                val intent  =  Intent(this,OTPActivity::class.java)
                intent.putExtra("number",binding.numberEt.text.toString())
                startActivity(intent)
            }
        }
    }
}