package com.bhaskardamayanthi.gossy.webViewActivity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bhaskardamayanthi.gossy.databinding.ActivityWebViewBinding
import com.bhaskardamayanthi.gossy.loading.Loading.dismissDialogForLoading
import com.bhaskardamayanthi.gossy.loading.Loading.showAlertDialogForLoading

class WebViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWebViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showAlertDialogForLoading(this)
        val webView: WebView = binding.web
        val link = intent.getStringExtra("link")
        val  headText = intent.getStringExtra("headText")
        binding.back.setOnClickListener {
            finish()
        }

        // Enable JavaScript
        webView.settings.javaScriptEnabled = true

        // Enable caching

      //  webView.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK

        // Optimize WebView load
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true

        // Enable hardware acceleration
        webView.setLayerType(WebView.LAYER_TYPE_HARDWARE, null)

        // Load the URL
        link?.let {
            //Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            webView.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    // Hide ProgressBar when page finishes loading
                    dismissDialogForLoading()
                }
            }

            webView.loadUrl(it)
            binding.headText.text= headText
        }
    }
}