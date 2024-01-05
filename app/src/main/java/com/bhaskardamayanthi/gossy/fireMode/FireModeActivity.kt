package com.bhaskardamayanthi.gossy.fireMode

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bhaskardamayanthi.gossy.R
import com.bhaskardamayanthi.gossy.databinding.ActivityFireModeBinding


class FireModeActivity() : AppCompatActivity() {

private lateinit var binding:ActivityFireModeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFireModeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.upload.setOnClickListener {
            val intent = Intent(this,VankaPaymentGateway::class.java)
            startActivity(intent)
        }


    }

}