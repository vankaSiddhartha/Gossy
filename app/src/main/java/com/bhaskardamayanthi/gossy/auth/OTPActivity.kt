package com.bhaskardamayanthi.gossy.auth

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bhaskardamayanthi.gossy.MainActivity
import com.bhaskardamayanthi.gossy.R
import com.bhaskardamayanthi.gossy.managers.PhoneAuthManager
import com.bhaskardamayanthi.gossy.broadcast.SmsBroadcastReceiver
import com.bhaskardamayanthi.gossy.broadcast.SmsBroadcastReceiver.SmsBroadcastReceiverListener
import com.bhaskardamayanthi.gossy.databinding.ActivityOtpactivityBinding
import com.bhaskardamayanthi.gossy.localStore.StoreManager
import com.google.android.gms.auth.api.phone.SmsRetriever
import org.jetbrains.annotations.Nullable
import java.util.regex.Matcher
import java.util.regex.Pattern


class OTPActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtpactivityBinding
    private val REQ_USER_CONSENT = 200
   private lateinit var smsBroadcastReceiver: SmsBroadcastReceiver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val phoneNumber = "+91"+intent.getStringExtra("number")
        val phoneAuthManager = PhoneAuthManager(this)
        if (phoneNumber.toString().isNotEmpty()) {
            phoneAuthManager.sendVerificationCode(phoneNumber?:"",this)
        }

        binding.upload.setOnClickListener {
            if (binding.editTextDigit1.text.toString().isNotEmpty()&&binding.editTextDigit2.text.toString().isNotEmpty()&&binding.editTextDigit3.text.toString().isNotEmpty()&&binding.editTextDigit4.text.toString().isNotEmpty()&&binding.editTextDigit5.text.toString().isNotEmpty()&&binding.editTextDigit6.text.toString().isNotEmpty())
            {
                val otp = binding.editTextDigit1.text.toString()+binding.editTextDigit2.text.toString()+binding.editTextDigit3.text.toString()+binding.editTextDigit4.text.toString()+binding.editTextDigit5.text.toString()+binding.editTextDigit6.text.toString()
                phoneAuthManager.verifyOTP(otp)
            }
        }
        binding.upload.setOnClickListener {
            startActivity(Intent(this@OTPActivity, FetchDataLoadingActivity::class.java))
        }
//        binding.editTextDigit6.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                // Not used, but required by TextWatcher interface
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                // Not used, but required by TextWatcher interface
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//                val name = s?.toString() ?: ""
//                if (name.isNotEmpty()) {
//
//                    binding.upload.setCardBackgroundColor(ContextCompat.getColor(this@OTPActivity, R.color.btnBlue))
//
//
//                        startActivity(Intent(this@OTPActivity, FetchDataLoadingActivity::class.java))
//
//                } else {
//                    // If text is empty, you can reset the card view color to default here
//                    binding.upload.setCardBackgroundColor(ContextCompat.getColor(this@OTPActivity, R.color.grey))
//
//                }
//            }
//        })
        try {
            startSmartUserConsent();
        }catch (e:Exception){
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
        }

    }
    private fun startSmartUserConsent() {
        val client = SmsRetriever.getClient(this)
        client.startSmsUserConsent(null)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_USER_CONSENT) {
            if (resultCode == RESULT_OK && data != null) {
                val message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)
                getOtpFromMessage(message)
            }
        }
    }

    private fun getOtpFromMessage(message: String?) {
        val otpPattern: Pattern = Pattern.compile("(|^)\\d{6}")
        val matcher: Matcher = otpPattern.matcher(message ?: "")

        if (matcher.find()) {
            val otp = matcher.group(0)

            if (otp != null) {
                if (otp.length == 6) {
                    binding.editTextDigit1.setText(otp[0].toString())
                    binding.editTextDigit2.setText(otp[1].toString())
                    binding.editTextDigit3.setText(otp[2].toString())
                    binding.editTextDigit4.setText(otp[3].toString())
                    binding.editTextDigit5.setText(otp[4].toString())
                    binding.editTextDigit6.setText(otp[5].toString())
                }
            }
        }
    }
    private fun registerBroadcastReceiver() {
        smsBroadcastReceiver = SmsBroadcastReceiver()
        smsBroadcastReceiver!!.smsBroadcastReceiverListener =
            object : SmsBroadcastReceiverListener {
                override fun onSuccess(intent: Intent?) {
                    startActivityForResult(intent!!, REQ_USER_CONSENT)
                }

                override fun onFailure() {}
            }
        val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        registerReceiver(smsBroadcastReceiver, intentFilter)
    }

    override fun onStart() {
        super.onStart()
        try {
            registerBroadcastReceiver()
        }catch (e:Exception){
            Toast.makeText(this, e.message.toString(), Toast.LENGTH_SHORT).show()
        }

    }

    override fun onStop() {
        super.onStop()
        try {
            unregisterReceiver(smsBroadcastReceiver)
        }catch (e:Exception){
            Toast.makeText(this, e.message.toString(), Toast.LENGTH_SHORT).show()
        }

    }
}