package com.bhaskardamayanthi.gossy.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bhaskardamayanthi.gossy.R
import com.bhaskardamayanthi.gossy.databinding.ActivityPermissionBinding

class PermissionActivity : AppCompatActivity() {
    private lateinit var binding:ActivityPermissionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityPermissionBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}