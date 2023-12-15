package com.bhaskardamayanthi.gossy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bhaskardamayanthi.gossy.anonymousPost.AnonymousPostFragment
import com.bhaskardamayanthi.gossy.databinding.ActivityMainBinding
import com.bhaskardamayanthi.gossy.localStore.StoreManager
import com.bhaskardamayanthi.gossy.managers.FragmentIntentManager.intentFragment
import com.bhaskardamayanthi.gossy.uploadPost.UploadPostFragment
import com.bumptech.glide.Glide

class MainActivity : AppCompatActivity() {
   private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        val storeManager = StoreManager(this)
        intentFragment(R.id.frag,AnonymousPostFragment(),this,"AnonymousPostFrag")
        binding.titleText.text = "Gossips"
        val fakeImg = storeManager.getString("fakeImg","")
        Glide.with(this).load(fakeImg).into(binding.profile)
        binding.bottomNavigationView.setOnItemSelectedListener{menuItem->
            when (menuItem.itemId) {
               R.id.home->{
                   intentFragment(R.id.frag,AnonymousPostFragment(),this,"AnonymousPostFrag")
                   binding.titleText.text = "Gossips"
                   true
               }
                R.id.uploadPost->{
                    intentFragment(R.id.frag,UploadPostFragment(),this,"UploadPostFrag")
                    binding.titleText.text = "Upload post"
                    true
                }
                else->{
                    true
                }
            }

        }
    }
}