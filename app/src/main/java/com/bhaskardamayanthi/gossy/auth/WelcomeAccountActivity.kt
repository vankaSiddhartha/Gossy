package com.bhaskardamayanthi.gossy.auth


import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bhaskardamayanthi.gossy.R
import com.bhaskardamayanthi.gossy.databinding.ActivityWelcomeAccountBinding


class WelcomeAccountActivity : AppCompatActivity() {
    private lateinit var binding:ActivityWelcomeAccountBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(androidx.appcompat.R.style.Theme_AppCompat)
        supportActionBar?.hide()
        binding = ActivityWelcomeAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
      val textView =binding.loginText
        val spannableString = SpannableString(textView.text)
        val blueColor = ForegroundColorSpan(
            ContextCompat.getColor(
                this,
                R.color.btnBlue
            )
        ) // Replace R.color.blue with your desired blue color resource


        val startIndex = textView.text.toString().indexOf("Log in")
        val endIndex = startIndex + "Log in".length
        spannableString.setSpan(blueColor, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        textView.text = spannableString
    }
}