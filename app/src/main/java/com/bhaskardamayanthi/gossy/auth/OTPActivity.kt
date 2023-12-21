package com.bhaskardamayanthi.gossy.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bhaskardamayanthi.gossy.managers.PhoneAuthManager
import com.bhaskardamayanthi.gossy.databinding.ActivityOtpactivityBinding
import com.bhaskardamayanthi.gossy.localStore.StoreManager
import com.bhaskardamayanthi.gossy.managers.OTPTimerManager
import com.bhaskardamayanthi.gossy.managers.TokenManager


class  OTPActivity : AppCompatActivity(),OTPTimerManager.OnTimerTickListener{
    private lateinit var binding: ActivityOtpactivityBinding
    private lateinit var otpManager:OTPTimerManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        otpManager = OTPTimerManager(this@OTPActivity)

        val storeManager = StoreManager(this)
        val phoneNumber = "+91"+intent.getStringExtra("number")
        storeManager.saveString("number",phoneNumber)


        val phoneAuthManager = PhoneAuthManager(this,otpManager)
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
            if (storeManager.getBoolean("isLogin",false)) {
                val intent = Intent(this@OTPActivity, FetchDataLoadingActivity::class.java)
                startActivity(intent)
            }else {
                val token = TokenManager(this)
                token.saveTokenLocally()
                val intent = Intent(this@OTPActivity, UserDataActivity::class.java)
                startActivity(intent)
            }
        }


    }
    override fun onTick(secondsRemaining: Long) {
        // Update UI with the remaining seconds
        binding.otpTimmer.text = "Seconds remaining: $secondsRemaining"
    }

    override fun onFinish() {
        // Handle timer completion
        binding.otpTimmer.text = "Timer finished"
    }

    override fun onDestroy() {
        super.onDestroy()
        // Cancel the timer to avoid memory leaks
        otpManager.cancel()
    }





}