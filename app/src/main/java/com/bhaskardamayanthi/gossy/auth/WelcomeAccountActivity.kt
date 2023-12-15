package com.bhaskardamayanthi.gossy.auth


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bhaskardamayanthi.gossy.MainActivity
import com.bhaskardamayanthi.gossy.databinding.ActivityWelcomeAccountBinding
import com.bhaskardamayanthi.gossy.localStore.StoreManager
import com.bhaskardamayanthi.gossy.managers.TokenManager
import com.google.firebase.auth.FirebaseAuth


class WelcomeAccountActivity : AppCompatActivity() {
    private lateinit var binding:ActivityWelcomeAccountBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(androidx.appcompat.R.style.Theme_AppCompat)
        supportActionBar?.hide()

        binding = ActivityWelcomeAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val token = TokenManager(this)
        token.saveTokenLocally()
      val textView =binding.loginText
        val blueColor = ForegroundColorSpan(Color.BLUE)
        val spannableString = SpannableString(textView.text)

// Privacy Policy
        val privacyPolicyText = "Privacy policy"
        val privacyPolicyStartIndex = spannableString.indexOf(privacyPolicyText)
        val privacyPolicyEndIndex = privacyPolicyStartIndex + privacyPolicyText.length
        if (privacyPolicyStartIndex != -1) {
            spannableString.setSpan(blueColor, privacyPolicyStartIndex, privacyPolicyEndIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

// Terms of Service
        val termsOfServiceText = "terms of service"
        val termsOfServiceStartIndex = spannableString.indexOf(termsOfServiceText)
        val termsOfServiceEndIndex = termsOfServiceStartIndex + termsOfServiceText.length
        if (termsOfServiceStartIndex != -1) {
            spannableString.setSpan(blueColor, termsOfServiceStartIndex, termsOfServiceEndIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        textView.text = spannableString
        binding.loginText.setOnClickListener {
            startActivity(Intent(this,PermissionActivity::class.java))
        }
        binding.upload.setOnClickListener {
            startActivity(Intent(this,PhoneAuthActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        val storeManager = StoreManager(this)
        val isLog = storeManager.getString("number","0")
        if (isLog !="0") {
            startActivity(Intent(this, MainActivity::class.java))
            finishAffinity()
        }
    }
}