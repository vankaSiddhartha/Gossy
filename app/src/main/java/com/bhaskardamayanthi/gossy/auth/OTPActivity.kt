package com.bhaskardamayanthi.gossy.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bhaskardamayanthi.gossy.managers.PhoneAuthManager
import com.bhaskardamayanthi.gossy.databinding.ActivityOtpactivityBinding
import com.bhaskardamayanthi.gossy.localStore.StoreManager


class OTPActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtpactivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val storeManager = StoreManager(this)
        val phoneNumber = "+91"+intent.getStringExtra("number")
        storeManager.saveString("number",phoneNumber)


        val phoneAuthManager = PhoneAuthManager(this)
        if (phoneNumber.toString().isNotEmpty()) {
            phoneAuthManager.sendVerificationCode(phoneNumber?:"",this)
        }

        binding.upload.setOnClickListener {
            if (binding.otpEt.text.toString().isNotEmpty())
            {
                val otp = binding.otpEt.text.toString()
                phoneAuthManager.verifyOTP(otp,phoneNumber)
            }else{
                SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setTitleText("Oops...")
                    .setContentText(it.toString()).show()
            }
        }
        binding.upload.setOnClickListener {
            val intent = Intent(this@OTPActivity, UserDataActivity::class.java)

            startActivity(intent)
        }


    }





}